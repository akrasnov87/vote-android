package ru.mobnius.vote.data.manager;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;

import ru.mobnius.vote.data.manager.configuration.PreferencesManager;
import ru.mobnius.vote.data.service.MyService;

/**
 * Управление серсисами приложения
 */
class ServiceManager {
    private final Context context;
    private Intent myIntent;

    public ServiceManager(Context context){
        this.context = context;
    }


    /**
     * запуск сервиса по отправке информации на сервер
     * @param serviceInterval интервал передачи служебных данных на сервер
     */
    private void startMyService(int serviceInterval, int timeout, int accuracy, int power, int telemetryInterval, boolean sdCardMemoryUsage) {
        if(myIntent != null){
            stopMyService();
        }

        myIntent = new Intent(context, MyService.class);
        myIntent.putExtra(MyService.SYNC_SERVICE, serviceInterval);
        myIntent.putExtra(MyService.TRACK_TIMEOUT, timeout);
        myIntent.putExtra(MyService.TRACK_ACCURACY, accuracy);
        myIntent.putExtra(MyService.TRACK_POWER, power);
        myIntent.putExtra(MyService.TELEMETRY_INTERVAL, telemetryInterval);
        myIntent.putExtra(MyService.TELEMETRY_MEMORY, sdCardMemoryUsage);
        context.startService(myIntent);
    }

    /**
     * запуск сервиса по отправке информации на сервер
     */
    public void startMyService() {
        this.startMyService(PreferencesManager.getInstance().getSyncInterval(), PreferencesManager.getInstance().getTrackingInterval(), Criteria.ACCURACY_FINE, Criteria.POWER_HIGH, PreferencesManager.getInstance().getTelemetryInterval(), true);
    }

    /**
     * остановка сервиса по отправке информации на сервер
     */
    public void stopMyService(){
        if(myIntent != null) {
            context.stopService(myIntent);
            myIntent = null;
        }
    }
}
