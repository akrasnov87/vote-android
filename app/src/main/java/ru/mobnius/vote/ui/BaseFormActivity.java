package ru.mobnius.vote.ui;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;

import androidx.annotation.NonNull;

import java.util.Date;

import ru.mobnius.vote.data.manager.BaseActivity;
import ru.mobnius.vote.data.manager.GeoManager;

public abstract class BaseFormActivity extends BaseActivity
        implements GeoManager.GeoListener {
    private GeoManager mGeoManager;

    private final static String LATITUDE = "latitude";
    private final static String LONGITUDE = "longitude";
    private final static String COUNT = "count";

    private double latitude;
    private double longitude;

    /**
     * количество полученных координат
     */
    private int locations;

    @SuppressLint("MissingPermission")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mGeoManager = new GeoManager(this, locations, latitude, longitude);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putDouble(LATITUDE, mGeoManager.getLatitude());
        outState.putDouble(LONGITUDE, mGeoManager.getLongitude());
        outState.putInt(COUNT, mGeoManager.getAccuracy());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        latitude = savedInstanceState.getDouble(LATITUDE);
        longitude = savedInstanceState.getDouble(LONGITUDE);
        locations = savedInstanceState.getInt(COUNT);
    }

    /**
     * Получение текущей гео-координаты
     * @return координта
     */
    @Override
    public Location getCurrentLocation() {
        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(mGeoManager.getLatitude());
        location.setLongitude(mGeoManager.getLongitude());
        location.setTime(new Date().getTime());
        location.setAccuracy(mGeoManager.getAccuracy());

        return location;
    }

    @Override
    protected void onDestroy() {
        mGeoManager.destroy();
        super.onDestroy();
    }
}
