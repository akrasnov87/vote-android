package ru.mobnius.vote.ui.fragment.tools;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


import java.util.ArrayList;
import java.util.HashMap;

import ru.mobnius.vote.data.storage.models.Answer;
import ru.mobnius.vote.ui.fragment.data.OnAnswerListener;

public abstract class AnswerFragmentDialog<T> extends DialogFragment {
    private OnAnswerListener mAnswerListener;
    private Answer mAnswer;
    private String mCommand;
    private T mInput;

    public T getInput() {
        return mInput;
    }

    public AnswerFragmentDialog(Answer answer, String command, T input) {
        mInput = input;
        mCommand = command;
        mAnswer = answer;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof OnAnswerListener) {
            mAnswerListener = (OnAnswerListener)context;
        }
    }

    protected void onAnswerListener(T result) {
        if(mAnswerListener != null) {
            mAnswerListener.onAnswerCommand(mCommand, mAnswer, result);
        }
    }
}
