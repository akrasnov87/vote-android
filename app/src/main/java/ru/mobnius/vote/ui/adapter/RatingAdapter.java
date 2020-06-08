package ru.mobnius.vote.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.mobnius.vote.R;
import ru.mobnius.vote.ui.adapter.holder.RatingHolder;
import ru.mobnius.vote.ui.data.RatingAsyncTask;
import ru.mobnius.vote.ui.model.RatingItemModel;

public class RatingAdapter extends RecyclerView.Adapter<RatingHolder> {

    private final Context mContext;
    private final List<RatingItemModel> mList;
    private RatingAsyncTask.OnRatingLoadedListener mListener;

    public RatingAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<>();

        if(mContext instanceof RatingAsyncTask.OnRatingLoadedListener) {
            mListener = (RatingAsyncTask.OnRatingLoadedListener) mContext;
        }
    }

    public void updateList(List<RatingItemModel> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void update(Integer uik) {
        new RatingAsyncTask(mListener).execute(uik);
    }

    public int getPosition(long userId) {
        int idx = -1;
        for (RatingItemModel model:
             mList) {
            idx++;
            if(model.user_id == userId) {
                return idx;
            }
        }

        return -1;
    }

    @NonNull
    @Override
    public RatingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_rating, parent, false);
        return new RatingHolder(mContext, view);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingHolder holder, int position) {
        holder.bind(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
