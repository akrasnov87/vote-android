package ru.mobnius.vote.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class NetworkUtil {

    /**
     * Проверка на доступность сети интернет
     *
     * @param context контекст
     * @return true - интернет есть
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager =
                (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        assert manager != null;
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            // Network is present and connected
            isAvailable = true;
        }
        return isAvailable;
    }

    public static boolean isConnectionFast(Context context) {
        TelephonyManager mTelephonyManager = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        int networkType = mTelephonyManager.getNetworkType();

        if (!isNetworkAvailable(context)) {
            return false;
        }

        if (isWifiConnection(context)) {
            return true;
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                return getNetworkClassForQ(context);
            } else {
                return getNetworkClass(networkType);
            }

        }
    }

    public static boolean isWifiConnection(Context context) {
        NetworkInfo info = getInfo(context);
        if (info == null || !info.isConnected()) {
            return false;
        }
        return info.getType() == ConnectivityManager.TYPE_WIFI;
    }

    public static NetworkInfo getInfo(Context context) {
        return ((ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();
    }

    public static boolean getNetworkClass(int subType) {
        switch (subType) {
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
            case TelephonyManager.NETWORK_TYPE_LTE:
                return true;

            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:

            default:
                return false;
        }
    }

    @SuppressLint({"MissingPermission", "NewApi"})
    public static boolean getNetworkClassForQ(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        Network nw = connectivityManager.getActiveNetwork();
        NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);
        if (actNw != null) {
            if (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true;
            }
            if (actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                return true;
            }
            if (actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                return getNetworkClass(tm.getDataNetworkType());
            }
        }
        return false;
    }
}
