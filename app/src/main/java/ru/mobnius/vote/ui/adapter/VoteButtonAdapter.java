package ru.mobnius.vote.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.mobnius.vote.R;
import ru.mobnius.vote.data.storage.models.Answer;
import ru.mobnius.vote.ui.adapter.holder.VoteButtonHolder;
import ru.mobnius.vote.ui.data.OnClickVoteItemListener;

public class VoteButtonAdapter extends RecyclerView.Adapter<VoteButtonHolder> {

    private final Context mContext;
    private OnClickVoteItemListener mListener;
    private Answer[] mAnswers;
    private final long mExclusionAnswerID;
    private final long mLastAnswerId;

    public VoteButtonAdapter(Context context, Answer[] answers, long exclusionAnswerID, long lastAnswerId) {
        mContext = context;
        if(exclusionAnswerID > 0) {
            // на данный вопрос был данн ответ с индентификатором exclusionAnswerID
            for(Answer item : answers) {
                if(item.id == exclusionAnswerID) {
                    mAnswers = new Answer[1];
                    mAnswers[0] = item;
                }
            }
        } else {
            mAnswers = answers;
        }
        mLastAnswerId = lastAnswerId;

        mExclusionAnswerID = exclusionAnswerID;
        if(context instanceof OnClickVoteItemListener) {
            mListener = (OnClickVoteItemListener) context;
        }
    }

    @NonNull
    @Override
    public VoteButtonHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.vote_button_item, parent, false);
        return new VoteButtonHolder(mContext, view, mAnswers, mListener, mExclusionAnswerID, mLastAnswerId);
    }

    @Override
    public void onBindViewHolder(@NonNull VoteButtonHolder holder, int position) {
        holder.bind(mAnswers[position], mContext);
    }

    @Override
    public int getItemCount() {
        return mAnswers.length;
    }
}
