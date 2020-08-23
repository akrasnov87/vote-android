package ru.mobnius.vote.data.service;

import java.util.TimerTask;

import ru.mobnius.vote.data.manager.configuration.PreferencesManager;
import ru.mobnius.vote.data.manager.synchronization.IProgress;
import ru.mobnius.vote.data.manager.synchronization.ISynchronization;
import ru.mobnius.vote.data.manager.synchronization.LiteSynchronization;
import ru.mobnius.vote.data.manager.synchronization.ServiceSynchronization;
import ru.mobnius.vote.data.manager.synchronization.utils.transfer.Transfer;
import ru.mobnius.vote.data.manager.synchronization.utils.transfer.TransferProgress;

class AutoSyncTimerTask extends TimerTask {
    @Override
    public void run() {
        if(PreferencesManager.getInstance().isAutoSync()) {
            LiteSynchronization liteSynchronization = LiteSynchronization.getInstance(PreferencesManager.ZIP_CONTENT);
            liteSynchronization.start(null, new IProgress() {
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
