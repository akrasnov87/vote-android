package ru.mobnius.vote.utils;

import android.location.Location;

import java.util.Date;

class LocationUtil {
    /**
     * Создание объекта Location
     * @param n_longitude долгота
     * @param n_latitude широта
     * @return объект Location
     */
    public static Location getLocation(double n_longitude, double n_latitude) {
        Location location = new Location("CODE");
        location.setLatitude(n_latitude);
        location.setLongitude(n_longitude);
        location.setTime(new Date().getTime());
        return location;
    }

    /**
     * Создание объекта Location
     * @param n_longitude долгота
     * @param n_latitude широта
     * @param date дата
     * @return объект Location
     */
    public static Location getLocation(double n_longitude, double n_latitude, Date date) {
        Location location = new Location("CODE");
        location.setLatitude(n_latitude);
        location.setLongitude(n_longitude);
        location.setTime(date.getTime());
        return location;
    }
}
