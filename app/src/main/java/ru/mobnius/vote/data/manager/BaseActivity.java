package ru.mobnius.vote.data.manager;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.authorization.Authorization;
import ru.mobnius.vote.data.manager.authorization.AuthorizationCache;
import ru.mobnius.vote.data.manager.credentials.BasicUser;
import ru.mobnius.vote.ui.activity.RouteListActivity;
import ru.mobnius.vote.ui.activity.SettingActivity;
import ru.mobnius.vote.ui.fragment.IPinCodeEnabledListener;
import ru.mobnius.vote.ui.fragment.PinCodeFragment;

/**
 * Базовое activity для приложения
 */
public abstract class BaseActivity extends ExceptionInterceptActivity implements IPinCodeEnabledListener {

    private boolean doubleBackToExitPressedOnce = false;
    private boolean mIsBackToExist;

    public void setBackToExist(boolean backToExist) {
        mIsBackToExist = backToExist;
    }

    private final int REQUEST_PERMISSIONS = 1;
    private int mPermissionLength = 0;

    public BaseActivity() {
        super();
        mIsBackToExist = false;
    }

    public BaseActivity(boolean isBackToExist) {
        super();
        mIsBackToExist = isBackToExist;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // предназначено для привязки перехвата ошибок
        onExceptionIntercept();
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobniusApplication application = (MobniusApplication) getApplication();
        application.addPinCodeEnabledListener(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            mPermissionLength = permissions.length;

            ActivityCompat.requestPermissions(this,
                    permissions,
                    REQUEST_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length == mPermissionLength) {
                boolean allGrant = true;
                for (int grant : grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) {
                        allGrant = false;
                        break;
                    }
                }

                if (!allGrant) {
                    Toast.makeText(this, getText(R.string.not_permissions), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, getText(R.string.permissions_grant), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, getText(R.string.not_permissions), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (isBackToExist()) {
            if (doubleBackToExitPressedOnce) {
                finishAffinity();
                finish();
                super.onBackPressed();
                return;
            }
            doubleBackToExitPressedOnce = true;

            Toast.makeText(this, getString(R.string.signOutMessage), Toast.LENGTH_LONG).show();

            int TOAST_DURATION = 2750;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, TOAST_DURATION);
        } else {
            super.onBackPressed();
        }
    }

    public DataManager getDataManager() {
        return DataManager.getInstance();
    }

    private boolean isBackToExist() {
        return mIsBackToExist;
    }

    protected void alert(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", null).show();
    }

    @Override
    public void onPinCodeEnabled() {
        Authorization mAuthorization = Authorization.getInstance();
        BasicUser mBasicUser = mAuthorization.getLastAuthUser();
        AuthorizationCache cache = new AuthorizationCache(this);
        String pin = cache.readPin(mBasicUser.getCredentials().login);
        if (!pin.isEmpty()) {
            PinCodeFragment fragment = PinCodeFragment.newInstance(pin, mBasicUser.getCredentials().login);
            getSupportFragmentManager().beginTransaction().add(getLayoutResId(), fragment).commit();
        }
    }

    private int getLayoutResId() {
        return getWindow().getDecorView().findViewById(android.R.id.content).getId();
    }
}
