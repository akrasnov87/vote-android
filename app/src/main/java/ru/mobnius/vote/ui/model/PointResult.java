package ru.mobnius.vote.ui.model;

/**
 * Результат точки маршрута
 */
public class PointResult {

    /**
     * Создание экземпляра
     * @param resultTypeId идентификатор типа документа
     * @param name Наименование документа
     * @param order Сортировка документа
     * @param resultId Иденттификатор результат, если документ уже был создан
     * @param isLock заблокирован
     * @return Результат точки маршрута
     */
    public static PointResult getInstance(Long resultTypeId, String name, int order, String resultId, boolean isLock) {
        PointResult pointResult = new PointResult();
        pointResult.mIsExists = resultId != null;
        pointResult.mName = name;
        pointResult.mOrder = order;
        pointResult.mResultTypeId = resultTypeId;
        pointResult.mResultId = resultId;
        pointResult.mIsLock = isLock;

        return pointResult;
    }

    /**
     * Заблокирован документ или нет
     */
    private boolean mIsLock;

    /**
     * Идентификатор результата. см. объект ResultTypes.id
     */
    private Long mResultTypeId;

    /**
     * Наименование документа. см. ResultTypes.c_name
     */
    private String mName;

    /**
     * Параметр сортировки. см. ResultTypes.n_order
     */
    private int mOrder;

    /**
     * Был ли выполнен ранее этот документ
     */
    private boolean mIsExists;

    /**
     * Идентификатор результата если он был выполнен ранее. см. Results.id
     */
    private String mResultId;

    public Long getResultTypeId() {
        return mResultTypeId;
    }

    public String getName() {
        return mName;
    }

    public int getOrder() {
        return mOrder;
    }

    public boolean isExists() {
        return mIsExists;
    }

    public String getResultId() {
        return mResultId;
    }

    public boolean isLock() {
        return mIsLock;
    }
}
