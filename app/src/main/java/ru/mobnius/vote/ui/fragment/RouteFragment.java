package ru.mobnius.vote.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.BaseFragment;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.configuration.PreferencesManager;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.ui.data.RouteFilterManager;
import ru.mobnius.vote.ui.data.RouteSortManager;
import ru.mobnius.vote.ui.fragment.adapter.RouteAdapter;
import ru.mobnius.vote.ui.fragment.adapter.RouteTypeAdapter;
import ru.mobnius.vote.ui.model.RouteItem;
import ru.mobnius.vote.utils.DateUtil;


public class RouteFragment extends BaseFragment {
    private RecyclerView mRecyclerView;
    private List<RouteItem> mRoutesList;
    private RouteFilterManager mRouteFilterManager;
    private RouteSortManager mRouteSortManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_route, container, false);
        mRecyclerView = view.findViewById(R.id.fRoute_rvRoutes);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        List<List<RouteItem>> innerList = new ArrayList<>();
        String serializedFilter = PreferencesManager.getInstance().getFilter(PreferencesManager.ROUTE_FILTER_PREFS);
        String serializedSort = PreferencesManager.getInstance().getSort(PreferencesManager.ROUTE_SORT_PREFS);
        mRouteSortManager = new RouteSortManager(PreferencesManager.ROUTE_SORT_PREFS, serializedSort);
        mRouteFilterManager = new RouteFilterManager(PreferencesManager.ROUTE_FILTER_PREFS, serializedFilter);
        mRouteFilterManager.deSerialize(serializedFilter);
        mRoutesList = Arrays.asList(mRouteFilterManager.toFilters
                (DataManager.getInstance().getRouteItems(DataManager.RouteFilter.ALL).toArray(new RouteItem[0])));
        List<RouteItem> list = Arrays.asList(mRouteSortManager.toSorters(mRoutesList.toArray(new RouteItem[0])));
        List<RouteItem> currentRoutes = new ArrayList<>();
        List<RouteItem> futureRoutes = new ArrayList<>();
        List<RouteItem> finishRoutes = new ArrayList<>();
        for (RouteItem item : list) {
            if (DataManager.getInstance().isRouteFinish(item.id)) {
                finishRoutes.add(item);

            } else {
                if (item.isContains()) {
                    currentRoutes.add(item);
                } else {
                    futureRoutes.add(item);
                }
            }
        }
        if (currentRoutes.size() > 0) {
            innerList.add(currentRoutes);
        }
        if (futureRoutes.size() > 0) {
            innerList.add(futureRoutes);
        }
        if (finishRoutes.size() > 0) {
            innerList.add(finishRoutes);
        }
        mRecyclerView.setAdapter(new RouteAdapter(getContext(), innerList));
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.ROUTES;
    }
}
