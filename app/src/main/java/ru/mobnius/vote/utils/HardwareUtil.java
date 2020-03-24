package ru.mobnius.vote.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.BatteryManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import androidx.core.app.ActivityCompat;

import java.util.Objects;

public class HardwareUtil {
    /**
     * Возвращается IMEI
     * @param context activity
     * @return Возвращается IMEI - номер
     */
    @SuppressLint("HardwareIds")
    @SuppressWarnings("deprecation")
    public static String getIMEI(Context context) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q ||
                ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Objects.requireNonNull(telephonyManager).getImei();
        }else {
            return Objects.requireNonNull(telephonyManager).getDeviceId();
        }
    }

    /**
     * Заряд батареи
     * @param context контекст
     * @return возвращается заряд батареи в процентах
     */
    public static Integer getBatteryPercentage(Context context) {

        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, iFilter);

        int level = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
        int scale = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : -1;

        float batteryPct = level / (float) scale;

        return (int) (batteryPct * 100);
    }
}
