package ru.mobnius.vote.data.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import java.io.IOException;

import ru.mobnius.vote.data.Logger;
import ru.mobnius.vote.data.Network;
import ru.mobnius.vote.utils.NetworkUtil;

/**
 * отслеживание подключение к сети
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    private Context mContext;
    private Network mNetwork;
    private ExistsAsync mExistsAsync;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        mNetwork = NetworkUtil.requestStatus(context);

        if(context instanceof OnNetworkChangeListener) {
            if(mExistsAsync != null && !mExistsAsync.isCancelled()) {
                mExistsAsync.cancel(true);
            }

            mExistsAsync = new ExistsAsync();
            mExistsAsync.execute();
        }
    }

    public class ExistsAsync extends AsyncTask<Void, String, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean serverExists = false;
            if(mNetwork.onLine) {
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
            ((OnNetworkChangeListener)mContext).onNetworkChange(mNetwork.onLine, aBoolean);
        }
    }
}
