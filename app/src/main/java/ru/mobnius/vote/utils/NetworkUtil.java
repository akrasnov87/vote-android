package ru.mobnius.vote.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import ru.mobnius.vote.data.Network;

public class NetworkUtil {

    /**
     * Проверка на доступность сети интернет
     *
     * @param context контекст
     * @return true - интернет есть
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            // Network is present and connected
            isAvailable = true;
        }
        return isAvailable;
    }

    /**
     * Запрашивает статус соединения с сетью
     */
    public static Network requestStatus(Context context){
        ru.mobnius.vote.data.manager.SocketManager socketManager = ru.mobnius.vote.data.manager.SocketManager.getInstance();
        boolean socket = false;
        if(socketManager != null)
            socket = socketManager.isConnected();

        return new Network(NetworkUtil.isNetworkAvailable(context), socket);
    }
}
