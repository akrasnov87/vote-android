package ru.mobnius.vote.data.service;

import android.content.Context;
import android.telephony.PhoneStateListener;
import ru.mobnius.vote.utils.NetworkUtil;

public class MobileDataListener extends PhoneStateListener {
    private NetworkTypeListener mListener;
    private Context mContext;

    public MobileDataListener(NetworkTypeListener networkTypeListener, Context context) {
        mListener = networkTypeListener;
        mContext = context;
    }

    public void onDataConnectionStateChanged(int state, int networkType) {
        super.onDataConnectionStateChanged(state, networkType);
        boolean connection = NetworkUtil.isConnectionFast(state, networkType, mContext);
        mListener.isFineConnection(connection);
    }

    public interface NetworkTypeListener {
        void isFineConnection(boolean isConnectionFast);
    }
}