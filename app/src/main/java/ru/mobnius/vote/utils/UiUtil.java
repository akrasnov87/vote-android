package ru.mobnius.vote.utils;

import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;

public class UiUtil {
    /**
     * Автозамена пробела на ""
     * @param editText поля в которых нужно заменить пробел на ""
     */
    public static void setNoSpaces(EditText[] editText) {
        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int date_start, int date_end) {
                for (int i = start; i < end; i++) {
                    if (!Character.isLetterOrDigit(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };
        for (EditText et : editText) {
            et.setFilters(new InputFilter[] { filter });
        }
    }
}
