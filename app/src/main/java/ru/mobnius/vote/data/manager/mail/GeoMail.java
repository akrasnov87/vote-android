package ru.mobnius.vote.data.manager.mail;

import com.google.gson.annotations.Expose;

import ru.mobnius.vote.data.manager.authorization.Authorization;
import ru.mobnius.vote.data.storage.models.Tracking;

public class GeoMail extends BaseMail {

    public GeoMail(double latitude, double longitude, String date) {
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
    public double mLatitude;
    @Expose
    public double mLongitude;
    @Expose
    public String mDate;
}
