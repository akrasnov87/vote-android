package ru.mobnius.vote.ui.fragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.mobnius.vote.R;
import ru.mobnius.vote.data.storage.models.Answer;
import ru.mobnius.vote.ui.fragment.data.onClickVoteItemListener;

public class VoteButtonAdapter extends RecyclerView.Adapter<VoteButtonHolder> {

    private Context mContext;
    private onClickVoteItemListener mListener;
    private Answer[] mAnswers;
    private long mExclusionAnswerID;

    public VoteButtonAdapter(Context context, Answer[] answers, long exclusionAnswerID) {
        mContext = context;
        mAnswers = answers;
        mExclusionAnswerID = exclusionAnswerID;
        if(context instanceof onClickVoteItemListener) {
            mListener = (onClickVoteItemListener) context;
        }
    }

    @NonNull
    @Override
    public VoteButtonHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.vote_button_item, parent, false);
        return new VoteButtonHolder(view, mAnswers, mListener, mExclusionAnswerID);
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
