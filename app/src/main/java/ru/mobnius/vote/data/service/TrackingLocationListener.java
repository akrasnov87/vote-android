package ru.mobnius.vote.data.service;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import java.util.Date;

import ru.mobnius.vote.data.manager.DbOperationType;
import ru.mobnius.vote.data.storage.models.DaoSession;
import ru.mobnius.vote.data.storage.models.Tracking;
import ru.mobnius.vote.utils.DateUtil;
import ru.mobnius.vote.utils.NetworkUtil;

class TrackingLocationListener implements LocationListener {
    private final DaoSession mDaoSession;
    private final Context mContext;
    private final long mUserId;

    private static final String TRACK_ONLINE = "online";
    private static final String TRACK_OFFLINE = "offline";

    public TrackingLocationListener(DaoSession daoSession, Context context, long userId) {
        mDaoSession = daoSession;
        mContext = context;
        mUserId = userId;
    }

    @Override
    public void onLocationChanged(Location location) {
        String networkStatus;

        if(NetworkUtil.isNetworkAvailable(mContext)) {
            networkStatus = TRACK_ONLINE;
        }else {
            networkStatus = TRACK_OFFLINE;
        }

        Tracking tracking = new Tracking();
        tracking.fn_user = mUserId;
        tracking.d_date = DateUtil.convertDateToString(new Date());
        tracking.c_network_status = networkStatus;
        tracking.n_latitude = location.getLatitude();
        tracking.n_longitude = location.getLongitude();
        tracking.objectOperationType = DbOperationType.CREATED;

        mDaoSession.getTrackingDao().insert(tracking);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
