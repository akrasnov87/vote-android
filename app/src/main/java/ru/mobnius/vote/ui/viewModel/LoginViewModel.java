package ru.mobnius.vote.ui.viewModel;


import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;

import ru.mobnius.vote.data.BaseViewModel;
import ru.mobnius.vote.data.manager.Version;
import ru.mobnius.vote.ui.model.LoginModel;
import ru.mobnius.vote.utils.DateUtil;

public class LoginViewModel extends BaseViewModel<LoginModel> {

    private final int MIN_LENGTH = 3;

    public LoginViewModel() {
    }

    /**
     * Вывод сообщения о версии приложения
     *
     * @param template    шаблон строки
     * @param versionName Версия
     * @param status      статус приложения
     * @return строка
     */
    public String getVersionToast(String template, String versionName, String status) {
        if (isValidTemplate(template)) {
            Version mVersion = new Version();
            int[] parts = mVersion.getVersionParts(versionName);
            String userDateString = DateUtil.convertDateToUserString(mVersion.getBuildDate(Version.BIRTH_DAY, versionName));

            return String.format(template, parts[0], parts[1], userDateString, status);
        }

        return null;
    }

    /**
     * Проверка шаблона сообщения
     *
     * @param template шаблон
     * @return
     */
    boolean isValidTemplate(String template) {
        int count = -1;
        int i = -1;
        do {
            i++;
            count++;
            i = template.indexOf("%s", i);
        } while (i >= 0);

        return count == 4;
    }

    /**
     * Проверка на минимальную длину
     *
     * @param field содержимое поля
     * @return строка с результатом. Если String.Empty, то все хорошо.
     */
    public String minLength(String field) {
        if (field.length() >= MIN_LENGTH) {
            return "";
        } else {
            return "Минимальная длина поля %s должна быть " + MIN_LENGTH + " символа (ов).";
        }
    }

    /**
     * Проверка на пустое поле
     *
     * @param login    содержимое поля логин
     * @param password содержимое поля пароль
     * @return нужно ли активировать кнопку.
     */
    public boolean isButtonEnable(String login, String password) {
        if (login.length() >= MIN_LENGTH && password.length() >= MIN_LENGTH) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * Автозамена пробела на ""
     * @param editText поля в которых нужно заменить пробел на ""
     */
    public void setNoSpaces(EditText[] editText) {
        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (!Character.isLetterOrDigit(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };
        for (EditText et : editText) {
            et.setFilters(new InputFilter[]{filter});
        }
    }
}
