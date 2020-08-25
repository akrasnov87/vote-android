package ru.mobnius.vote.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ru.mobnius.vote.R;
import ru.mobnius.vote.data.Logger;
import ru.mobnius.vote.data.manager.BaseActivity;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.MobniusApplication;
import ru.mobnius.vote.data.manager.OnNetworkChangeListener;
import ru.mobnius.vote.data.manager.configuration.PreferencesManager;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.data.manager.exception.IExceptionGroup;
import ru.mobnius.vote.data.manager.synchronization.Entity;
import ru.mobnius.vote.data.manager.synchronization.FinishStatus;
import ru.mobnius.vote.data.manager.synchronization.IProgress;
import ru.mobnius.vote.data.manager.synchronization.ISynchronization;
import ru.mobnius.vote.data.manager.synchronization.ManualSynchronization;
import ru.mobnius.vote.data.manager.synchronization.utils.SocketStatusReader;
import ru.mobnius.vote.data.manager.synchronization.utils.transfer.DownloadTransfer;
import ru.mobnius.vote.data.manager.synchronization.utils.transfer.Transfer;
import ru.mobnius.vote.data.manager.synchronization.utils.transfer.TransferListener;
import ru.mobnius.vote.data.manager.synchronization.utils.transfer.TransferProgress;
import ru.mobnius.vote.data.manager.synchronization.utils.transfer.UploadTransfer;
import ru.mobnius.vote.data.storage.models.Points;
import ru.mobnius.vote.data.storage.models.Results;
import ru.mobnius.vote.ui.fragment.SynchronizationPartFragment;
import ru.mobnius.vote.ui.model.PointState;
import ru.mobnius.vote.utils.AuditUtils;
import ru.mobnius.vote.utils.NetworkUtil;

public class SynchronizationActivity extends BaseActivity
        implements View.OnClickListener, OnNetworkChangeListener {

    public static Intent getIntent(Context context) {
        return new Intent(context, SynchronizationActivity.class);
    }

    /**
     * хранилище фрагментов транспортировки
     */
    private HashMap<String, SynchronizationPartFragment> mTransferFragments;
    private FragmentTransaction mFragmentTransaction;

    private Button btnStart;
    private Button btnStop;
    private TextView tvLogs;
    private TextView tvError;
    private Button btnSyncAppartament;

    private LocaleDataAsyncTask mLocaleDataAsyncTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synchronization);

        mTransferFragments = new HashMap<>();

        tvError = findViewById(R.id.sync_error);
        tvLogs = findViewById(R.id.sync_logs);
        btnStart = findViewById(R.id.sync_start);
        btnStop = findViewById(R.id.sync_stop);
        btnSyncAppartament = findViewById(R.id.sync_appartament);

        btnStart.setOnClickListener(this);
        btnStart.setEnabled(false);
        btnStop.setOnClickListener(this);
        btnSyncAppartament.setOnClickListener(this);

        mLocaleDataAsyncTask = new LocaleDataAsyncTask();
        mLocaleDataAsyncTask.execute();
    }

    @Override
    public String getHelpKey() {
        return "sync";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_synchronization, menu);
        MenuItem eye = menu.findItem(R.id.action_sync_log);
        if (PreferencesManager.getInstance().isDebug()) {
            eye.setVisible(true);
            eyeStatus(eye, PreferencesManager.getInstance().isDebug());
        } else {
            eye.setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_sync_log) {
            eyeStatus(item, tvLogs.getVisibility() == View.GONE);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.SYNCHRONIZATION;
    }

    @Override
    public String getExceptionGroup() {
        return IExceptionGroup.SYNCHRONIZATION;
    }

    private void eyeStatus(MenuItem item, boolean visible) {
        item.setIcon(visible ? R.drawable.ic_visibility_white_24dp : R.drawable.ic_visibility_off_white_24dp);
        tvLogs.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ((MobniusApplication)getApplication()).addNetworkChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        ManualSynchronization.getInstance(true).stop();
        ((MobniusApplication)getApplication()).removeNetworkChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.sync_start:
            case R.id.sync_appartament:
                if (NetworkUtil.isNetworkAvailable(this) && NetworkUtil.isConnectionFast(this)) {
                    if (v.getId() == R.id.sync_appartament) {
                        v.setVisibility(View.GONE);
                    }
                    AuditUtils.write("Синхронизация в online", AuditUtils.SYNC, AuditUtils.Level.HIGH);
                    availableNetwork();
                    tvLogs.setText("");
                    start();
                } else {
                    AuditUtils.write("Синхронизация в offline", AuditUtils.SYNC, AuditUtils.Level.HIGH);
                    tvError.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.sync_stop:
                AuditUtils.write("Принудительное завершение синхронизации", AuditUtils.SYNC, AuditUtils.Level.HIGH);
                stop();
                btnStart.setEnabled(true);
                btnStop.setVisibility(View.GONE);
                btnSyncAppartament.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * остановка выполнения синхронизации
     */
    private void stop() {
        for (String tid : mTransferFragments.keySet()) {
            removeSynchronizationPart(tid);
        }
        mTransferFragments.clear();
        ManualSynchronization.getInstance(PreferencesManager.ZIP_CONTENT).stop();
    }

    /**
     * запуск выполнения синхронизации
     */
    private void start() {
        try {
            stop();

            final List<Boolean> success = new ArrayList<>();

            ManualSynchronization.getInstance(true).start(this, new IProgress() {
                @Override
                public void onStartTransfer(String tid, Transfer transfer) {
                    if (transfer instanceof UploadTransfer) {
                        addSynchronizationPart(tid);
                        success.add(true);
                    }
                }

                @Override
                public void onRestartTransfer(String tid, Transfer transfer) {
                    updateSynchronizationPartStatus(tid, TransferListener.RESTART);
                }

                @Override
                public void onPercentTransfer(String tid, TransferProgress progress, Transfer transfer) {
                    if (transfer instanceof UploadTransfer) {
                        updateSynchronizationPart(tid, progress.getPercent(), 0, progress);
                    }

                    if (transfer instanceof DownloadTransfer) {
                        updateSynchronizationPart(tid, 100, progress.getPercent(), progress);
                    }
                }

                @Override
                public void onStopTransfer(String tid, Transfer transfer) {
                    updateSynchronizationPartStatus(tid, TransferListener.STOP);
                }

                @Override
                public void onEndTransfer(String tid, Transfer transfer, Object data) {
                    if (transfer instanceof DownloadTransfer) {
                        removeSynchronizationPart(tid);
                        success.remove(0);

                        if (success.size() == 0) {
                            btnStop.setVisibility(View.GONE);
                            btnStart.setEnabled(true);
                        }
                        tvLogs.setMovementMethod(ScrollingMovementMethod.getInstance());
                    }
                }

                @Override
                public void onErrorTransfer(String tid, String message, Transfer transfer) {
                    updateSynchronizationPartStatus(tid, TransferListener.ERROR);
                }

                @Override
                public void onStart(ISynchronization synchronization) {

                }

                @Override
                public void onStop(ISynchronization synchronization) {
                    mTransferFragments.clear();
                }

                @Override
                public void onProgress(ISynchronization synchronization, int step, String message, String tid) {

                    updateSynchronizationPartLogs(tid, message);

                    if (!message.isEmpty()) {
                        tvLogs.append(message + "\n");
                    }
                    if (synchronization.getFinishStatus() == FinishStatus.SUCCESS && step > 1) {
                        alert(message);
                    }
                }

                @Override
                public void onError(ISynchronization synchronization, int step, String message, String tid) {
                    tvLogs.append(Html.fromHtml("<font color='#FF0000'>" + message + "</font><br />"));
                    AuditUtils.write(message, AuditUtils.SYNC_ERROR, AuditUtils.Level.HIGH);
                    alert(message);
                }
            });
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    private void addSynchronizationPart(String tid) {

        Entity[] entities = ManualSynchronization.getInstance(true).getEntities(tid);
        String name;
        if (entities.length > 0) {
            name = entities[0].nameEntity;
        } else {
            name = "Неизвестно";
        }

        SynchronizationPartFragment synchronizationPartFragment = new SynchronizationPartFragment();

        Bundle bundle = new Bundle();
        bundle.putString(SynchronizationPartFragment.DATA_TYPE, name);
        synchronizationPartFragment.setArguments(bundle);
        mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        mFragmentTransaction.add(R.id.sync_progress, synchronizationPartFragment);
        mFragmentTransaction.commit();
        mTransferFragments.put(tid, synchronizationPartFragment);
    }

    private void updateSynchronizationPart(String tid, double progress, double secondProgress, TransferProgress transferProgress) {
        SynchronizationPartFragment fragment = mTransferFragments.get(tid);
        if (fragment != null) {
            fragment.updatePercent(progress, secondProgress);
            fragment.updateStatus(transferProgress);
        }
    }

    private void updateSynchronizationPartStatus(String tid, int type) {
        SynchronizationPartFragment fragment = mTransferFragments.get(tid);
        if (fragment != null) {
            fragment.updateProgressBarColor(type);
        }
    }

    private void updateSynchronizationPartLogs(String tid, String message) {
        SocketStatusReader reader = SocketStatusReader.getInstance(message);
        if (reader != null) {
            SynchronizationPartFragment fragment = mTransferFragments.get(tid);
            if (fragment != null) {
                fragment.updateLogs(reader.getParams()[0]);
            }
        }
    }

    /**
     * удаление фрагмента для вывода progressbar
     */
    private void removeSynchronizationPart(String tid) {
        mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        SynchronizationPartFragment fragment = mTransferFragments.get(tid);
        if (fragment != null) {
            mFragmentTransaction.remove(fragment);
            mFragmentTransaction.commit();
        }
    }

    private void availableNetwork() {
        if (tvError.getVisibility() == View.VISIBLE) {
            tvError.setVisibility(View.GONE);
        }
        btnStop.setVisibility(View.VISIBLE);
        btnStart.setEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mLocaleDataAsyncTask.cancel(true);
        mLocaleDataAsyncTask = null;
    }

    @Override
    public void onNetworkChange(boolean online, boolean serverExists, boolean isFast) {
        if (isFast) {
            tvError.setVisibility(View.GONE);
            btnStart.setEnabled(true);
        } else {
            tvError.setVisibility(View.VISIBLE);
            tvError.setText(R.string.internet_is_slow_sync);
            btnStart.setEnabled(false);
        }
    }

    @SuppressLint("StaticFieldLeak")
    class LocaleDataAsyncTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            int pointCount = 0;
            List<Results> results = DataManager.getInstance().getDaoSession().getResultsDao().loadAll();
            for (Results result : results) {
                if (!result.isSynchronization) {
                    pointCount++;
                }
            }
            return pointCount;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            if (integer > 0) {
                btnSyncAppartament.setVisibility(View.VISIBLE);
                btnStart.setEnabled(false);
                btnSyncAppartament.setText(String.format("Сохранить %s квартир на сервере", integer));
            } else {
                btnStart.setEnabled(true);
                btnSyncAppartament.setVisibility(View.GONE);
            }
        }
    }
}
