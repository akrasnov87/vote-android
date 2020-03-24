package ru.mobnius.vote.ui.model;

import java.util.Date;

import ru.mobnius.vote.data.manager.DataManager;

public class RouteItem {
    /**
     * идентификатор
     */
    public String id;
    /**
     * примечание
     */
    public String notice;
    /**
     * номер маршрута
     */
    public String number;
    /**
     * дата создания
     */
    public Date date;
    /**
     * дата начала выполения
     */
    public Date dateStart;
    /**
     * Дата завершения маршрута
     */
    public Date dateEnd;
    /**
     * был ли продлен маршрут
     */
    public boolean isExtended;
    /**
     * дата до которой продлен маршрут
     */
    public Date extended;
    /**
     * тип маршрута. идентификатор
     */
    public long typeId;
    /**
     * тип маршрута. наименование
     */
    public String typeName;
    /**
     * Статус маршрута. идентификтаор
     */
    public long statusId;
    /**
     * Статус маршрута. Наименование.
     */
    public String statusName;
    /**
     * Сортировка
     */
    public int order;

    public boolean isContains() {
        return DataManager.getInstance().isWait(id);
    }
}
