package ru.mobnius.vote.data.manager.rpc;

import com.google.gson.annotations.Expose;

public class FilterItem {

    public final static String OPERATOR_LIKE = "like";
    public final static String OPERATOR_EQUAL = "=";
    public final static String OPERATOR_NOT_EQUAL = "!=";
    public final static String OPERATOR_IN = "in";
    public final static String OPERATOR_NOT_IN = "notin";
    public final static String OPERATOR_LESS = "<";
    public final static String OPERATOR_LESS_EQUAL = "<=";
    public final static String OPERATOR_MORE = ">";
    public final static String OPERATOR_MORE_EQUAL = ">=";

    @Expose
    private final String property;
    @Expose
    private final Object value;
    @Expose
    public final String operator;

    /**
     *
     * @param property наименование свойства
     * @param operator Условный оператор OPERATOR_*
     * @param value значение
     */
    public FilterItem(String property, String operator, Object value) {
        this.operator = operator == null ? OPERATOR_EQUAL : operator;
        this.property = property;
        this.value = value;

    }

    /**
     *
     * @param property наименование свойства
     * @param value значение
     */
    public FilterItem(String property, Object value) {
        this(property, null, value);
    }
}
