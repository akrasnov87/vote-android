package ru.mobnius.vote.ui.data;

import android.os.AsyncTask;

import java.io.IOException;

import ru.mobnius.vote.data.Logger;
import ru.mobnius.vote.data.manager.MobniusApplication;
import ru.mobnius.vote.data.manager.OnNetworkChangeListener;
import ru.mobnius.vote.data.manager.RequestManager;

public class ServerExistsAsyncTask extends AsyncTask<Boolean, String, Boolean> {

    private final OnNetworkChangeListener mListener;

    public ServerExistsAsyncTask(OnNetworkChangeListener listener) {
        mListener = listener;
    }

    private boolean online = false;

    @Override
    protected Boolean doInBackground(Boolean... booleans) {
        boolean serverExists = false;
        online = booleans[0];
        if(online) {
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
        mListener.onNetworkChange(online, aBoolean);
    }
}
