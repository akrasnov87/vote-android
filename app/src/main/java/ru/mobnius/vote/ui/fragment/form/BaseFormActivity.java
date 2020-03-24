package ru.mobnius.vote.ui.fragment.form;

import android.annotation.SuppressLint;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;

import androidx.annotation.NonNull;

import java.util.Date;

import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.GeoManager;
import ru.mobnius.vote.data.manager.PhotoManager;
import ru.mobnius.vote.data.manager.camera.CameraManager;
import ru.mobnius.vote.data.storage.models.AttachmentTypes;
import ru.mobnius.vote.ui.activity.SingleFragmentActivity;
import ru.mobnius.vote.ui.fragment.data.GalleryUtil;
import ru.mobnius.vote.utils.BitmapUtil;

public abstract class BaseFormActivity extends SingleFragmentActivity implements GeoManager.GeoListener {
    private CameraManager mCameraManager;
    private PhotoManager mPhotoManager;
    private GeoManager mGeoManager;

    private final static String LATITUDE = "latitude";
    private final static String LONGITUDE = "longitude";
    private final static String COUNT = "count";

    private double latitude;
    private double longitude;

    /**
     * количество полученных координат
     */
    private int locations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mPhotoManager = GalleryUtil.deSerializable(getIntent());
        super.onCreate(savedInstanceState);
        mCameraManager = new CameraManager(this);
    }

    @SuppressLint("MissingPermission")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mGeoManager = new GeoManager(this, locations, latitude, longitude);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putDouble(LATITUDE, mGeoManager.getLatitude());
        outState.putDouble(LONGITUDE, mGeoManager.getLongitude());
        outState.putInt(COUNT, mGeoManager.getAccuracy());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        latitude = savedInstanceState.getDouble(LATITUDE);
        longitude = savedInstanceState.getDouble(LONGITUDE);
        locations = savedInstanceState.getInt(COUNT);
    }

    public void onCamera() {
        mCameraManager.open(BitmapUtil.IMAGE_QUALITY);
    }

    public PhotoManager getPhotoManager() {
        if(mPhotoManager == null) {
            mPhotoManager = new PhotoManager();
        }
        return mPhotoManager;
    }

    public void setPhotoManager(PhotoManager photoManager) {
        mPhotoManager = photoManager;
    }

    public CameraManager getCameraManager() {
        return mCameraManager;
    }

    /**
     * Получение текущей гео-координаты
     * @return координта
     */
    @Override
    public Location getCurrentLocation() {
        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(mGeoManager.getLatitude());
        location.setLongitude(mGeoManager.getLongitude());
        location.setTime(new Date().getTime());
        location.setAccuracy(mGeoManager.getAccuracy());

        return location;
    }

    /**
     * Получение списка типов изображений
     * @return тип изображений
     */
    public AttachmentTypes[] getAttachmentTypes() {
        return DataManager.getInstance().getAttachmentTypes();
    }

    @Override
    protected void onDestroy() {
        mGeoManager.destroy();
        super.onDestroy();
    }
}
