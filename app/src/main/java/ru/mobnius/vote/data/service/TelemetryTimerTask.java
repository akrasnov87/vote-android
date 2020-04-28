package ru.mobnius.vote.data.service;

import android.app.ActivityManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.util.Date;
import java.util.Objects;
import java.util.TimerTask;

import ru.mobnius.vote.data.Logger;
import ru.mobnius.vote.data.manager.DbOperationType;
import ru.mobnius.vote.data.storage.models.DaoSession;
import ru.mobnius.vote.data.storage.models.MobileIndicators;
import ru.mobnius.vote.utils.DateUtil;
import ru.mobnius.vote.utils.HardwareUtil;
import ru.mobnius.vote.utils.NetworkUtil;

import static android.content.Context.ACTIVITY_SERVICE;

public class TelemetryTimerTask extends TimerTask {
    private Context mContext;
    private DaoSession mDaoSession;
    private long mUserId;

    public TelemetryTimerTask(Context context, DaoSession daoSession, long userId) {
        mContext = context;
        mDaoSession = daoSession;
        mUserId = userId;
    }

    @Override
    public void run() {
        MobileIndicators mobileIndicators = new MobileIndicators();

        ConnectivityManager manager =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert manager != null;
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        mobileIndicators.b_isonline = NetworkUtil.requestStatus(mContext).onLine;
        if(networkInfo != null) {
            mobileIndicators.c_network_type = Objects.requireNonNull(networkInfo).getSubtypeName();
        }

        ActivityManager actManager = (ActivityManager) mContext.getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        assert actManager != null;
        actManager.getMemoryInfo(memInfo);

        mobileIndicators.n_ram = memInfo.totalMem;
        mobileIndicators.n_used_ram = memInfo.availMem;

        StatFs stat2 = new StatFs(Environment.getDataDirectory().getPath());
        mobileIndicators.n_phone_memory = stat2.getTotalBytes();
        Logger.debug("Total MB : " + Formatter.formatFileSize(mContext, mobileIndicators.n_phone_memory));
        mobileIndicators.n_used_phone_memory = stat2.getAvailableBytes();
        Logger.debug("Available MB : " + Formatter.formatFileSize(mContext, mobileIndicators.n_used_phone_memory));
        String state = Environment.getExternalStorageState();
        if (MyService.SD_CARD_MEMORY_USAGE && Environment.MEDIA_MOUNTED.equals(state))
        {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                long[] externalSdCardSize = getExternalSdCardSize();

                mobileIndicators.n_sd_card_memory = externalSdCardSize[0];
                Logger.debug("SD Total MB : " + Formatter.formatFileSize(mContext, mobileIndicators.n_sd_card_memory));
                mobileIndicators.n_used_sd_card_memory = externalSdCardSize[1];
                Logger.debug("SD Available MB : " + Formatter.formatFileSize(mContext, mobileIndicators.n_used_sd_card_memory));
            }
        }

        mobileIndicators.n_battery_level = HardwareUtil.getBatteryPercentage(mContext);
        mobileIndicators.fn_user = mUserId;
        mobileIndicators.d_date = DateUtil.convertDateToString(new Date());

        mobileIndicators.setObjectOperationType(DbOperationType.CREATED);
        mDaoSession.getMobileIndicatorsDao().insert(mobileIndicators);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private long[] getExternalSdCardSize() {
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
}
