package ru.mobnius.vote.ui.activity;

import androidx.fragment.app.Fragment;

import ru.mobnius.vote.ui.fragment.StatisticFragment;

public class StatisticActivity extends SingleFragmentActivity {
    public static final String ALL_POINTS = "all_apartments";
    public static final String DONE_POINTS = "done_apartments";

    @Override
    protected Fragment createFragment() {
        String all = getIntent().getStringExtra(ALL_POINTS);
        String done = getIntent().getStringExtra(DONE_POINTS);
        return StatisticFragment.openStatisticFragment(all, done, "0");
    }

    @Override
    public int getExceptionCode() {
        return 0;
    }
}
