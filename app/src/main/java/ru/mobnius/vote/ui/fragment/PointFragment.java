package ru.mobnius.vote.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import ru.mobnius.vote.Names;
import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.BaseFragment;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.configuration.PreferencesManager;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.ui.activity.MainActivity;
import ru.mobnius.vote.ui.data.PointFilterManager;
import ru.mobnius.vote.ui.data.PointSearchManager;
import ru.mobnius.vote.ui.data.PointSortManager;
import ru.mobnius.vote.ui.fragment.adapter.PointAdapter;
import ru.mobnius.vote.ui.model.PointFilter;
import ru.mobnius.vote.ui.model.PointItem;
import ru.mobnius.vote.utils.JsonUtil;

public class PointFragment extends BaseFragment implements SearchView.OnQueryTextListener, SearchView.OnCloseListener {
    private static final String QUERY_RESULT = "query_result";
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private TextView tvTitle;

    private DataManager mDataManager;
    private PointSearchManager mPointSearchManager;
    private boolean mIsRouteView;

    private String routeId;

    public static PointFragment newInstance(String routeId) {
        PointFragment pointFragment;
        if (routeId != null) {
            pointFragment = new PointFragment();
            Bundle args = new Bundle();
            args.putString(Names.ROUTE_ID, routeId);
            pointFragment.setArguments(args);
        } else {
            pointFragment = new PointFragment();
        }

        return pointFragment;
    }

    public static PointFragment newSearchInstance(String query) {
        PointFragment pointFragment = new PointFragment();
        Bundle args = new Bundle();
        args.putString(QUERY_RESULT, query);
        pointFragment.setArguments(args);
        return pointFragment;
    }

    public PointFragment() {
        mDataManager = DataManager.getInstance();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_point, container, false);
        mProgressBar = view.findViewById(R.id.fPoint_pbRoutesProgress);
        tvTitle = view.findViewById(R.id.fPoint_tvPointsTitle);
        mRecyclerView = view.findViewById(R.id.fPoint_rvPoints);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        routeId = null;
        String query = null;
        if (getArguments() != null) {
            routeId = getArguments().getString(Names.ROUTE_ID);
            mIsRouteView = getArguments().containsKey(Names.ROUTE_ID);
            query = getArguments().getString(QUERY_RESULT);
        }
        if (query != null) {
            searchResult(query);
            mProgressBar.setVisibility(View.GONE);
            tvTitle.setText("Результаты поиска");
        } else {
            if (!isRouteView()) {
                tvTitle.setText("Задания по всем маршрутам");
                mProgressBar.setVisibility(View.GONE);
            }
            mRecyclerView.setAdapter(new PointAdapter(getContext(), getFilteredAndSortedList()));
            int donePoints = mDataManager.getCountDonePoints(routeId);
            if (donePoints > 0) {
                mProgressBar.setMax(mDataManager.getPointItems(routeId, PointFilter.ALL).size());
                mProgressBar.setProgress(donePoints);
                mProgressBar.setVisibility(View.VISIBLE);
            }
        }
    }

    public void searchResult(String query) {
        if (query.isEmpty()) {
            mRecyclerView.setAdapter(new PointAdapter(getContext(), getFilteredAndSortedList()));
        } else {
            mPointSearchManager = new PointSearchManager();
            List<PointItem> list;
            list = Arrays.asList(mPointSearchManager.toFilters(getFilteredAndSortedList().toArray(new PointItem[0]), query));
            mRecyclerView.setAdapter(new PointAdapter(getContext(), list));
        }
    }

    /**
     * @return Получение отсортированного и отфильтрованного массива PointItem
     */
    private List<PointItem> getFilteredAndSortedList() {
        String prefName = isRouteView() ? PreferencesManager.POINT_FILTER_PREFS : PreferencesManager.ALL_POINTS_FILTER_PREFS;
        String sortPref = isRouteView() ? PreferencesManager.POINT_SORT_PREFS : PreferencesManager.ALL_POINTS_SORT_PREFS;
        String serializedFilter = PreferencesManager.getInstance().getFilter(prefName);
        String serializedSort = PreferencesManager.getInstance().getSort(sortPref);
        PointSortManager pointSortManager = new PointSortManager(sortPref, serializedSort);
        pointSortManager.deSerialize(serializedSort);
        PointFilterManager pointFilterManager = new PointFilterManager(prefName, serializedFilter);
        pointFilterManager.deSerialize(serializedFilter);
        List<PointItem> pointsList1 = Arrays.asList(pointFilterManager.toFilters(mDataManager.getPointItems(routeId, PointFilter.ALL).toArray(new PointItem[0])));
        return Arrays.asList(pointSortManager.toSorters(pointsList1.toArray(new PointItem[0])));
    }

    /**
     * Представление в режиме маршрута
     *
     * @return режим (Все задания или задания отдельного маршрута)
     */
    private boolean isRouteView() {
        return mIsRouteView;
    }

    @Override
    public int getExceptionCode() {
        int exeption = IExceptionCode.ALL_POINTS;
        if (PreferencesManager.getInstance() != null && PreferencesManager.getInstance().getIsRouteView()) {
            exeption = IExceptionCode.POINTS;
        }
        return exeption;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (isRouteView()) {
            searchResult(query);
        } else {
            searchResult(query);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onClose() {
        if (isRouteView()) {
            searchResult(JsonUtil.EMPTY);
        } else {

        }
        return false;
    }
}
