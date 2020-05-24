package ru.mobnius.vote.data.service;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;

import java.util.Date;
import java.util.Timer;

import ru.mobnius.vote.data.BaseService;
import ru.mobnius.vote.data.Logger;
import ru.mobnius.vote.data.manager.DbOperationType;
import ru.mobnius.vote.data.manager.MailManager;
import ru.mobnius.vote.data.manager.SocketManager;
import ru.mobnius.vote.data.manager.configuration.PreferencesManager;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.data.manager.mail.DeviceMail;
import ru.mobnius.vote.data.manager.synchronization.ServiceSynchronization;
import ru.mobnius.vote.data.storage.models.DaoSession;
import ru.mobnius.vote.data.storage.models.MobileDevices;
import ru.mobnius.vote.utils.DateUtil;
import ru.mobnius.vote.utils.HardwareUtil;
import ru.mobnius.vote.utils.VersionUtil;

import static ru.mobnius.vote.utils.SyncUtil.resetTid;

/**
 * Служба по запуску служебной синхронизации
 */
public class MyService extends BaseService {

    public static final String SYNC_SERVICE = "syncInterval";
    public static final String TRACK_TIMEOUT = "trackTimeout";
    public static final String TRACK_ACCURACY = "trackAccuracy";
    public static final String TRACK_POWER = "trackPower";
    public static final String TELEMETRY_INTERVAL = "telemetryInterval";
    public static final String TELEMETRY_MEMORY = "sdCardMemoryUsage";
    /**
     * выполнять сбор информации по SD карте или нет
     */
    public static boolean SD_CARD_MEMORY_USAGE = true;

    /**
     * таймер для отправки служебных данных
     */
    private final Timer mServiceSyncTimer;
    /**
     * таймер для сбора телеметрии
     */
    private Timer telemetryTimer;
    private LocationManager mLocationManager;
    private TrackingLocationListener mTrackingLocationListener;

    public MyService() {
        mServiceSyncTimer = new Timer();
        telemetryTimer = new Timer();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        runServiceSynchronization(intent);
        runTracking(intent);
        runTelemetry(intent);

        return START_NOT_STICKY;
    }

    private void runServiceSynchronization(Intent intent) {
        if(intent != null) {
            resetTid(ServiceSynchronization.getInstance(PreferencesManager.ZIP_CONTENT));

            /*
              Интервал отправки служебных данных на сервер
             */

            int serviceInterval = intent.getIntExtra(SYNC_SERVICE, PreferencesManager.getInstance().getSyncInterval());

            mServiceSyncTimer.schedule(new SyncTimerTask(), 0, serviceInterval);
        }
    }

    @SuppressLint("MissingPermission")
    private void runTracking(Intent intent) {
        if(intent != null) {
            mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            mTrackingLocationListener = new TrackingLocationListener(getDaoSession(), getBaseContext(), Integer.parseInt(getBasicUser().getUserId().toString()));
            int timeout = intent.getIntExtra(TRACK_TIMEOUT, PreferencesManager.getInstance().getTrackingInterval());
            int accuracy = intent.getIntExtra(TRACK_ACCURACY, Criteria.ACCURACY_FINE);
            int power = intent.getIntExtra(TRACK_POWER, Criteria.POWER_HIGH);

            Criteria criteria = new Criteria();
            criteria.setAccuracy(accuracy);
            criteria.setPowerRequirement(power);

            mLocationManager.requestLocationUpdates(timeout, 10, criteria, mTrackingLocationListener, null);
        }
    }

    private void runTelemetry(Intent intent) {
        writeDeviceInfo();

        if(intent != null) {
            int telemetryInterval = intent.getIntExtra(MyService.TELEMETRY_INTERVAL, PreferencesManager.getInstance().getTelemetryInterval());
            MyService.SD_CARD_MEMORY_USAGE = intent.getBooleanExtra(MyService.TELEMETRY_MEMORY, MyService.SD_CARD_MEMORY_USAGE);

            telemetryTimer.schedule(new TelemetryTimerTask(getBaseContext(), getDaoSession(), Integer.parseInt(getBasicUser().getUserId().toString())), 0, telemetryInterval);
        }
    }

    @Override
    public void onDestroy() {
        mServiceSyncTimer.cancel();
        if(mLocationManager != null) {
            mLocationManager.removeUpdates(mTrackingLocationListener);
        }
        if(telemetryTimer != null) {
            telemetryTimer.cancel();
            telemetryTimer.purge();
            telemetryTimer = null;
        }
        super.onDestroy();
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.SERVICE;
    }

    /**
     * записывает информацию об устройстве
     */
    private void writeDeviceInfo(){
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
                //noinspection RedundantCast
                socketManager.getSocket().emit("deviceinfo", (Object) MailManager.send(new DeviceMail(mobileDevices)));
            }catch (Exception e){
                Logger.error(e);
            }
        }
    }
}
