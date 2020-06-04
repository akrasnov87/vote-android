package ru.mobnius.vote.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import ru.mobnius.vote.data.manager.BaseActivity;

public abstract class BaseFormActivity extends BaseActivity
        implements LocationListener {

    public final static int NONE = 0;
    public final static int NORMAL = 1;
    public final static int GOOD = 2;

    private final static String LOCATION = "location";
    private final static String COUNT = "count";

    private Location mLocation;

    private LocationManager locationManager;

    /**
     * количество полученных координат
     */
    private int locations;

    public Location getLocation() {
        if(mLocation == null) {
            Location location = new Location("");
            location.setLatitude(0);
            location.setLongitude(0);
            return location;
        }
        return mLocation;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean b = super.onCreateOptionsMenu(menu);
        if(locations > 0) {
            --locations;
            onLocationChanged(mLocation);
        }

        return b;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            alert("Нет разрешений на использование геолокации");
            return;
        }

        if(locations < 2) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        locationManager.removeUpdates(this);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(LOCATION, mLocation);
        outState.putInt(COUNT, locations);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mLocation = savedInstanceState.getParcelable(LOCATION);
        locations = savedInstanceState.getInt(COUNT);
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
        locations++;

        int status;
        if (locations == 1) {
            status = NORMAL;
        } else {
            status = GOOD;
            locationManager.removeUpdates(this);
        }

        onLocationStatusChange(status, location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void onLocationStatusChange(int status, Location location) {

    }
}
