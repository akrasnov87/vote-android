package ru.mobnius.vote.data.manager;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import ru.mobnius.vote.data.manager.exception.IExceptionGroup;
import ru.mobnius.vote.data.manager.exception.IExceptionIntercept;
import ru.mobnius.vote.data.manager.exception.MyUncaughtExceptionHandler;

public abstract class BaseFragment extends Fragment implements IExceptionIntercept {

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
        return IExceptionGroup.USER_INTERFACE;
    }

    protected MobniusApplication getApplication() {
        FragmentActivity fragmentActivity = getActivity();
        if(fragmentActivity != null) {
            return (MobniusApplication) fragmentActivity.getApplication();
        }
        return null;
    }
}
