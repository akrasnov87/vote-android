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
    private LoginFragment mLoginFragment;
    private ExistsAsync mExistsAsync;

    public LoginActivity() {
        super(true);
    }

    @Override
    protected Fragment createFragment() {
        mLoginFragment = LoginFragment.newInstance(false);
        return mLoginFragment;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (getNetworkChangeListener() != null) {
            getNetworkChangeListener().addNetworkChangeListener(mLoginFragment);
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
            getNetworkChangeListener().removeNetworkChangeListener(mLoginFragment);
        }
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.LOGIN;
    }

    @Override
    public void onNetworkChange(boolean online, boolean serverExists) {
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
