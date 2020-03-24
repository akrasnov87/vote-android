package ru.mobnius.vote.ui.fragment.data;

import android.text.TextWatcher;

public interface IMeterReadingTextWatcher extends TextWatcher {

    /**
     * Обработчик изменения показания
     * @param meterId идентификатор показания
     * @param prevValue пред. значение показания
     * @param value значение показания
     */
    void afterMeterReadingTextChanged(String meterId, Double prevValue, Double value);
}
