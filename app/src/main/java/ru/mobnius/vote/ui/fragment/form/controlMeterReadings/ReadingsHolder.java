package ru.mobnius.vote.ui.fragment.form.controlMeterReadings;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.mobnius.vote.R;
import ru.mobnius.vote.ui.fragment.data.IMeterReadingTextWatcher;
import ru.mobnius.vote.ui.fragment.data.MeterReadingTextWatcher;
import ru.mobnius.vote.ui.model.Meter;

class ReadingsHolder extends RecyclerView.ViewHolder {
    private TextView tvType;
    private TextView tvPrevious;
    private EditText etCurrent;
    private IMeterReadingTextWatcher mTextWatcher;

    public ReadingsHolder(@NonNull View itemView, IMeterReadingTextWatcher textWatcher) {
        super(itemView);
        mTextWatcher = textWatcher;

        tvType = itemView.findViewById(R.id.itemMeterReadings_tvType);
        tvPrevious = itemView.findViewById(R.id.itemMeterReadings_tvPreviousData);
        etCurrent = itemView.findViewById(R.id.itemMeterReadings_etCurrentData);
    }

    public void bindReadings(Meter meter, Double value) {
        tvType.setText(meter.getName());
        tvPrevious.setText(meter.getPrevValueToString());
        etCurrent.setText(meter.toValueString(value));

        etCurrent.addTextChangedListener(new MeterReadingTextWatcher(mTextWatcher, meter.getInputMeterReadingsId()));
    }
}
