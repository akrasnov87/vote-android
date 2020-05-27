package ru.mobnius.vote.ui.data;

import ru.mobnius.vote.data.manager.GeoManager;
import ru.mobnius.vote.data.manager.vote.VoteManager;

/**
 *
 */
public interface OnVoteListener extends GeoManager.GeoListener {
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
}
