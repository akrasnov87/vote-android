package ru.mobnius.vote.utils;

import android.content.Context;
import android.location.LocationManager;
import ru.mobnius.vote.data.Logger;

public class LocationChecker {
    public static final int LOCATION_OFF = 0;
    public static final int LOCATION_ON_LOW_ACCURACY = 1;

    public static void start(OnLocationAvailable checker) {
        final Context context = (Context) checker;
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        try {
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                checker.onLocationAvailable(LOCATION_OFF);
            } else {
                if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    checker.onLocationAvailable(LOCATION_ON_LOW_ACCURACY);
                }
            }
        } catch (Exception ex) {
            Logger.error(ex);
        }
    }

    public interface OnLocationAvailable {
        void onLocationAvailable(int mode);
    }
}
