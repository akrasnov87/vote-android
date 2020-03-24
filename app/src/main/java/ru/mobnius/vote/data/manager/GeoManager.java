package ru.mobnius.vote.data.manager;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.RequiresPermission;

import java.io.Serializable;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.Context.LOCATION_SERVICE;

public class GeoManager implements LocationListener, Serializable {
    private Context mContext;

    private double latitude;
    private double longitude;

    /**
     * количество полученных координат
     */
    private int locations;

    private GeoListener mGeoListener;
    private LocationManager locationManager;

    @RequiresPermission(anyOf = {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION})
    public GeoManager(Context context, int count, double latitude, double longitude) {
        mContext = context;
        locations = count;
        this.latitude = latitude;
        this.longitude = longitude;

        if(mContext instanceof GeoListener) {
            mGeoListener = (GeoListener)context;
            mGeoListener.onLocationStatusChange(count, latitude, longitude);
        }
        locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    /**
     * Точность
     * @return
     */
    public int getAccuracy() {
        return locations;
    }

    public void destroy() {
        locations = 0;
        latitude = 0;
        longitude = 0;

        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        locations++;

        if(mGeoListener != null) {
            int status;
            if (locations == 1) {
                status = GeoListener.NORMAL;
            } else {
                status = GeoListener.GOOD;
            }

            mGeoListener.onLocationStatusChange(status, latitude, longitude);
        }
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

    public interface GeoListener {
        int NONE = 0;
        int NORMAL = 1;
        int GOOD = 2;

        /**
         * Обработчик изменения статуса сигнала
         * @param status статус сигнала
         * @
         */
        void onLocationStatusChange(int status, double latitude, double longitude);

        /**
         * Получение текущей гео-координаты
         * @return координта
         */
        Location getCurrentLocation();
    }
}
