package ru.mobnius.vote.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.BaseActivity;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.data.storage.models.DaoSession;
import ru.mobnius.vote.data.storage.models.Points;
import ru.mobnius.vote.data.storage.models.PointsDao;
import ru.mobnius.vote.ui.data.BurndownChartAsyncTask;
import ru.mobnius.vote.ui.fragment.StatisticDialogFragment;
import ru.mobnius.vote.ui.model.BurndownItemModel;
import ru.mobnius.vote.utils.DateUtil;
import ru.mobnius.vote.utils.NetworkUtil;

/**
 * Статистика
 */
public class StatisticActivity extends BaseActivity
    implements BurndownChartAsyncTask.OnBurnDownLoadedListener {

    public static Intent getIntent(Context context) {
        return new Intent(context, StatisticActivity.class);
    }

    private TextView tvAllCount;
    private TextView tvDone;
    private TextView tvLost;
    private LineChart mChart;

    private long mAllCount;
    private long mDoneCount;

    private List<BurndownItemModel> mItems;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setSubtitle("Общая информация по квартирам");

        mChart = findViewById(R.id.statistic_burndown);
        mChart.getLegend().setEnabled(false);
        mChart.getDescription().setEnabled(false);
        mChart.getAxisRight().setEnabled(false);
        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        mChart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                BurndownItemModel item = mItems.get((int)value);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(item.d_date);
                return String.format("%s %s", calendar.get(Calendar.DATE), DateUtil.getMonthName(item.d_date, true));
            }
        });

        tvAllCount = findViewById(R.id.statistic_all_count);
        tvDone = findViewById(R.id.statistic_done_count);
        tvLost = findViewById(R.id.statistic_lost_count);

        DaoSession daoSession = DataManager.getInstance().getDaoSession();
        List<Points> allList = daoSession.getPointsDao().queryBuilder().where(PointsDao.Properties.N_priority.gt(0)).list();
        mAllCount = allList.size();
        mDoneCount = daoSession.getUserPointsDao().queryBuilder().count();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_stat, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        if(item.getItemId() == R.id.action_rating) {
            startActivity(RatingActivity.getIntent(this));
        }
        if(item.getItemId() == R.id.action_statistic) {
            StatisticDialogFragment dialogFragment = new StatisticDialogFragment();
            dialogFragment.show(getSupportFragmentManager(), "statistic");
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        tvAllCount.setText(String.format("%s: %s", getString(R.string.appartament_all), mAllCount));
        tvDone.setText(String.format("%s: %s", getString(R.string.appartament_done), mDoneCount));
        tvLost.setText(String.format("%s: %s", getString(R.string.appartament_lost), mAllCount - mDoneCount));
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(NetworkUtil.isNetworkAvailable(this) && NetworkUtil.isConnectionFast(this)) {
            startProgress();
            new BurndownChartAsyncTask(this).execute(mAllCount);
        } else {
            mChart.setNoDataText(getString(R.string.no_server_connection).toUpperCase());
        }

        Paint p = mChart.getPaint(Chart.PAINT_INFO);
        p.setTextSize(48);
        p.setColor(Color.rgb(244, 67, 54));
        p.setTextAlign(Paint.Align.CENTER);
        mChart.invalidate();
    }

    @Override
    public String getHelpKey() {
        return "statistic";
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.STATISTIC;
    }

    @Override
    public void onBurnDownLoaded(List<BurndownItemModel> items) {
        mItems = items;
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.rgb(244, 67, 54));

        ArrayList<Entry> entries = new ArrayList<>();
        int idx = 0;
        for(BurndownItemModel ts : items) {
            entries.add(new Entry(idx, (float) ts.n_count));
            idx++;
        }

        LineDataSet dataSet = new LineDataSet(entries, "");
        dataSet.setDrawCircles(true);
        dataSet.setValueTextSize(14);
        dataSet.setDrawFilled(true);
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int)value);
            }
        });

        LineData lineData = new LineData(dataSet);
        mChart.setData(lineData);
        dataSet.setColors(colors);
        mChart.animateXY(100, 100);
        stopProgress();
    }
}
