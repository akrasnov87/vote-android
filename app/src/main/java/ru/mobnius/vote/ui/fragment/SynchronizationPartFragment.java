package ru.mobnius.vote.ui.fragment;

import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Objects;

import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.synchronization.utils.transfer.TransferListener;
import ru.mobnius.vote.data.manager.synchronization.utils.transfer.TransferProgress;

public class SynchronizationPartFragment extends Fragment {

    public final static String DATA_TYPE = "description";

    private ProgressBar progressBar;
    private TextView tvDescription;
    private TextView tvStatus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.template_fragment_synchronization_part, container, false);

        progressBar = v.findViewById(R.id.sync_part_progress);
        tvDescription = v.findViewById(R.id.sync_part_description);
        tvStatus = v.findViewById(R.id.sync_part_status);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle arguments = getArguments();
        tvDescription.setText(Objects.requireNonNull(arguments).getString(DATA_TYPE));

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
        tvStatus.setText(progress.toTransferString());
        Bundle arguments = getArguments();
        if(arguments != null && arguments.getString(DATA_TYPE) != null) {
            String type = arguments.getString(DATA_TYPE)+ " (" + progress.getTransferData().toTransferString() + ")";
            tvDescription.setText(type);
        }
    }

    /**
     * Обновление логов
     *
     * @param logs лог
     */
    public void updateLogs(String logs) {
        Bundle arguments = getArguments();
        if(arguments != null && arguments.getString(DATA_TYPE) != null) {
            String type = arguments.getString(DATA_TYPE) + " " + logs;
            tvDescription.setText(type);
        }
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
                colorStateList = getResources().getColorStateList(R.color.colorSecondary);
                break;

            default:
                colorStateList = getResources().getColorStateList(R.color.colorFloating);
                break;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            progressBar.setSecondaryProgressTintList(colorStateList);
            progressBar.setProgressTintList(colorStateList);
        }
    }
}
