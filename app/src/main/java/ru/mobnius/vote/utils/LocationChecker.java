package ru.mobnius.vote.utils;

import android.content.Context;
import android.location.LocationManager;

import java.util.Objects;

import ru.mobnius.vote.data.Logger;

public class LocationChecker {

    public static void start(OnLocationAvailable checker) {
        final Context context = (Context) checker;
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean isNetworkAvailable = false;

        try {
            isNetworkAvailable = Objects.requireNonNull(locationManager).isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            Logger.error(ex);
        }
        checker.onLocationAvailable(isNetworkAvailable);
    }

    public interface OnLocationAvailable {
        void onLocationAvailable(boolean enable);
    }
}
