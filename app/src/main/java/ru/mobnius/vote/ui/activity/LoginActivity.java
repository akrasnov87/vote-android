package ru.mobnius.vote.ui.activity;

import androidx.fragment.app.Fragment;


import ru.mobnius.vote.data.manager.OnNetworkChangeListener;
import ru.mobnius.vote.data.manager.MobniusApplication;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.ui.data.ServerExistsAsyncTask;
import ru.mobnius.vote.ui.fragment.LoginFragment;
import ru.mobnius.vote.utils.NetworkUtil;


public class LoginActivity extends SingleFragmentActivity
        implements OnNetworkChangeListener {
    private LoginFragment mLoginFragment;

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

        new ServerExistsAsyncTask(this)
                .execute(NetworkUtil.isNetworkAvailable(this));
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

}
