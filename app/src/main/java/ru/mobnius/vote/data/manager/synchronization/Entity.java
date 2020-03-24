package ru.mobnius.vote.data.manager.synchronization;

import android.text.TextUtils;

import java.util.UUID;

import ru.mobnius.vote.data.manager.rpc.FilterItem;

public class Entity implements IEntity {

    /**
     * использовать функцию cf_ для получения данных
     */
    public boolean useCFunction;

    public String nameEntity;

    /**
     * Идентификатор сущности. Предназначен для работы с пакетами
     */
    public String tid;

    /**
     * имя таблицы
     */
    public final String tableName;

    /**
     * список колонок для выборки
     */
    public String select = "";

    /**
     * Передача данных на сервер
     */
    public final boolean to;

    /**
     * Получение данных от сервера
     */
    public final boolean from;
    /**
     * является справочником
     */
    protected boolean isDictionary = false;

    /**
     * Обработка завершена или нет
     */
    public boolean finished = false;

    /**
     * параметры в функцию
     */
    public Object params;

    /**
     * фильтрация
     */
    public Object[] filters;

    /**
     * Конструктор. По умолчанию указывается что разрешена отправка данных на сервер to = true
     * @param tableName имя таблицы
     */
    public Entity(String tableName){
        this(tableName, true, false);
    }

    /**
     * Конструктор
     * @param tableName имя таблицы
     * @param to разрешена отправка данных на сервер
     * @param from разрешена возможность получения данных с сервера
     */
    public Entity(String tableName, boolean to, boolean from){
        this.tableName = tableName;
        this.to = to;
        this.from = from;
        this.tid = UUID.randomUUID().toString();
        nameEntity = "Общие";
    }

    /**
     * Создание сущности
     * @param tableName имя таблицы
     * @param to разрешена отправка данных на сервер
     * @param from разрешена возможность получения информации с сервера
     * @return Возвращается сущность
     */
    public static Entity createInstance(String tableName, boolean to, boolean from){
        return new Entity(tableName, to, from);
    }

    /**
     * Уставнваливается идентификатор для сущности
     * @param tid идентификатор
     * @return возвращается текущая сущность
     */
    public Entity setTid(String tid){
        this.tid = tid;
        return this;
    }

    /**
     * обработка сущности завершена
     * @return текущая сущность
     */
    public Entity setFinished(){
        this.finished = true;
        return this;
    }

    /**
     * устновка списка колонок для выборки
     * @param select список
     * @return текущая сущность
     */
    public Entity setSelect(String ...select){
        this.select = TextUtils.join(", ", select);
        return this;
    }

    /**
     * установить параметр useCFunction
     * @return текущий объект
     */
    public Entity setUseCFunction(){
        this.useCFunction = true;
        return this;
    }

    /**
     * параметры в RPC запрос
     * @param params параметры
     * @return текущий объект
     */
    public Entity setParam(Object params){
        this.params = params;
        return this;
    }

    /**
     * устновка фильтров
     * @param filters фильтры
     * @return текущий объект
     */
    public Entity setFilters(Object ...filters){
        this.filters = filters;
        return this;
    }

    /**
     * устновка фильтра
     * @param filter фильтр
     * @return текущий объект
     */
    public Entity setFilter(FilterItem filter) {
        filters = new Object[1];
        filters[0] = filter;
        return this;
    }
}
