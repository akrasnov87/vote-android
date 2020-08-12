package ru.mobnius.vote.data.manager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;

import androidx.core.app.ActivityCompat;

import java.io.IOException;

import ru.mobnius.vote.data.Logger;
import ru.mobnius.vote.utils.NetworkUtil;

/**
 * отслеживание подключение к сети
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    private Context mContext;
    private boolean mIsOnline;
    private boolean mIsFasted;
    private ExistsAsync mExistsAsync;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        mIsOnline = NetworkUtil.isNetworkAvailable(context);
        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            mIsFasted = NetworkUtil.isConnectionFast(context);
        } else {
            mIsFasted = true;
        }

        if(context instanceof OnNetworkChangeListener) {
            if(mExistsAsync != null && !mExistsAsync.isCancelled()) {
                mExistsAsync.cancel(true);
            }

            mExistsAsync = new ExistsAsync();
            mExistsAsync.execute();
        }
    }

    @SuppressLint("StaticFieldLeak")
    class ExistsAsync extends AsyncTask<Void, String, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean serverExists = false;
            if(mIsOnline) {
                try {
                    serverExists = RequestManager.exists(MobniusApplication.getBaseUrl()) != null;
                } catch (IOException e) {
                    Logger.error(e);
                }
            }
            return serverExists;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            ((OnNetworkChangeListener)mContext).onNetworkChange(mIsOnline, aBoolean, mIsFasted);
        }
    }
}
