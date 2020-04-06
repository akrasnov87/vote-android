package ru.mobnius.vote.ui.fragment.additional;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


import java.util.ArrayList;
import java.util.HashMap;


public abstract class BaseAdditionalInfoDialog extends DialogFragment{


    private IAdditionalInfoCallback mInfoCallback;
    private boolean isUpdate;

    public BaseAdditionalInfoDialog(boolean isUpdate){
        this.isUpdate = isUpdate;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof IAdditionalInfoCallback){
            mInfoCallback = (IAdditionalInfoCallback) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    IAdditionalInfoCallback getInfoCallback(){
        return mInfoCallback;
    }

    /**
     * интерфейс для получения введенных на диалоговых формах данных
     */
    public interface IAdditionalInfoCallback{
        void OnCommentFinish(String comment, boolean isFinish);

        void OnContactFinish(ArrayList<HashMap<String, String>> contacts);
    }
}
