package ru.mobnius.vote.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.BaseFragment;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.configuration.PreferencesManager;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.ui.activity.SynchronizationActivity;
import ru.mobnius.vote.ui.data.RouteFilterManager;
import ru.mobnius.vote.ui.data.RouteSortManager;
import ru.mobnius.vote.ui.fragment.adapter.RouteAdapter;
import ru.mobnius.vote.ui.model.PointFilter;
import ru.mobnius.vote.ui.model.RouteItem;


public class RouteFragment extends BaseFragment {
    private RecyclerView mRecyclerView;
    private Button btnSync;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_route, container, false);
        mRecyclerView = view.findViewById(R.id.fRoute_rvRoutes);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        btnSync = view.findViewById(R.id.fRoute_btnSynchronization);
        btnSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SynchronizationActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        List<RouteItem> routesList = DataManager.getInstance().getRouteItems(DataManager.RouteFilter.ALL);
        if (routesList.size() == 0) {
            btnSync.setVisibility(View.VISIBLE);
        } else {
            btnSync.setVisibility(View.GONE);
            invalidateList();
        }
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.ROUTES;
    }

    public void invalidateList() {
        List<RouteItem> routes = DataManager.getInstance().getRouteItems(DataManager.RouteFilter.ALL);
        if (!PreferencesManager.getInstance().isUndoneRoutes()) {
            List<RouteItem> undoneRoutes = new ArrayList<>();
            for (RouteItem route : routes) {
                int done = DataManager.getInstance().getCountDonePoints(route.id);
                int allPoints = DataManager.getInstance().getPointItems(route.id, PointFilter.ALL).size();
                if (done != allPoints) {
                    undoneRoutes.add(route);
                }
            }
            PreferencesManager.getInstance().getSharedPreferences().edit().
                    putBoolean(PreferencesManager.ROUTE_FILTER_PREFS, true).apply();
            mRecyclerView.setAdapter(new RouteAdapter(getContext(), undoneRoutes));
        } else {
            PreferencesManager.getInstance().getSharedPreferences().edit().
                    putBoolean(PreferencesManager.ROUTE_FILTER_PREFS, false).apply();
            mRecyclerView.setAdapter(new RouteAdapter(getContext(), routes));
        }

    }
}
