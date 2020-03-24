package ru.mobnius.vote.ui.activity;

import androidx.fragment.app.Fragment;

import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.ui.fragment.SynchronizationFragment;

public class SynchronizationActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new SynchronizationFragment();
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.SYNCHRONIZATION;
    }
}
