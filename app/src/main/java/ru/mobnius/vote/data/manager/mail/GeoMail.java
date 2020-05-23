package ru.mobnius.vote.data.manager.mail;

import com.google.gson.annotations.Expose;

import ru.mobnius.vote.data.storage.models.Tracking;

public class GeoMail extends BaseMail {

    private GeoMail(double latitude, double longitude, String date) {
        super();
        mGroup = "manager";

        mDate = date;
        mLatitude = latitude;
        mLongitude = longitude;
    }

    public GeoMail(Tracking tracking) {
        this(tracking.n_latitude, tracking.n_longitude, tracking.d_date);
    }

    @Expose
    private final double mLatitude;
    @Expose
    private final double mLongitude;
    @Expose
    private final String mDate;
}
