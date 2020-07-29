package ru.mobnius.vote.data.manager;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import ru.mobnius.vote.data.service.MobileDataListener;
import ru.mobnius.vote.utils.NetworkUtil;
import static android.net.ConnectivityManager.TYPE_MOBILE;


public class ConnectionStateManager extends BroadcastReceiver implements MobileDataListener.NetworkTypeListener {

    private Context mContext;
    private ConnectionCallback mConnectionCallback;
    private MobileDataListener mMobileDataListener;


    public void listenConnection(ConnectionCallback connectionCallback) {
        mConnectionCallback = connectionCallback;
        TelephonyManager mTelephonyManager = (TelephonyManager)
                mContext.getSystemService(Context.TELEPHONY_SERVICE);
        int networkType = mTelephonyManager.getNetworkType();
        mMobileDataListener.onDataConnectionStateChanged(TYPE_MOBILE, networkType);
    }

    public void setContext(Context context){
        mContext = context;
        if (mMobileDataListener == null) {
            mMobileDataListener = new MobileDataListener(this, context);
            TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            TelephonyMgr.listen(mMobileDataListener,
                    PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);
        }
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        if (NetworkUtil.isNetworkAvailable(context) && NetworkUtil.isWifiConnection(context)){
            mConnectionCallback.onConnectionChange(true);
        } else {
            TelephonyManager mTelephonyManager = (TelephonyManager)
                    context.getSystemService(Context.TELEPHONY_SERVICE);
            int networkType = mTelephonyManager.getNetworkType();
            mMobileDataListener.onDataConnectionStateChanged(TYPE_MOBILE, networkType);
        }

    }

    @Override
    public void isFineConnection(boolean isConnectionFast) {
        if (mConnectionCallback !=null){
            if(NetworkUtil.isNetworkAvailable(mContext) && NetworkUtil.isWifiConnection(mContext)){
                isConnectionFast = true;
            }
            mConnectionCallback.onConnectionChange(isConnectionFast);
        }
    }

    public interface ConnectionCallback {
        void onConnectionChange(boolean isConnected);
    }
}