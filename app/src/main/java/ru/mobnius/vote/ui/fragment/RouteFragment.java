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
        String serializedFilter = PreferencesManager.getInstance().getFilter(PreferencesManager.ROUTE_FILTER_PREFS);
        String serializedSort = PreferencesManager.getInstance().getSort(PreferencesManager.ROUTE_SORT_PREFS);
        RouteSortManager routeSortManager = new RouteSortManager(PreferencesManager.ROUTE_SORT_PREFS, serializedSort);
        RouteFilterManager routeFilterManager = new RouteFilterManager(PreferencesManager.ROUTE_FILTER_PREFS, serializedFilter);
        routeFilterManager.deSerialize(serializedFilter);
        List<RouteItem> routesList = DataManager.getInstance().getRouteItems(DataManager.RouteFilter.ALL);
        if (routesList.size() == 0) {
            btnSync.setVisibility(View.VISIBLE);
        } else {
            btnSync.setVisibility(View.GONE);
            List<RouteItem> routesList1 = Arrays.asList(routeFilterManager.toFilters
                    ((routesList).toArray(new RouteItem[0])));
            List<RouteItem> list = Arrays.asList(routeSortManager.toSorters(routesList1.toArray(new RouteItem[0])));
            mRecyclerView.setAdapter(new RouteAdapter(getContext(), list));
        }
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.ROUTES;
    }
}
