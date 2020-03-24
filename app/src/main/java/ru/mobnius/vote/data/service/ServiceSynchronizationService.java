package ru.mobnius.vote.data.service;

import android.content.Intent;
import android.os.IBinder;

import java.util.Timer;
import java.util.TimerTask;

import ru.mobnius.vote.data.BaseService;
import ru.mobnius.vote.data.Logger;
import ru.mobnius.vote.data.manager.configuration.PreferencesManager;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.data.manager.synchronization.IProgress;
import ru.mobnius.vote.data.manager.synchronization.ISynchronization;
import ru.mobnius.vote.data.manager.synchronization.ServiceSynchronization;
import ru.mobnius.vote.data.manager.synchronization.utils.transfer.Transfer;
import ru.mobnius.vote.data.manager.synchronization.utils.transfer.TransferProgress;

import static ru.mobnius.vote.utils.SyncUtil.resetTid;

/**
 * Служба по запуску служебной синхронизации
 */
public class ServiceSynchronizationService extends BaseService {

    /**
     * таймер для отправки служебных данных
     */
    final Timer serviceTimer;

    /**
     * задача по отправке служебных данных
     */
    final ServiceTask serviceTask;

    public ServiceSynchronizationService() {
        serviceTimer = new Timer();
        serviceTask = new ServiceTask();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null){
            resetTid(ServiceSynchronization.getInstance(PreferencesManager.ZIP_CONTENT));

            /*
              Интервал отправки служебных данных на сервер
             */
            int SERVICE_INTERVAL = 60 * 1000;
            int serviceInterval = intent.getIntExtra("serviceInterval", SERVICE_INTERVAL);

            serviceTimer.schedule(serviceTask, 0, serviceInterval);
        }
        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        serviceTimer.cancel();
        super.onDestroy();
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.SYNC_IN_SERVICE;
    }

    class ServiceTask extends TimerTask {

        @Override
        public void run() {
            ServiceSynchronization serviceSynchronization = ServiceSynchronization.getInstance(PreferencesManager.ZIP_CONTENT);
            serviceSynchronization.start(null, new IProgress() {
                @Override
                public void onStart(ISynchronization synchronization) {

                }

                @Override
                public void onStop(ISynchronization synchronization) {

                }

                @Override
                public void onProgress(ISynchronization synchronization, int step, String message, String tid) {

                }

                @Override
                public void onError(ISynchronization synchronization, int step, String message, String tid) {
                    Logger.debug(message);
                }

                @Override
                public void onStartTransfer(String tid, Transfer transfer) {

                }

                @Override
                public void onRestartTransfer(String tid, Transfer transfer) {

                }

                @Override
                public void onPercentTransfer(String tid, TransferProgress progress, Transfer transfer) {

                }

                @Override
                public void onStopTransfer(String tid, Transfer transfer) {

                }

                @Override
                public void onEndTransfer(String tid, Transfer transfer, Object data) {

                }

                @Override
                public void onErrorTransfer(String tid, String message, Transfer transfer) {

                }
            });
        }
    }
}
