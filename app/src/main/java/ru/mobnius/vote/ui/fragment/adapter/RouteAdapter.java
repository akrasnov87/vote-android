package ru.mobnius.vote.ui.fragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.ui.activity.PointActivity;
import ru.mobnius.vote.ui.activity.RouteInfoActivity;
import ru.mobnius.vote.ui.model.PointFilter;
import ru.mobnius.vote.ui.model.PointItem;
import ru.mobnius.vote.ui.model.RouteInfo;
import ru.mobnius.vote.ui.model.RouteItem;
import ru.mobnius.vote.utils.DateUtil;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RouteHolder> {

    private Context mContext;
    private List<RouteItem> mRouteItems;

    public RouteAdapter(Context context, List<RouteItem> items) {
        mContext = context;
        mRouteItems = items;
    }

    @NonNull
    @Override
    public RouteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_route, parent, false);
        return new RouteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteAdapter.RouteHolder holder, int position) {
        if (mRouteItems.size() > 0) {
            holder.bindRoute(mRouteItems.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mRouteItems.size();
    }

    class RouteHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvRouteName;
        private TextView tvType;
        private TextView tvPointCount;
        private TextView tvEndDate;
        private ImageView ivRouteInfo;
        private ProgressBar pbRouteProgress;

        public RouteHolder(@NonNull View itemView) {
            super(itemView);
            tvRouteName = itemView.findViewById(R.id.itemRoute_tvRouteName);
            tvType = itemView.findViewById(R.id.itemRoute_tvType);
            tvPointCount = itemView.findViewById(R.id.itemRoute_tvPointsCount);
            tvEndDate = itemView.findViewById(R.id.itemRoute_tvEndDate);
            pbRouteProgress = itemView.findViewById(R.id.itemRoute_ivRouteProgress);
            ivRouteInfo.setOnClickListener(this);
            itemView.setOnClickListener(this);

        }

        public void bindRoute(RouteItem routeItem) {
            tvRouteName.setText(routeItem.number);
            tvType.setText(routeItem.typeName);
            RouteInfo info = DataManager.getInstance().getRouteInfo(routeItem.id);
            String endDate = "До " + DateUtil.convertDateToUserString(info.getDateEnd(), DateUtil.USER_SHORT_FORMAT);
            tvEndDate.setText(endDate);
            List<PointItem> allList = DataManager.getInstance().getPointItems(routeItem.id, PointFilter.ALL);
            List<PointItem> doneList = DataManager.getInstance().getPointItems(routeItem.id, PointFilter.DONE);
            pbRouteProgress.setMax(allList.size());
            pbRouteProgress.setProgress(doneList.size());
            String pointCount = mContext.getResources().getQuantityString(R.plurals.plurals_routes, allList.size(), allList.size());
            tvPointCount.setText(pointCount);

        }

        @Override
        public void onClick(View v) {
            String routeId = mRouteItems.get(getLayoutPosition()).id;
            switch (v.getId()) {
                default:
                    mContext.startActivity(PointActivity.newIntent(mContext, routeId));
                    break;
            }
        }
    }
}
