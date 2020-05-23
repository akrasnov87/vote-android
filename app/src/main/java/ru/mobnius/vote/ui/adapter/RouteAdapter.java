package ru.mobnius.vote.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.mobnius.vote.R;
import ru.mobnius.vote.ui.adapter.holder.RouteHolder;
import ru.mobnius.vote.ui.model.RouteItem;

public class RouteAdapter extends RecyclerView.Adapter<RouteHolder> {

    private final Context mContext;
    private final List<RouteItem> mRouteItems;

    public RouteAdapter(Context context, List<RouteItem> items) {
        mContext = context;
        mRouteItems = items;
    }

    @NonNull
    @Override
    public RouteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_route, parent, false);
        return new RouteHolder(mContext, view);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteHolder holder, int position) {
        holder.bindRoute(mRouteItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mRouteItems.size();
    }
}
