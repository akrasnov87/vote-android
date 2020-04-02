package ru.mobnius.vote.ui.fragment.adapter;

import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.mobnius.vote.R;
import ru.mobnius.vote.data.storage.models.Answer;
import ru.mobnius.vote.ui.fragment.data.onClickVoteItemListener;

/**
 * кнопка с вариантом ответа
 */
public class VoteButtonHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private Button mButton;
    private Answer[] mAnswers;
    private onClickVoteItemListener mListener;

    public VoteButtonHolder(@NonNull View itemView, Answer[] answers, onClickVoteItemListener listener) {
        super(itemView);

        mListener = listener;
        mAnswers = answers;

        mButton = itemView.findViewById(R.id.voteButtonItem);
        mButton.setOnClickListener(this);
    }

    public void bind(Answer answer) {
        mButton.setText(answer.c_text);
    }

    @Override
    public void onClick(View v) {
        int position = getAdapterPosition();
        Answer answer = mAnswers[position];
        mListener.onClickVoteItem(answer);
    }
}
