package ru.mobnius.vote.ui.fragment.data;

import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.GeoManager;
import ru.mobnius.vote.data.manager.vote.VoteManager;

/**
 *
 */
public interface OnVoteListener extends GeoManager.GeoListener {
    /**
     * Управление голосованием
     * @return
     */
    VoteManager getVoteManager();

    /**
     * Управление данными
     * @return
     */
    DataManager getDataManager();

    /**
     * Идент. маршрута
     * @return
     */
    String getRouteId();

    /**
     * Идент. точки маршрута
     * @return
     */
    String getPointId();
}
