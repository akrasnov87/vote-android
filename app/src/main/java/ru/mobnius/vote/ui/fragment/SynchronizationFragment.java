package ru.mobnius.vote.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import ru.mobnius.vote.R;
import ru.mobnius.vote.data.Logger;
import ru.mobnius.vote.data.manager.BaseFragment;
import ru.mobnius.vote.data.manager.FileManager;
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
import ru.mobnius.vote.ui.fragment.template.SynchronizationPartFragment;
import ru.mobnius.vote.utils.NetworkUtil;


public class SynchronizationFragment extends BaseFragment implements View.OnClickListener {

    private Button btnStart;
    private Button btnCancel;
    private TextView tvLogs;
    private TextView tvNetworkError;
    private ViewGroup parent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    /**
     * хранилище фрагментов транспортировки
     */
    private HashMap<String, SynchronizationPartFragment> transferFragments;

    private FragmentTransaction fragmentTransaction;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_synchronization, container, false);
        parent = container;
        transferFragments = new HashMap<>();

        tvNetworkError = view.findViewById(R.id.fSynchronization_tvNetworkError);
        tvLogs = view.findViewById(R.id.fSynchronization_tvLogs);
        btnStart = view.findViewById(R.id.fSynchronization_btnStart);
        btnCancel = view.findViewById(R.id.fSynchronization_btnCancel);

        btnStart.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        return view;
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.SYNCHRONIZATION;
    }

    @Override
    public String getExceptionGroup() {
        return IExceptionGroup.SYNCHRONIZATION;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fSynchronization_btnStart:
                if (NetworkUtil.isNetworkAvailable(Objects.requireNonNull(getContext()))) {
                    availableNetwork();
                    tvLogs.setText("");
                    start();
                } else {
                    tvNetworkError.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.fSynchronization_btnCancel:
                stop();
                btnStart.setEnabled(true);
                btnStart.setTextColor(Color.BLACK);
                btnCancel.setVisibility(View.GONE);
                break;
        }
    }

    private void availableNetwork() {
        if (tvNetworkError.getVisibility() == View.VISIBLE) {
            Transition transition = new Slide(Gravity.TOP);
            transition.addTarget(tvNetworkError).setDuration(800);
            TransitionManager.beginDelayedTransition(parent, transition);
            tvNetworkError.setVisibility(View.GONE);
        }
        btnCancel.setVisibility(View.VISIBLE);
        btnStart.setEnabled(false);
        btnStart.setTextColor(getResources().getColor(R.color.gray_light));
    }

    /**
     * остановка выполнения синхронизации
     */
    void stop() {
        for (String tid : transferFragments.keySet()) {
            removeSynchronizationPart(tid);
        }
        transferFragments.clear();
        ManualSynchronization.getInstance(PreferencesManager.ZIP_CONTENT).stop();
    }

    /**
     * запуск выполнения синхронизации
     */
    private void start() {
        try {
            stop();

            final List<Boolean> success = new ArrayList<>();

            ManualSynchronization.getInstance(true).start(getActivity(), new IProgress() {
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
                            btnCancel.setVisibility(View.GONE);
                            btnStart.setEnabled(true);
                            btnStart.setTextColor(Color.BLACK);
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
                    if (synchronization.getFinishStatus() == FinishStatus.SUCCESS) {

                        try {
                            FileManager.getInstance().deleteFolder(FileManager.CACHES);
                        } catch (FileNotFoundException e) {
                            Logger.error(e);
                        }
                    }
                    transferFragments.clear();
                }

                @Override
                public void onProgress(ISynchronization synchronization, int step, String message, String tid) {

                    updateSynchronizationPartLogs(tid, message);

                    if (!message.isEmpty()) {
                        tvLogs.append(message + "\n");
                    }
                    if (synchronization.getFinishStatus() == FinishStatus.SUCCESS && step > 1) {
                        showDialog(message, Color.BLACK);
                    }

                }

                @Override
                public void onError(ISynchronization synchronization, int step, String message, String tid) {
                    tvLogs.append(Html.fromHtml("<font color='#FF0000'>" + message + "</font><br />"));
                    showDialog(message, Color.RED);
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
        fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fSynchronization_llProgress, synchronizationPartFragment);
        fragmentTransaction.commit();
        transferFragments.put(tid, synchronizationPartFragment);
    }

    private void updateSynchronizationPart(String tid, double progress, double secondProgress, TransferProgress transferProgress) {
        SynchronizationPartFragment fragment = transferFragments.get(tid);
        if (fragment != null) {
            fragment.updatePercent(progress, secondProgress);
            fragment.updateStatus(transferProgress);
        }
    }

    private void updateSynchronizationPartStatus(String tid, int type) {
        SynchronizationPartFragment fragment = transferFragments.get(tid);
        if (fragment != null) {
            fragment.updateProgressBarColor(type);
        }
    }

    private void updateSynchronizationPartLogs(String tid, String message) {
        SocketStatusReader reader = SocketStatusReader.getInstance(message);
        if (reader != null) {
            SynchronizationPartFragment fragment = transferFragments.get(tid);
            if (fragment != null) {
                fragment.updateLogs(reader.getParams()[0]);
            }
        }
    }

    /**
     * удаление фрагмента для вывода progressbar
     *
     * @param tid
     */
    private void removeSynchronizationPart(String tid) {
        fragmentTransaction = getChildFragmentManager().beginTransaction();
        SynchronizationPartFragment fragment = transferFragments.get(tid);
        if (fragment != null) {
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_synchronization, menu);
        MenuItem eye = menu.findItem(R.id.synchronizationToolbar_showLogs);
        eyeStatus(eye, PreferencesManager.getInstance().isDebug());

        eye.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                eyeStatus(item, tvLogs.getVisibility() == View.GONE);
                return true;
            }
        });
    }

    private void eyeStatus(MenuItem item, boolean visible) {
        item.setIcon(visible ? R.drawable.ic_visibility_white_24dp : R.drawable.ic_visibility_off_white_24dp);
        tvLogs.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void showDialog(String message, int color) {
        AlertDialog adb = new AlertDialog.Builder(getContext()).setMessage(message).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
        TextView messageBox = adb.findViewById(android.R.id.message);
        messageBox.setTextColor(color);
        messageBox.setTextSize(26);
    }

}
