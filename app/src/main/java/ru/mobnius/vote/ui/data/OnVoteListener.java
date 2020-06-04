package ru.mobnius.vote.ui.data;

import android.location.Location;

import ru.mobnius.vote.data.manager.vote.VoteManager;

/**
 *
 */
public interface OnVoteListener {
    /**
     * Управление голосованием
     */
    VoteManager getVoteManager();

    /**
     * Идент. маршрута
     */
    String getRouteId();

    /**
     * Идент. точки маршрута
     */
    String getPointId();

    Location getLocation();
}
