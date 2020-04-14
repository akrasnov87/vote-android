package ru.mobnius.vote.data.manager;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;

import ru.mobnius.vote.data.service.ServiceSynchronizationService;
import ru.mobnius.vote.data.service.TelemetryService;
import ru.mobnius.vote.data.service.TrackingService;

/**
 * Управление серсисами приложения
 */
public class ServiceManager {
    private Context context;
    private Intent trackingIntent;
    private Intent telemetryIntent;
    private Intent synchronizationIntent;

    public ServiceManager(Context context){
        this.context = context;
    }

    /**
     * запуск сервиса по сбора геоинформации
     * @param timeout перодичность опроса в милисекундах
     * @param accuracy точно передоваемой информации см. Criteria.ACCURACY_FINE или Criteria.ACCURACY_COARSE
     * @param power потребление заряда батареи см. Criteria.POWER_HIGH
     */
    public void startTrackingService(int timeout, int accuracy, int power){
        if(trackingIntent != null){
            stopTrackingService();
        }
        trackingIntent = new Intent(context, TrackingService.class);
        trackingIntent.putExtra("timeout", timeout);
        trackingIntent.putExtra("accuracy", accuracy);
        trackingIntent.putExtra("power", power);
        context.startService(trackingIntent);
    }

    /**
     * Запуск сервиса по сбору основных показателей мобильного устройства
     * @param telemetryInterval интервал сбора информации
     * @param sdCardMemoryUsage обрабатывать sd карту или нет
     */
    public void startTelemetryService(int telemetryInterval, boolean sdCardMemoryUsage){
        if(telemetryIntent != null){
            stopTelemetryService();
        }

        telemetryIntent = new Intent(context, TelemetryService.class);
        telemetryIntent.putExtra("telemetryInterval", telemetryInterval);
        telemetryIntent.putExtra("sdCardMemoryUsage", sdCardMemoryUsage);
        context.startService(telemetryIntent);
    }

    /**
     * Запуск сервиса по сбору основных показателей мобильного устройства
     */
    public void startTelemetryService(){
        startTelemetryService(60*1000, true);
    }

    /**
     * запуск сервиса по сбора геоинформации, максимальная точность
     */
    public void startTrackingService(){
        this.startTrackingService(TrackingService.TRACK_TIMEOUT, Criteria.ACCURACY_FINE, Criteria.POWER_HIGH);
    }

    /**
     * запуск сервиса по отправке информации на сервер
     * @param serviceInterval интервал передачи служебных данных на сервер
     */
    public void startSynchronizationService(int serviceInterval){
        if(synchronizationIntent != null){
            stopSynchronizationService();
        }

        synchronizationIntent = new Intent(context, ServiceSynchronizationService.class);
        synchronizationIntent.putExtra("serviceInterval", serviceInterval);
        context.startService(synchronizationIntent);
    }

    /**
     * запуск сервиса по отправке информации на сервер
     */
    public void startSynchronizationService(){
        this.startSynchronizationService(10* 1000);
    }

    /**
     * остановка сервиса по сбору геоинформации
     */
    public void stopTrackingService(){
        if(trackingIntent != null) {
            context.stopService(trackingIntent);
            trackingIntent = null;
        }
    }

    /**
     * остановка сервиса по сбору показателей мобильного устройства
     */
    public void stopTelemetryService(){
        if(telemetryIntent != null) {
            context.stopService(telemetryIntent);
            telemetryIntent = null;
        }
    }

    /**
     * остановка сервиса по отправке информации на сервер
     */
    public void stopSynchronizationService(){
        if(synchronizationIntent != null) {
            context.stopService(synchronizationIntent);
            synchronizationIntent = null;
        }
    }
}
