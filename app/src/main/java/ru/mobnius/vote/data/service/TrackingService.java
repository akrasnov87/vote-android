package ru.mobnius.vote.data.service;

import android.Manifest;
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

import com.google.gson.Gson;

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

/**
 * Сервис по наблюдению за геоданными
 */
public class TrackingService extends BaseService {
    LocationManager locationManager;

    private static final String ONLINE = "online";
    private static final String OFFLINE = "offline";

    /**
     * Значение по умолчанию для задержка перед получением следующей координаты
     */
    public final static int TIMEOUT = 10 * 1000;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null) {
            int timeout = intent.getIntExtra("timeout", TIMEOUT);
            int accuracy = intent.getIntExtra("accuracy", Criteria.ACCURACY_FINE);
            int power = intent.getIntExtra("power", Criteria.POWER_HIGH);

            Criteria criteria = new Criteria();
            criteria.setAccuracy(accuracy);
            criteria.setPowerRequirement(power);

            // TODO: 11.06.2019 добавить возможность включения разрешения самим пользователем
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // тут нет прав для получения геоданных
                Toast.makeText(this, "Нет разрешения на получения координат.", Toast.LENGTH_SHORT).show();
            }else {
                locationManager.requestLocationUpdates(timeout, 10, criteria, locationListener, null);
            }
        }
        return START_NOT_STICKY;
    }

    /**
     * Проверка на доступность сети интернет
     * @param context activity
     * @return true - интернет есть
     */
    private boolean isNetworkAvailable(Context context){
        ConnectivityManager manager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert manager != null;
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            // Network is present and connected
            isAvailable = true;
        }
        return isAvailable;
    }

    @Override
    public void onDestroy() {
        if(locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
        super.onDestroy();
    }

    final LocationListener locationListener = new LocationListener() {
        /**
         * Обработчик изменение местоположения пользователя (устройства)
         * @param location гео-информация
         */
        @Override
        public void onLocationChanged(Location location) {
            // TODO: 11.06.2019 в networkStatus должна передавать информации о сети в которой была получена координата, например WIFI, GPS
            String networkStatus;
            if(isNetworkAvailable(getBaseContext())){
                networkStatus = ONLINE;
            }else{
                networkStatus = OFFLINE;
            }
            DaoSession daoSession = getDaoSession();
            Tracking tracking = new Tracking();
            tracking.fn_user = Integer.parseInt(getBasicUser().getUserId().toString());
            tracking.d_date = DateUtil.convertDateToString(new Date());
            tracking.c_network_status = networkStatus;
            tracking.n_latitude = location.getLatitude();
            tracking.n_longitude = location.getLongitude();
            tracking.objectOperationType = DbOperationType.CREATED;

            daoSession.getTrackingDao().insert(tracking);

            // отправляем текущее местоположение пользователя
            SocketManager socketManager = SocketManager.getInstance();
            if(socketManager != null && socketManager.isRegistered()) {
                socketManager.getSocket().emit("tracking", (Object) MailManager.send(new GeoMail(tracking)));
            }

            Logger.debug(location.getLatitude() + ":" + location.getLongitude());
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Logger.debug("Network provider: " + provider + "(" + status + ")");
        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    public int getExceptionCode() {
        return IExceptionCode.SAVE_TRACKING;
    }
}
