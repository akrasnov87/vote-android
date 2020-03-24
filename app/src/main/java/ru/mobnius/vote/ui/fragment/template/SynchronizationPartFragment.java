package ru.mobnius.vote.ui.fragment.template;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.synchronization.utils.transfer.TransferListener;
import ru.mobnius.vote.data.manager.synchronization.utils.transfer.TransferProgress;

public class SynchronizationPartFragment extends Fragment {

    public final static String DATA_TYPE = "description";

    ProgressBar progressBar;
    TextView tvDescription;
    TextView tvStatus;

    public SynchronizationPartFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.template_fragment_synchronization_part, container, false);
        progressBar = v.findViewById(R.id.fSynchronizationPart_pbProgress);
        tvDescription = v.findViewById(R.id.fSynchronizationPart_tvDescription);
        tvStatus = v.findViewById(R.id.frSynchronizationPart_tvStatus);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle arguments = getArguments();
        tvDescription.setText(arguments.getString(DATA_TYPE));

        updateProgressBarColor(TransferListener.START);
    }

    /**
     * обновление процента выполнения
     *
     * @param percent       процент
     * @param secondPercent процент
     */
    public void updatePercent(double percent, double secondPercent) {
        progressBar.setSecondaryProgress((int) percent);
        progressBar.setProgress((int) secondPercent);
    }

    /**
     * обновление статуса
     *
     * @param progress прогресс
     */
    public void updateStatus(TransferProgress progress) {
        tvStatus.setText(progress.toString());
        Bundle arguments = getArguments();
        tvDescription.setText(arguments.getString(DATA_TYPE) + " (" + progress.getTransferData().toString() + ")");
    }

    /**
     * Обновление логов
     *
     * @param logs лог
     */
    public void updateLogs(String logs) {
        Bundle arguments = getArguments();
        tvDescription.setText(arguments.getString(DATA_TYPE) + " " + logs);
    }

    /**
     * обновление цвета полосы
     *
     * @param type тип статуса TransferListener
     */
    public void updateProgressBarColor(int type) {
        ColorStateList colorStateList;
        switch (type) {
            case TransferListener.STOP:
            case TransferListener.ERROR:
                colorStateList = getResources().getColorStateList(R.color.colorFail);
                break;

            default:
                colorStateList = getResources().getColorStateList(R.color.colorSuccess);
                break;
        }
        progressBar.setSecondaryProgressTintList(colorStateList);
        progressBar.setProgressTintList(colorStateList);
    }
}
