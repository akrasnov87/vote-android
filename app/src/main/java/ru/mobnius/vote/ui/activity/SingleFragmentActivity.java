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

    public void setFragment(Fragment fragment) {
        mFragment = fragment;
    }

    public Fragment getFragment() {
        return mFragment;
    }

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

    protected abstract Fragment createFragment();
}