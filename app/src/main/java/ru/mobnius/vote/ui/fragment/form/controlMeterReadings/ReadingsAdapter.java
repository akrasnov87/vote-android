package ru.mobnius.vote.ui.fragment.form.controlMeterReadings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.mobnius.vote.R;
import ru.mobnius.vote.ui.fragment.data.DocumentUtil;
import ru.mobnius.vote.ui.fragment.data.IMeterReadingTextWatcher;
import ru.mobnius.vote.ui.model.Meter;

class ReadingsAdapter extends RecyclerView.Adapter<ReadingsHolder> {
    private IMeterReadingTextWatcher mTextWatcher;
    private DocumentUtil.MeterReading[] mValues;
    private Context mContext;
    private Meter[] mServerMeters;

    public ReadingsAdapter(Context context, Meter[] serverMeters, IMeterReadingTextWatcher textWatcher, DocumentUtil.MeterReading[] meters) {
        mContext = context;
        mServerMeters = serverMeters;
        mTextWatcher = textWatcher;
        mValues = meters;
    }

    @NonNull
    @Override
    public ReadingsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_meter_readings, parent, false);
        return new ReadingsHolder(view, mTextWatcher);
    }

    @Override
    public void onBindViewHolder(@NonNull ReadingsHolder holder, int position) {
        Meter meter = mServerMeters[position];
        holder.bindReadings(meter, mValues[position].getValue());
    }

    @Override
    public int getItemCount() {
        return mServerMeters.length;
    }
}
