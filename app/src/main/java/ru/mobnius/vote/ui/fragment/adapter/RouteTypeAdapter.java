package ru.mobnius.vote.ui.fragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.mobnius.vote.R;
import ru.mobnius.vote.ui.model.RouteItem;

public class RouteTypeAdapter extends RecyclerView.Adapter<RouteTypeAdapter.RouteTypeHolder> {
    private Context mContext;
    private List<List<RouteItem>> mRouteItems;
    private List<String> statusTypes;
    private RecyclerView.RecycledViewPool mViewPool;


    public RouteTypeAdapter(Context context, List<List<RouteItem>> items) {

        mContext = context;
        mRouteItems = items;
        statusTypes = new ArrayList<>();
        statusTypes.add("Маршруты в работе");
        statusTypes.add("Маршруты в ожидании");
        statusTypes.add("Завершенные маршруты");
        mViewPool = new RecyclerView.RecycledViewPool();
    }

    @NonNull
    @Override
    public RouteTypeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_route_type, parent, false);
        RecyclerView rvRoutes = view.findViewById(R.id.itemRouteType_rvRouteTypes);
        LinearLayoutManager innerLLM = new LinearLayoutManager(mContext,
                LinearLayoutManager.VERTICAL, false);
        rvRoutes.setLayoutManager(innerLLM);
        rvRoutes.setRecycledViewPool(mViewPool);
        return new RouteTypeHolder(view, rvRoutes);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteTypeHolder holder, int position) {
        holder.tvTitle.setText(statusTypes.get(position));
        if (position!=0){
            holder.tvTitle.setTextColor(mContext.getResources().getColor(R.color.disabled_primary_color));
        }
        holder.rvRoutes.setAdapter(new RouteAdapter(mContext, mRouteItems.get(position), position!=0));
    }

    @Override
    public int getItemCount() {
        return mRouteItems.size();
    }

    static class RouteTypeHolder extends RecyclerView.ViewHolder{
        private TextView tvTitle;
        private RecyclerView rvRoutes;

        RouteTypeHolder(@NonNull View itemView, RecyclerView innerRv) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.itemRouteType_tvTitle);
            rvRoutes=innerRv;
        }
    }
}
