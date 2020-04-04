package ru.mobnius.vote.data.manager.synchronization.utils.transfer;

import android.app.Activity;
import android.util.Log;

import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.Date;

import ru.mobnius.vote.data.Logger;
import ru.mobnius.vote.data.manager.synchronization.ISynchronization;

public class UploadTransfer extends Transfer {

    /**
     * слушатель загрузки данных на сервер
     */
    UploadTransfer.UploadListener uploadListener;

    /**
     * массив байтов который требуется передать на сервер
     */
    private byte[] uploadBytes;

    /**
     * текущая позиция при загрузки на сервер
     */
    private int uploadPosition = 0;
    private Date dtStart;

    public UploadTransfer(ISynchronization synchronization, Socket socket, String version, Activity context, String tid) {
        super(synchronization, socket, version, context, tid);
    }

    /**
     * загрузка на сервер информации
     * @param bytes массив данных
     * @param callback результат
     */
    public void upload(byte[] bytes, final ITransferStatusCallback callback){
        dtStart = new Date();

        this.callback = callback;
        disconnectListener();

        Log.d(UPLOAD_TAG, "Старт иден. " + tid);
        uploadListener = new UploadListener(synchronization, context, tid, this, callback);
        uploadListener.onStart();
        socket.on(EVENT_UPLOAD, uploadListener);

        uploadBytes = bytes;
        Log.d(UPLOAD_TAG, "tid: "+tid+"; start: " + uploadPosition + "; chunk: " + getChunk() + "; totalLength: " + bytes.length);
        socket.emit(EVENT_UPLOAD, protocolVersion, Arrays.copyOfRange(bytes, uploadPosition, getChunk()), tid, uploadPosition, bytes.length);
    }

    /**
     * перезапуск процесса
     */
    public void restart(){
        uploadListener = new UploadTransfer.UploadListener(synchronization, context, tid, this, callback);
        socket.on(EVENT_UPLOAD, uploadListener);

        int end = uploadPosition + getChunk();
        if (end > uploadBytes.length) {
            end = uploadBytes.length;
        }

        socket.emit(EVENT_UPLOAD, protocolVersion, Arrays.copyOfRange(uploadBytes, uploadPosition, end), tid, uploadPosition, uploadBytes.length);
        Log.d(UPLOAD_TAG, "Запуск после восстановления "+ EVENT_UPLOAD +": " + uploadPosition);
    }

    /**
     * Удаление слушателя
     */
    public void removeListener(){
        if(socket != null){
            if(uploadListener != null) {
                socket.off(EVENT_UPLOAD, uploadListener);
                uploadListener = null;

                Log.d(UPLOAD_TAG, "Удаляем обработчик " + EVENT_UPLOAD);
            }
        }
    }

    @Override
    public void destroy() {
        super.destroy();

        uploadBytes = null;
        uploadPosition = 0;
        dtStart = null;
    }

    class UploadListener extends TransferListener {

        /**
         * конструктор
         * @param synchronization синхронизация
         * @param activity        интерфейс
         * @param tid             идентификатор транзакции
         * @param instance        текущий экземпляр передачи данных
         * @param statusCallback  статус
         */
        public UploadListener(ISynchronization synchronization, Activity activity, String tid, UploadTransfer instance, ITransferStatusCallback statusCallback) {
            super(synchronization, activity, tid, instance, statusCallback);
        }

        @Override
        public void call(final Object... args) {
            if(synchronization.getEntities(tid).length > 0) {
                try {
                    processing(args);
                } catch (Exception e) {
                    onError(e.getMessage());
                }
            }
        }

        /**
         * обработка результат
         * @param args параметры
         */
        void processing(Object[] args) {
            final boolean valid = args != null && args[0] instanceof JSONObject;

            if (!valid) {
                onError("Переданный объект не является JSONObject");
                return;
            }

            JSONObject jsonObject = (JSONObject) args[0];
            TransferResult result = TransferResult.readResult(jsonObject);

            if (result.tid.equals(tid)) {
                if (!result.data.success) {
                    onError(result.data.msg);
                } else {
                    if (result.meta.processed) {
                        onEnd(uploadBytes);
                        transfer.destroy();
                        Log.d(UPLOAD_TAG, "tid: " + tid + "; finish");
                    } else {
                        uploadPosition = result.meta.start;
                        int percent = (uploadPosition * 100) / uploadBytes.length;
                        int end = uploadPosition + getChunk();
                        if (end > uploadBytes.length) {
                            end = uploadBytes.length;
                        }
                        try {
                            Log.d(UPLOAD_TAG, "tid: " + tid + "; start: " + end + "; chunk: " + getChunk() + "; totalLength: " + uploadBytes.length);
                            socket.emit(EVENT_UPLOAD, protocolVersion, Arrays.copyOfRange(uploadBytes, uploadPosition, end), tid, uploadPosition, uploadBytes.length);
                        } catch (Exception e) {
                            onError(e.getMessage());
                        }
                        long lastChunk = getChunk();
                        if (getIterationStartTime() != null) {
                            // время которое потребовалось для передачи CHUNK блока
                            long time = new Date().getTime() - getIterationStartTime().getTime();
                            // сколько нужно блоков для передачи за 1 секунду?
                            updateChunk((INTERVAL * getChunk()) / (time == 0 ? 1 : time));
                            onPercent(percent,
                                    TransferSpeed.getInstance(lastChunk, new Date().getTime() - getIterationStartTime().getTime()),
                                    getLastTime(dtStart, percent),
                                    TransferData.getInstance(uploadPosition, uploadBytes.length));
                        } else {
                            onPercent(percent,
                                    TransferSpeed.getInstance(uploadPosition, new Date().getTime() - dtStart.getTime()),
                                    getLastTime(dtStart, percent),
                                    TransferData.getInstance(uploadPosition, uploadBytes.length));
                        }
                    }
                }
            }
        }
    }
}
