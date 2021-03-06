package ru.mobnius.vote.ui.model;

import java.util.Date;

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
    /**
     * кол-во заданий
     */
    public int count;
}
