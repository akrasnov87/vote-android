package ru.mobnius.vote.data.manager;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import ru.mobnius.vote.data.manager.exception.IExceptionGroup;
import ru.mobnius.vote.data.manager.exception.IExceptionIntercept;
import ru.mobnius.vote.data.manager.exception.MyUncaughtExceptionHandler;

/**
 * Базовый класс для DialogFragment
 */
public abstract class BaseDialogFragment extends DialogFragment
        implements IExceptionIntercept {

    public BaseDialogFragment() {
        //Set Arguments here if needed for dialog auto recreation on screen rotation
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onExceptionIntercept();
    }

    /**
     * Обработчик перехвата ошибок
     */
    public void onExceptionIntercept() {
        Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler(Thread.getDefaultUncaughtExceptionHandler(), getExceptionGroup(), getExceptionCode(), getContext()));
    }

    public String getExceptionGroup() {
        return IExceptionGroup.DIALOG;
    }
}
