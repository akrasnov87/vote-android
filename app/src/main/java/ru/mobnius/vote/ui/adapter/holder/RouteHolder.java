package ru.mobnius.vote.ui.adapter.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.mobnius.vote.Names;
import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.ui.activity.FeedbackActivity;
import ru.mobnius.vote.ui.activity.PointListActivity;
import ru.mobnius.vote.ui.model.FeedbackExcessData;
import ru.mobnius.vote.ui.model.PointFilter;
import ru.mobnius.vote.ui.model.PointItem;
import ru.mobnius.vote.ui.model.RouteInfo;
import ru.mobnius.vote.ui.model.RouteItem;
import ru.mobnius.vote.utils.DateUtil;

public class RouteHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    private final TextView tvRouteName;
    private final TextView tvType;
    private final TextView tvPointCount;
    private final TextView tvEndDate;
    private final ProgressBar mProgress;
    private final ImageButton mFeedback;

    private final Context mContext;

    private RouteItem mRouteItem;

    public RouteHolder(final Context context, @NonNull View itemView) {
        super(itemView);
        mContext = context;
        tvRouteName = itemView.findViewById(R.id.item_route_name);
        tvType = itemView.findViewById(R.id.item_route_type);
        tvPointCount = itemView.findViewById(R.id.item_route_point_count);
        tvEndDate = itemView.findViewById(R.id.item_route_date);
        mProgress = itemView.findViewById(R.id.item_route_progress);
        mFeedback = itemView.findViewById(R.id.item_route_feedback);
        mFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(FeedbackActivity.getIntent(context, FeedbackActivity.CHANGE_HOUSE_NUMBER, "{\"route_id\": \"" + mRouteItem.id + "\"}"));
            }
        });

        itemView.setOnClickListener(this);
    }

    public void bindRoute(RouteItem routeItem) {
        mRouteItem = routeItem;

        tvRouteName.setText(routeItem.number);
        tvType.setText(routeItem.typeName);

        RouteInfo info = DataManager.getInstance().getRouteInfo(routeItem.id);
        String endDate = DateUtil.convertDateToUserString(info.getDateEnd(), DateUtil.USER_SHORT_FORMAT);
        tvEndDate.setText(endDate);

        List<PointItem> doneList = DataManager.getInstance().getPointItems(routeItem.id, PointFilter.DONE);

        int allPoints = routeItem.count;
        int donePoints = doneList.size();
        mProgress.setMax(allPoints);
        mProgress.setProgress(donePoints);
        mProgress.setSecondaryProgress(allPoints);
        String pointCount = mContext.getResources().getQuantityString(R.plurals.plurals_routes, allPoints, allPoints);
        tvPointCount.setText(pointCount);
    }

    @Override
    public void onClick(View v) {
        mContext.startActivity(PointListActivity.newIntent(mContext, mRouteItem.id));
    }
}
