package ru.mobnius.vote.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.mobnius.vote.R;
import ru.mobnius.vote.ui.adapter.holder.PointHolder;
import ru.mobnius.vote.ui.model.PointItem;

public class PointAdapter extends RecyclerView.Adapter<PointHolder> {

    private final List<PointItem> mPointsList;
    private final Context mContext;

    public PointAdapter(Context context, List<PointItem> pointItems) {
        mPointsList = pointItems;
        mContext = context;
    }

    @NonNull
    @Override
    public PointHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_point, parent, false);
        return new PointHolder(mContext, view);
    }

    @Override
    public void onBindViewHolder(@NonNull PointHolder holder, int position) {
        holder.bindPoints(mPointsList.get(position));
    }

    @Override
    public int getItemCount() {
        return mPointsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
