package ru.mobnius.vote.ui.model;

import android.location.Location;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TempImageSerializable implements Serializable {
    private List<TempImage> mTempImages;

    public TempImageSerializable() {
        mTempImages = new ArrayList<>();
    }

    public void addImage(String name, long type, Location location) {
        TempImage tempImage = new TempImage(name, "", type, location);
        mTempImages.add(tempImage);
    }

    public void removeImage(String name) {
        TempImage tempImage = null;

        for(TempImage image : mTempImages) {
            if(image.getName().equals(name)) {
                tempImage = image;
                break;
            }
        }

        mTempImages.remove(tempImage);
    }

    public List<TempImage> getTempImages() {
        return mTempImages;
    }

    public class TempImage {

        public TempImage() {

        }

        public TempImage(String name, String notice, long type, Location location) {
            mName = name;
            mNotice = notice;
            mType = type;
            mLocation = location;
        }

        private String mName;
        private String mNotice;
        private long mType;
        private Location mLocation;

        public String getName() {
            return mName;
        }

        public void setName(String name) {
            mName = name;
        }

        public String getNotice() {
            return mNotice;
        }

        public void setNotice(String notice) {
            mNotice = notice;
        }

        public long getType() {
            return mType;
        }

        public void setType(long type) {
            mType = type;
        }

        public Location getLocation() {
            return mLocation;
        }

        public void setLocation(Location location) {
            mLocation = location;
        }
    }
}
