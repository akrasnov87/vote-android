package ru.mobnius.vote.ui.fragment.data;

import android.text.TextWatcher;

public interface IStringTextWatcher extends TextWatcher {

    /**
     * Обработчик изменения
     * @param id идентификатор
     * @param prevValue пред. значение
     * @param value значение
     */
    void afterStringTextChanged(String id, String prevValue, String value);
}
