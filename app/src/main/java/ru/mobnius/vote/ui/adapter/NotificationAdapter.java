package ru.mobnius.vote.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.storage.models.FeedbackTypes;
import ru.mobnius.vote.data.storage.models.Feedbacks;
import ru.mobnius.vote.ui.adapter.holder.NotificationHolder;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationHolder> {

    private final List<Feedbacks> mFeedbacksList;
    private final Context mContext;

    public NotificationAdapter(Context context, List<Feedbacks> feedbacks) {
        mFeedbacksList = feedbacks;
        mContext = context;
    }

    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_feedback_answer, parent, false);

        List<FeedbackTypes> feedbackTypes = DataManager.getInstance().getDaoSession().getFeedbackTypesDao().loadAll();
        return new NotificationHolder(view, feedbackTypes);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationHolder holder, int position) {
        holder.bind(mFeedbacksList.get(position));
    }

    @Override
    public int getItemCount() {
        return mFeedbacksList.size();
    }
}