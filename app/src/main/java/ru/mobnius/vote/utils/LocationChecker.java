package ru.mobnius.vote.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;

import ru.mobnius.vote.data.Logger;

public class LocationChecker {

    public static boolean start(OnLocationAvailable checker) {
        final Context context = (Context) checker;
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean isGPS_available = false;
        boolean isNetworkAvailable = false;

        try {
            assert locationManager != null;
            isGPS_available = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            Logger.error(ex);
        }

        try {
            isNetworkAvailable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            Logger.error(ex);
        }
        boolean enabled = !isGPS_available && !isNetworkAvailable;
        if (enabled) {
            new AlertDialog.Builder(context)
                    .setMessage("Для работы приложения необходимо включить доступ к геолокации")
                    .setPositiveButton("Включить геолокацию", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setCancelable(false)
                    .show();
        } else {
            checker.onLocationAvailable();
        }
        return enabled;
    }

    public interface OnLocationAvailable {
        void onLocationAvailable();
    }
}
