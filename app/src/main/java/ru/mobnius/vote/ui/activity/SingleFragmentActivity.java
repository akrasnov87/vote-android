package ru.mobnius.vote.ui.activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.BaseActivity;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.INetworkChange;
import ru.mobnius.vote.data.manager.MobniusApplication;

public abstract class SingleFragmentActivity extends BaseActivity {
    private boolean doubleBackToExitPressedOnce = false;

    public void setBackToExist(boolean backToExist) {
        mIsBackToExist = backToExist;
    }

    private boolean mIsBackToExist;
    private Fragment mFragment;

    public SingleFragmentActivity() {
        super();
        mIsBackToExist = false;
    }

    public SingleFragmentActivity(boolean isBackToExist) {
        super();
        mIsBackToExist = isBackToExist;
    }

    public Fragment getFragment() {
        return mFragment;
    }

    protected abstract Fragment createFragment();

    protected int getLayoutResId() {
        return R.layout.master_container;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutResId());
        FragmentManager fm = getSupportFragmentManager();

        mFragment = fm.findFragmentById(R.id.single_fragment_container);
        if (mFragment == null) {
            mFragment = createFragment();

                fm.beginTransaction()
                        .add(R.id.single_fragment_container, mFragment)
                        .commit();
        }
    }

    @Override
    public void onBackPressed() {
        if(isBackToExist()) {
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

    /**
     * Получение обработчика изменения сети
     * @return обработчик
     */
    public MobniusApplication getNetworkChangeListener() {
        if(getApplication() instanceof INetworkChange){
            return (MobniusApplication) getApplication();
        }

        return null;
    }

    public DataManager getDataManager() {
        return DataManager.getInstance();
    }
}

