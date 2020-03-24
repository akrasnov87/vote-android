package ru.mobnius.vote.ui.activity;

import androidx.fragment.app.Fragment;

import android.os.AsyncTask;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import java.io.IOException;

import ru.mobnius.vote.R;
import ru.mobnius.vote.data.Logger;
import ru.mobnius.vote.data.manager.INetworkChange;
import ru.mobnius.vote.data.manager.MobniusApplication;
import ru.mobnius.vote.data.manager.RequestManager;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.ui.fragment.LoginFragment;
import ru.mobnius.vote.utils.NetworkUtil;


public class LoginActivity extends SingleFragmentActivity implements INetworkChange {
    private MenuItem miServer;
    private MenuItem miNetwork;
    private ExistsAsync mExistsAsync;
    private boolean mOnline = false;
    private boolean mServerExists = false;

    public LoginActivity() {
        super(true);
    }

    @Override
    protected Fragment createFragment() {
        return LoginFragment.newInstance(false);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (getNetworkChangeListener() != null) {
            getNetworkChangeListener().addNetworkChangeListener(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mExistsAsync != null && !mExistsAsync.isCancelled()) {
            mExistsAsync.cancel(true);
        }

        mExistsAsync = new ExistsAsync();
        mExistsAsync.execute(NetworkUtil.isNetworkAvailable(this));
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (getNetworkChangeListener() != null) {
            getNetworkChangeListener().removeNetworkChangeListener(this);
        }
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.LOGIN;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_login, menu);
        miNetwork = menu.findItem(R.id.login_Network);
        miServer = menu.findItem(R.id.login_Server);

        // потому что onCreateOptionsMenu вызвается в разные моменты
        onNetworkChange(mOnline, mServerExists);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onNetworkChange(boolean online, boolean serverExists) {
        mOnline = online;
        mServerExists = serverExists;

        if(miNetwork != null && miServer != null) {
            miNetwork.setIcon(online ? R.drawable.ic_network_available_24dp : R.drawable.ic_network_unavailable_24dp);
            miNetwork.setTitle(online ? getString(R.string.network_availablre) : getString(R.string.network_not_available));
            miServer.setIcon(serverExists ? R.drawable.ic_server_available_24dp : R.drawable.ic_server_unavailable_24dp);
            miServer.setTitle(online ? getString(R.string.server_available) : getString(R.string.server_not_available));
        }
    }

    private class ExistsAsync extends AsyncTask<Boolean, String, Boolean> {
        boolean online = false;

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
            onNetworkChange(online, aBoolean);
        }
    }
}
