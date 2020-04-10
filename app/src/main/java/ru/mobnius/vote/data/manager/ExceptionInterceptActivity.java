package ru.mobnius.vote.data.manager;


import androidx.appcompat.app.AppCompatActivity;

import ru.mobnius.vote.data.manager.exception.IExceptionGroup;
import ru.mobnius.vote.data.manager.exception.IExceptionIntercept;
import ru.mobnius.vote.data.manager.exception.MyUncaughtExceptionHandler;

/**
 * абстрактный класс для реализации обработчиков ошибок
 */
public abstract class ExceptionInterceptActivity extends AppCompatActivity implements IExceptionIntercept {

    public void onExceptionIntercept() {
        Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler(Thread.getDefaultUncaughtExceptionHandler(), getExceptionGroup(), getExceptionCode(), this));
    }

    @Override
    public String getExceptionGroup() {
        return IExceptionGroup.USER_INTERFACE;
    }

    /**
     * Числовой код ошибки из IExceptionCode
     * @return строка
     */
    public abstract int getExceptionCode();
}
