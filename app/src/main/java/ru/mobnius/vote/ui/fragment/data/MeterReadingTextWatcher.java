package ru.mobnius.vote.ui.fragment.data;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

public class MeterReadingTextWatcher implements IMeterReadingTextWatcher {
    private IMeterReadingTextWatcher mTextWatcher;
    private String mMeterId;
    private Double mPrevValue;

    private final String TAG = "CHANGE_METER";

    public MeterReadingTextWatcher(IMeterReadingTextWatcher textWatcher, String meterId) {
        mTextWatcher = textWatcher;
        mMeterId = meterId;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        try {
            mPrevValue = Double.parseDouble(s.toString());
        } catch (NumberFormatException ignored) {

        }
        mTextWatcher.beforeTextChanged(s, start, count, after);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mTextWatcher.onTextChanged(s, start, before, count);
    }

    @Override
    public void afterTextChanged(Editable s) {
        Double value = null;
        try {
            value = Double.parseDouble(s.toString());
        }catch (NumberFormatException ignored) {

        }
        Log.d(TAG, String.format("%s: %s->%s", mMeterId, mPrevValue, value));
        afterMeterReadingTextChanged(mMeterId, mPrevValue, value);

        mTextWatcher.afterTextChanged(s);
    }

    @Override
    public void afterMeterReadingTextChanged(String meterId, Double prevValue, Double value) {
        mTextWatcher.afterMeterReadingTextChanged(meterId, prevValue, value);
    }
}
