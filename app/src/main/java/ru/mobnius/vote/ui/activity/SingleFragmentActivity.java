package ru.mobnius.vote.ui.activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.BaseActivity;

public abstract class SingleFragmentActivity extends BaseActivity {

    private Fragment mFragment;

    public SingleFragmentActivity(boolean backToExist) {
        super(backToExist);
    }

    public SingleFragmentActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.master_container);
        FragmentManager fm = getSupportFragmentManager();

        mFragment = fm.findFragmentById(R.id.single_fragment_container);
        if (mFragment == null) {
            mFragment = createFragment();
            if(mFragment != null) {
                fm.beginTransaction()
                        .add(R.id.single_fragment_container, mFragment)
                        .commit();
            }
        }
    }

    protected abstract Fragment createFragment();
}