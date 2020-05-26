package ru.mobnius.vote.ui.adapter.holder;

import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.mobnius.vote.R;
import ru.mobnius.vote.data.storage.models.Answer;
import ru.mobnius.vote.ui.data.OnClickVoteItemListener;

/**
 * кнопка с вариантом ответа
 */
public class VoteButtonHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    private final Button mButton;
    private final Answer[] mAnswers;
    private final OnClickVoteItemListener mListener;
    private final long mExclusionAnswerID;
    private final long mLastAnswerId;

    public VoteButtonHolder(@NonNull View itemView, Answer[] answers, OnClickVoteItemListener listener, long exclusionAnswerID, long lastAnswerId) {
        super(itemView);
        mLastAnswerId = lastAnswerId;
        mExclusionAnswerID = exclusionAnswerID;
        mListener = listener;
        mAnswers = answers;

        mButton = itemView.findViewById(R.id.vote_button_item);
        mButton.setOnClickListener(this);
    }

    public void bind(Answer answer) {
        if(mExclusionAnswerID == answer.id) {
            mButton.setBackgroundResource(R.drawable.button_success_state);
        } else {
            mButton.setBackgroundResource(R.drawable.button_state_alt);
        }

        if(mLastAnswerId > 0) {
            if(mLastAnswerId == answer.id) {
                mButton.setBackgroundResource(R.drawable.button_accent_state);
            }
        }
        mButton.setText(answer.c_text);
    }

    @Override
    public void onClick(View v) {
        int position = getAdapterPosition();
        Answer answer = mAnswers[position];
        // если режим просмотра, то разрешено нажимать только на выбранный пункт
        if(mExclusionAnswerID > 0 && answer.id != mExclusionAnswerID) {
            return;
        }

        mListener.onClickVoteItem(answer);
    }
}
