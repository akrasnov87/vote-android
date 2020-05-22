package ru.mobnius.vote.ui.activity;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;


import ru.mobnius.vote.data.manager.OnNetworkChangeListener;
import ru.mobnius.vote.data.manager.MobniusApplication;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.ui.data.ServerExistsAsyncTask;
import ru.mobnius.vote.ui.fragment.IPinDeactivated;
import ru.mobnius.vote.ui.fragment.LoginFragment;
import ru.mobnius.vote.utils.NetworkUtil;


public class LoginActivity extends SingleFragmentActivity implements IPinDeactivated {

    public static Intent getIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    private LoginFragment mLoginFragment;

    public LoginActivity() {
        super(true);
    }

    @Override
    protected Fragment createFragment() {
        mLoginFragment = LoginFragment.newInstance();
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

    /**
     * Получение обработчика изменения сети
     * @return обработчик
     */
    public MobniusApplication getNetworkChangeListener() {
        if(getApplication() instanceof OnNetworkChangeListener){
            return (MobniusApplication) getApplication();
        }

        return null;
    }

    @Override
    public void onPinDeactivated() {

    }
}
