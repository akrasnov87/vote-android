package ru.mobnius.vote.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.mobnius.vote.R;
import ru.mobnius.vote.data.storage.models.Feedbacks;
import ru.mobnius.vote.ui.adapter.holder.FeedbackAnswerHolder;

public class FeedbackAnswerAdapter extends RecyclerView.Adapter<FeedbackAnswerHolder> {

    private final List<Feedbacks> mFeedbacksList;
    private final Context mContext;

    public FeedbackAnswerAdapter(Context context, List<Feedbacks> feedbacks) {
        mFeedbacksList = feedbacks;
        mContext = context;
    }

    @NonNull
    @Override
    public FeedbackAnswerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_feedback_answer, parent, false);
        return new FeedbackAnswerHolder(mContext, view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackAnswerHolder holder, int position) {
        holder.bindFeedbacks(mFeedbacksList.get(position));
    }

    @Override
    public int getItemCount() {
        return mFeedbacksList.size();
    }
}