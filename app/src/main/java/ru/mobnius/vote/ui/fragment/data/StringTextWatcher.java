package ru.mobnius.vote.ui.fragment.data;

import android.text.Editable;
import android.util.Log;

public class StringTextWatcher implements IStringTextWatcher {
    private IStringTextWatcher mTextWatcher;
    private String mId;
    private String mPrevValue;

    private final String TAG = "CHANGE_STRING";

    public StringTextWatcher(IStringTextWatcher textWatcher, String id) {
        mTextWatcher = textWatcher;
        mId = id;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        mPrevValue = s.toString();
        mTextWatcher.beforeTextChanged(s, start, count, after);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mTextWatcher.onTextChanged(s, start, before, count);
    }

    @Override
    public void afterTextChanged(Editable s) {
        String value = s.toString();
        Log.d(TAG, String.format("%s: %s->%s", mId, mPrevValue, value));
        afterStringTextChanged(mId, mPrevValue, value);

        mTextWatcher.afterTextChanged(s);
    }

    @Override
    public void afterStringTextChanged(String id, String prevValue, String value) {
        mTextWatcher.afterStringTextChanged(id, prevValue, value);
    }
}
