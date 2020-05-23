package ru.mobnius.vote.data.manager.synchronization.utils.transfer;

import android.app.Activity;

import com.github.nkzawa.socketio.client.Socket;

import ru.mobnius.vote.data.manager.SocketManager;
import ru.mobnius.vote.data.manager.synchronization.ISynchronization;

import static ru.mobnius.vote.data.GlobalSettings.STATUS_TRANSFER_SPEED;

public abstract class Transfer {

    final String UPLOAD_TAG = "UPLOAD_TRANSFER";
    final String DOWNLOAD_TAG = "DOWNLOAD_TRANSFER";

    final String EVENT_UPLOAD = "upload";
    final String EVENT_DOWNLOAD = "download";

    /**
     * размер передоваемых данных
     */
    private final int CHUNK = 1024;

    /**
     * вычисленный размер блоков для передачи за секунду
     */
    private long calcChunk = CHUNK;

    /**
     * интервал в течении которого происходят вычисления
     */
    static final int INTERVAL = 250;

    /**
     * Подключение через websocket
     */
    final Socket socket;

    /**
     * версия протокола синхронизации
     */
    final String protocolVersion;

    /**
     * текущая активность
     */
    final Activity context;

    /**
     * слушатель "регистрации" пользователя на сервере
     */
    private TransferRegistryListener transferRegistryListener;

    /**
     * слушатель потери соединения с сервером
     */
    private TransferDisconnectListener transferDisconnectListener;

    /**
     * обработчик обратного вызова
     */
    ITransferStatusCallback callback;

    /**
     * идентификатор транзакции
     */
    final String tid;

    final ISynchronization synchronization;

    /**
     * конструктор
     * @param synchronization текущая синхронизация в рамках которой выполняется процесс
     * @param socket сокет соединение
     * @param version версия синхронизации
     * @param context интерфейс
     * @param tid идентификатор транзакции
     */
    Transfer(ISynchronization synchronization, Socket socket, String version, Activity context, String tid){
        this.socket = socket;
        this.context = context;
        protocolVersion = version;
        this.synchronization = synchronization;
        this.tid = tid;
    }

    /**
     * Настройка слушителя отсуствия соединения с сервером
     */
    void disconnectListener(){
        removeDisconnectListener();

        transferDisconnectListener = new TransferDisconnectListener(context, tid, this, callback);
        transferRegistryListener = new TransferRegistryListener(context, tid, this, callback);

        socket.on(Socket.EVENT_DISCONNECT, transferDisconnectListener);
        socket.on(SocketManager.EVENT_REGISTRY, transferRegistryListener);
    }

    /**
     * Удаление слушителя о соединении с сервером
     */
    private void removeDisconnectListener(){
        if(socket != null){
            if(transferRegistryListener != null){
                socket.off(SocketManager.EVENT_REGISTRY, transferRegistryListener);
                transferRegistryListener = null;
            }

            if(transferDisconnectListener != null){
                socket.off(Socket.EVENT_DISCONNECT, transferDisconnectListener);
                transferDisconnectListener = null;
            }
        }
    }

    /**
     * Размер блока для отправки на сервер
     * @return возвращается размер
     */
    int getChunk(){
        if(STATUS_TRANSFER_SPEED){
            return CHUNK;
        }else {
            return (int) calcChunk;
        }
    }

    /**
     * обновление размера блока
     * @param chunk размер блока
     */
    void updateChunk(long chunk){
        if(chunk > 0){
            calcChunk = chunk;
        }else{
            if(calcChunk + chunk > 0){
                calcChunk = calcChunk + chunk;
            }
        }
    }

    /**
     * очистка данных
     */
    public void destroy(){
        removeDisconnectListener();
        removeListener();
    }

    /**
     * удаление слушателя
     */
    abstract void removeListener();

    /**
     * перезапуск процесса
     */
    abstract void restart();

    /**
     * обработчик подключения (регистрации на) к серверу
     */
    class TransferRegistryListener extends TransferListener {
        /**
         * конструктор
         *
         * @param activity       интерфейс
         * @param tid            идентификатор транзакции
         * @param statusCallback статус
         */
        TransferRegistryListener(Activity activity, String tid, Transfer transfer, ITransferStatusCallback statusCallback) {
            super(synchronization, activity, tid, transfer, statusCallback);
        }

        @Override
        public void call(Object... args) {
            try{
                onRestart();
                transfer.restart();
            }catch (Exception e){
                onError(e.getMessage());
            }
        }
    }

    /**
     * обработчик потери подключения к серверу
     */
    class TransferDisconnectListener extends TransferListener {
        /**
         * конструктор
         *
         * @param activity       интерфейс
         * @param tid            идентификатор транзакции
         * @param statusCallback статус
         */
        TransferDisconnectListener(Activity activity, String tid, Transfer transfer, ITransferStatusCallback statusCallback) {
            super(synchronization, activity, tid, transfer, statusCallback);
        }

        @Override
        public void call(Object... args) {
            try{
                onStop();
                transfer.removeListener();
            }catch (Exception e){
                onError(e.getMessage());
            }
        }
    }
}
