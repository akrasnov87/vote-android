package ru.mobnius.vote.data.manager;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import ru.mobnius.vote.R;
import ru.mobnius.vote.ui.fragment.HelpDialogFragment;
import ru.mobnius.vote.utils.HelpUtil;
import ru.mobnius.vote.utils.ThemeUtil;

/**
 * Базовое activity для приложения
 */
public abstract class BaseActivity extends ExceptionInterceptActivity {

    private boolean doubleBackToExitPressedOnce = false;
    private final boolean mIsBackToExist;
    private ProgressBar mProgressBar;

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
        ThemeUtil.changeColor(this);
        // предназначено для привязки перехвата ошибок
        onExceptionIntercept();
        View view = findViewById(android.R.id.content).getRootView();

        mProgressBar = new ProgressBar(this);
        mProgressBar.setVisibility(View.GONE);

        if(view instanceof ViewGroup) {
            ((ViewGroup)view).addView(mProgressBar, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    public void onPermissionChecked() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE};
            mPermissionLength = permissions.length;

            ActivityCompat.requestPermissions(this,
                    permissions,
                    REQUEST_PERMISSIONS);
        } else {
            onPermissionChecked();
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
                    onPermissionChecked();
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

    private boolean isBackToExist() {
        return mIsBackToExist;
    }

    protected void alert(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", null).show();
    }

    /**
     * Вывод окна сообщения
     *
     * @param listener обработчик события нажатий
     */
    public void confirmDialog(String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);

        adb.setPositiveButton(getResources().getString(R.string.yes), listener);
        adb.setNegativeButton(getResources().getString(R.string.no), listener);

        AlertDialog alert = adb.create();
        alert.setTitle(message);
        alert.show();
    }

    protected void startProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    protected void stopProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    public void showHelp(String key) {
        HelpDialogFragment helpDialogFragment = new HelpDialogFragment();
        helpDialogFragment.bind(key);
        helpDialogFragment.show(getSupportFragmentManager(), "help");
    }

    public abstract String getHelpKey();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem help = menu.findItem(R.id.help);
        if(help != null) {
            help.setVisible(HelpUtil.isShow(getHelpKey()));
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.help) {
            showHelp(getHelpKey());
        }
        return super.onOptionsItemSelected(item);
    }
}
