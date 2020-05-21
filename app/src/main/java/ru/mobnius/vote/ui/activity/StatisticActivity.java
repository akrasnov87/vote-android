package ru.mobnius.vote.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.BaseActivity;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;

/**
 * Статистика
 */
public class StatisticActivity extends BaseActivity {
    private TextView tvAppartamentCount;
    private TextView tvAppartamentOpen;
    private TextView tvSignatureCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        tvAppartamentCount = findViewById(R.id.statistic_appartament_count);
        tvAppartamentOpen = findViewById(R.id.statistic_appartament_open);
        tvSignatureCount = findViewById(R.id.statistic_signature_count);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        tvAppartamentCount.setText(String.format(getString(R.string.apartment_count), "0"));
        tvAppartamentOpen.setText(String.format(getString(R.string.apartment_open), "0"));
        tvSignatureCount.setText(String.format(getString(R.string.signs_received), "0"));
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.STATISTIC;
    }
}
