package ru.mobnius.vote.data.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.util.Date;

import ru.mobnius.vote.data.BaseService;
import ru.mobnius.vote.data.Logger;
import ru.mobnius.vote.data.manager.DbOperationType;
import ru.mobnius.vote.data.manager.MailManager;
import ru.mobnius.vote.data.manager.SocketManager;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.data.manager.mail.GeoMail;
import ru.mobnius.vote.data.storage.models.DaoSession;
import ru.mobnius.vote.data.storage.models.Tracking;
import ru.mobnius.vote.utils.DateUtil;
import ru.mobnius.vote.utils.NetworkUtil;

/**
 * Сервис по наблюдению за геоданными
 */
public class TrackingService extends BaseService {
    private LocationManager mLocationManager;
    private TrackingLocationListener mTrackingLocationListener;

    /**
     * Значение по умолчанию для задержка перед получением следующей координаты
     */
    public final static int TRACK_TIMEOUT = 10 * 1000;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startTracking(intent);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        if(mLocationManager != null) {
            mLocationManager.removeUpdates(mTrackingLocationListener);
        }
        super.onDestroy();
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.SERVICE;
    }

    @SuppressLint("MissingPermission")
    private void startTracking(Intent intent) {
        if(intent != null) {
            mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            mTrackingLocationListener = new TrackingLocationListener(getDaoSession(), getBaseContext(), Integer.parseInt(getBasicUser().getUserId().toString()));
            int timeout = intent.getIntExtra("timeout", TRACK_TIMEOUT);
            int accuracy = intent.getIntExtra("accuracy", Criteria.ACCURACY_FINE);
            int power = intent.getIntExtra("power", Criteria.POWER_HIGH);

            Criteria criteria = new Criteria();
            criteria.setAccuracy(accuracy);
            criteria.setPowerRequirement(power);

            mLocationManager.requestLocationUpdates(timeout, 10, criteria, mTrackingLocationListener, null);
        }
    }
}
