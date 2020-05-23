package ru.mobnius.vote.ui.fragment.tools;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;


import ru.mobnius.vote.data.storage.models.Answer;
import ru.mobnius.vote.ui.data.OnAnswerListener;

abstract class AnswerFragmentDialog<T> extends DialogFragment {
    private OnAnswerListener mAnswerListener;
    private final Answer mAnswer;
    private final String mCommand;
    private final T mInput;
    private final boolean mIsDone;

    public T getInput() {
        return mInput;
    }

    AnswerFragmentDialog(Answer answer, String command, T input, boolean isDone) {
        mInput = input;
        mCommand = command;
        mAnswer = answer;
        mIsDone = isDone;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof OnAnswerListener) {
            mAnswerListener = (OnAnswerListener)context;
        }
    }

    void onAnswerListener(T result) {
        if(mAnswerListener != null) {
            mAnswerListener.onAnswerCommand(mCommand, mAnswer, result);
        }
    }

    boolean isDone() {
        return mIsDone;
    }
}
