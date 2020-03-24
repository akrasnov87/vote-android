package ru.mobnius.vote.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import ru.mobnius.vote.Names;
import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.GeoManager;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.data.storage.models.DaoSession;
import ru.mobnius.vote.data.storage.models.Results;
import ru.mobnius.vote.ui.fragment.form.controlMeterReadings.ControlMeterReadingsFragment;
import ru.mobnius.vote.ui.fragment.form.BaseFormActivity;

public class ControlMeterReadingsActivity extends BaseFormActivity {
    public static String TAG = "METER_READINGS";
    private Menu actionMenu;

    /**
     * Создание нового результата
     *
     * @param context
     * @param routeId маршрут, Routes
     * @param pointId точка маршрута, Points
     * @param outputTypeId тип создаваемого результата, ResultTypes
     * @return
     */
    public static Intent newIntent(Context context, String routeId, String pointId, long outputTypeId) {
        Intent intent = new Intent(context, ControlMeterReadingsActivity.class);
        intent.putExtra(Names.POINT_ID, pointId);
        intent.putExtra(Names.ROUTE_ID, routeId);
        intent.putExtra(Names.RESULT_TYPE_ID, outputTypeId);
        return intent;
    }

    /**
     * Редактирование существующего результата
     *
     * @param context
     * @param resultId иден. результата, Results
     * @return
     */
    public static Intent newIntent(Context context, String resultId) {
        Intent intent = new Intent(context, ControlMeterReadingsActivity.class);
        intent.putExtra(Names.RESULT_ID, resultId);
        return intent;
    }


    @Override
    protected Fragment createFragment() {
        Intent intent = getIntent();
        if (intent.hasExtra(Names.RESULT_ID)) {
            return ControlMeterReadingsFragment.updateInstance(intent.getStringExtra(Names.RESULT_ID));
        } else {
            return ControlMeterReadingsFragment.createInstance(
                    intent.getStringExtra(Names.ROUTE_ID),
                    intent.getStringExtra(Names.POINT_ID),
                    intent.getLongExtra(Names.RESULT_TYPE_ID, 0));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_choice_document, menu);
        actionMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.choiceDocument_Info:
                if (getIntent().hasExtra(Names.RESULT_ID)) {
                    DaoSession daoSession = DataManager.getInstance().getDaoSession();
                    String resultId = getIntent().getStringExtra(Names.RESULT_ID);
                    Results mResult = daoSession.getResultsDao().load(resultId);
                    startActivity(PointInfoActivity.newIntent(this, mResult.fn_point));
                } else {
                    startActivity(PointInfoActivity.newIntent(this, getIntent().getStringExtra(Names.POINT_ID)));
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.CONTROL_METER_READINGS;
    }

    /**
     * Обработчик получения координат
     * @param status статус сигнала: GeoListener.NONE, GeoListener.NORMAL, GeoListener.GOOD
     * @param latitude
     * @param longitude
     */
    @Override
    public void onLocationStatusChange(int status, double latitude, double longitude) {
        Log.d(TAG, "Статус: " + status);

        if(actionMenu != null) {
            int icon = -1;
            String message = "";
            switch (status) {
                case GeoManager.GeoListener.NONE:
                    icon = R.drawable.ic_gps_off_24px;
                    message = "Местоположение не определено.";
                    break;

                case GeoManager.GeoListener.NORMAL:
                    icon = R.drawable.ic_gps_not_fixed_24px;
                    message = "Координата не является точной.";
                    break;

                default:
                    icon = R.drawable.ic_gps_fixed_24px;
                    message = "Координата получена.";
                    break;
            }

            final int MENU_GPS_ITEM = 0;
            MenuItem menuItem = actionMenu.getItem(MENU_GPS_ITEM);
            menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

            menuItem.setIcon(icon);
            menuItem.setTitle(message);
        }
    }
}
