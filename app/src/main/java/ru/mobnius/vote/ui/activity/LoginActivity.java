package ru.mobnius.vote.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import java.util.Objects;

import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.BaseActivity;
import ru.mobnius.vote.data.manager.authorization.Authorization;
import ru.mobnius.vote.data.manager.authorization.AuthorizationCache;
import ru.mobnius.vote.data.manager.credentials.BasicUser;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.ui.fragment.LoginFragment;
import ru.mobnius.vote.utils.LocationChecker;


public class LoginActivity extends BaseActivity {

    public final static int LOGIN = 0;
    public final static int PIN = 1;

    public static Intent getIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    public static void setLoginFragment(AppCompatActivity context) {
        context.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.single_fragment_container, LoginFragment.newInstance())
                .commit();
        Objects.requireNonNull(context.getSupportActionBar()).setSubtitle(null);
    }

    public LoginActivity() {
        super(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        BasicUser basicUser = Authorization.getInstance().getLastAuthUser();
        String pin = "";
        if(basicUser != null) {
            AuthorizationCache cache = new AuthorizationCache(this);
            pin = cache.readPin(basicUser.getCredentials().login);
        }
        if (!pin.isEmpty()) {
            SettingActivity.setPinCodeFragment(this, pin, "Авторизация по пин-кода");
        } else {
            setLoginFragment(this);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.master_container);
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.LOGIN;
    }
}
