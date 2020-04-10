package ru.mobnius.vote.ui.activity;

import androidx.fragment.app.Fragment;

import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.ui.fragment.PrefFragment;

public class SettingActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new PrefFragment();
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.SETTING;
    }
}
