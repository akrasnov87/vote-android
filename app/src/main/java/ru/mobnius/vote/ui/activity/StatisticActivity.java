package ru.mobnius.vote.ui.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.BaseActivity;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;

/**
 * Статистика
 */
public class StatisticActivity extends BaseActivity {
    private TextView apartmentsTotal;
    private TextView apartmentsOpen;
    private TextView signaturesReceived;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_statistic);

        apartmentsTotal = findViewById(R.id.fStatistic_tvAppTotal);
        apartmentsOpen = findViewById(R.id.fStatistic_tvOpenApp);
        signaturesReceived = findViewById(R.id.fStatistic_tvSignsTotal);
    }

    @Override
    public void onStart() {
        super.onStart();

        apartmentsTotal.setText(String.format(getString(R.string.apartment_count), "0"));
        apartmentsOpen.setText(String.format(getString(R.string.apartment_open), "0"));
        signaturesReceived.setText(String.format(getString(R.string.signs_received), "0"));
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.STATISTIC;
    }
}
