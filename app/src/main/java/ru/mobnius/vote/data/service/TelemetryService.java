package ru.mobnius.vote.data.service;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.util.Date;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import ru.mobnius.vote.data.BaseService;
import ru.mobnius.vote.data.Logger;
import ru.mobnius.vote.data.manager.DbOperationType;
import ru.mobnius.vote.data.manager.MailManager;
import ru.mobnius.vote.data.manager.SocketManager;
import ru.mobnius.vote.data.manager.configuration.PreferencesManager;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.data.manager.mail.DeviceMail;
import ru.mobnius.vote.data.storage.models.DaoSession;
import ru.mobnius.vote.data.storage.models.MobileDevices;
import ru.mobnius.vote.data.storage.models.MobileIndicators;
import ru.mobnius.vote.utils.DateUtil;
import ru.mobnius.vote.utils.HardwareUtil;
import ru.mobnius.vote.utils.NetworkUtil;
import ru.mobnius.vote.utils.VersionUtil;

/**
 * Сервис по сбору основных показателей мобильного устройства
 */
public class TelemetryService extends BaseService {
    /**
     * таймер для сбора телеметрии
     */
    Timer telemetryTimer;

    /**
     * задача по сбору данных для телеметрии
     */
    TelemetryTask telemetryTask;

    /**
     * выполнять сбор информации по SD карте или нет
     */
    private boolean SD_CARD_MEMORY_USAGE = true;

    public TelemetryService() {
        telemetryTimer = new Timer();
        telemetryTask = new TelemetryTask();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.debug("Запуск сервиса MobileIndicators");
        writeDeviceInfo();
        if(intent != null) {
            /*
              Интервал получения информации о телеметрии
             */
            int TELEMETRY_INTERVAL = 60 * 1000;
            int telemetryInterval = intent.getIntExtra("telemetryInterval", TELEMETRY_INTERVAL);
            SD_CARD_MEMORY_USAGE = intent.getBooleanExtra("sdCardMemoryUsage", SD_CARD_MEMORY_USAGE);

            telemetryTimer.schedule(telemetryTask, 0, telemetryInterval);
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        if(telemetryTask != null){
            telemetryTask.cancel();
            telemetryTask = null;
        }
        if(telemetryTimer != null){
            telemetryTimer.cancel();
            telemetryTimer.purge();
            telemetryTimer = null;
        }
        super.onDestroy();
    }

    /**
     * запись основных показателей для теметрии
     * @param context контекст
     */
    void writeTelemetry(Context context){
        DaoSession daoSession = getDaoSession();
        MobileIndicators mobileIndicators = new MobileIndicators();

        ConnectivityManager manager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert manager != null;
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        mobileIndicators.b_isonline = NetworkUtil.requestStatus(context).onLine;
        if(networkInfo != null) {
            mobileIndicators.c_network_type = Objects.requireNonNull(networkInfo).getSubtypeName();
        }

        ActivityManager actManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        assert actManager != null;
        actManager.getMemoryInfo(memInfo);

        mobileIndicators.n_ram = memInfo.totalMem;
        mobileIndicators.n_used_ram = memInfo.availMem;

        StatFs stat2 = new StatFs(Environment.getDataDirectory().getPath());
        mobileIndicators.n_phone_memory = stat2.getTotalBytes();
        Logger.debug("Total MB : " + Formatter.formatFileSize(this, mobileIndicators.n_phone_memory));
        mobileIndicators.n_used_phone_memory = stat2.getAvailableBytes();
        Logger.debug("Available MB : " + Formatter.formatFileSize(this, mobileIndicators.n_used_phone_memory));
        String state = Environment.getExternalStorageState();
        if (SD_CARD_MEMORY_USAGE && Environment.MEDIA_MOUNTED.equals(state))
        {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                long[] externalSdCardSize = getExternalSdCardSize();

                mobileIndicators.n_sd_card_memory = externalSdCardSize[0];
                Logger.debug("SD Total MB : " + Formatter.formatFileSize(this, mobileIndicators.n_sd_card_memory));
                mobileIndicators.n_used_sd_card_memory = externalSdCardSize[1];
                Logger.debug("SD Available MB : " + Formatter.formatFileSize(this, mobileIndicators.n_used_sd_card_memory));
            }
        }

        mobileIndicators.n_battery_level = HardwareUtil.getBatteryPercentage(getBaseContext());
        mobileIndicators.fn_user = Integer.parseInt(getBasicUser().getUserId().toString());
        mobileIndicators.d_date = DateUtil.convertDateToString(new Date());

        mobileIndicators.setObjectOperationType(DbOperationType.CREATED);
        daoSession.getMobileIndicatorsDao().insert(mobileIndicators);
    }

    /**
     * записывает информацию об устройстве
     */
    void writeDeviceInfo(){
        DaoSession daoSession = getDaoSession();
        MobileDevices mobileDevices = new MobileDevices();
        mobileDevices.b_debug = PreferencesManager.getInstance().isDebug();
        mobileDevices.c_application_version = VersionUtil.getVersionName(this);
        mobileDevices.c_architecture = System.getProperty("os.arch");
        mobileDevices.d_date = DateUtil.convertDateToString(new Date());
        mobileDevices.c_imei = HardwareUtil.getIMEI(getBaseContext());
        mobileDevices.c_phone_model = Build.MODEL;
        mobileDevices.c_sdk = String.valueOf(Build.VERSION.SDK_INT);
        mobileDevices.c_os = Build.VERSION.RELEASE;
        mobileDevices.fn_user = Integer.parseInt(getBasicUser().getUserId().toString());

        mobileDevices.setObjectOperationType(DbOperationType.CREATED);
        daoSession.getMobileDevicesDao().insert(mobileDevices);

        SocketManager socketManager = SocketManager.getInstance();
        // отправляем информаци на сервер
        if(socketManager != null && socketManager.isRegistered()){
            try {
                socketManager.getSocket().emit("deviceinfo", (Object) MailManager.send(new DeviceMail(mobileDevices)));
            }catch (Exception e){
                Logger.error(e);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    long[] getExternalSdCardSize() {
        File storage = new File("/storage");
        String external_storage_path = "";
        long[] size = new long[2];

        if (storage.exists()) {
            File[] files = storage.listFiles();

            assert files != null;
            for (File file : files) {
                if (file.exists()) {
                    try {
                        if (Environment.isExternalStorageRemovable(file)) {
                            // storage is removable
                            external_storage_path = file.getAbsolutePath();
                            break;
                        }
                    } catch (Exception e) {
                        Log.e("TAG", e.toString());
                    }
                }
            }
        }

        if (!external_storage_path.isEmpty()) {
            File external_storage = new File(external_storage_path);
            if (external_storage.exists()) {
                size[0] = totalSize(external_storage);
                size[1] = availableSize(external_storage);
            }
        }
        return size;
    }

    private long totalSize(File file) {
        StatFs stat = new StatFs(file.getPath());

        return stat.getTotalBytes();
    }
    private long availableSize(File file) {
        StatFs stat = new StatFs(file.getPath());

        return stat.getAvailableBytes();
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.SAVE_TELEMETRY;
    }

    class TelemetryTask extends TimerTask{

        @Override
        public void run() {
            writeTelemetry(getBaseContext());
        }
    }
}
