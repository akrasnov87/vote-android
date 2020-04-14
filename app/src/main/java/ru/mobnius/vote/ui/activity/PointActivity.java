package ru.mobnius.vote.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

import ru.mobnius.vote.Names;
import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.BaseActivity;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.configuration.PreferencesManager;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.ui.data.PointFilterManager;
import ru.mobnius.vote.ui.data.PointSearchManager;
import ru.mobnius.vote.ui.data.PointSortManager;
import ru.mobnius.vote.ui.fragment.adapter.PointAdapter;
import ru.mobnius.vote.ui.model.PointFilter;
import ru.mobnius.vote.ui.model.PointItem;
import ru.mobnius.vote.utils.JsonUtil;

public class PointActivity extends BaseActivity implements SearchView.OnQueryTextListener, SearchView.OnCloseListener {
    private DataManager mDataManager;
    private String routeId;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private TextView tvTitle;
    private static final String QUERY_RESULT = "query_result";

    public static Intent newIntent(Context context, String routeId) {
        Intent intent = new Intent(context, PointActivity.class);
        intent.putExtra(Names.ROUTE_ID, routeId);
        return intent;
    }

    public static Intent newSearchIntent(Context context, String routeId, String query) {
        Intent intent = new Intent(context, PointActivity.class);
        intent.putExtra(Names.ROUTE_ID, routeId);
        intent.putExtra(QUERY_RESULT, query);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_point);
        routeId = getIntent().getStringExtra(Names.ROUTE_ID);
        mDataManager = DataManager.getInstance();
        mProgressBar = findViewById(R.id.fPoint_pbRoutesProgress);
        tvTitle = findViewById(R.id.fPoint_tvPointsTitle);
        mRecyclerView = findViewById(R.id.fPoint_rvPoints);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
    }

    @Override
    protected void onStart() {
        super.onStart();
        String query = null;
            query = getIntent().getStringExtra(QUERY_RESULT);

        if (query != null) {
            searchResult(query);
            mProgressBar.setVisibility(View.GONE);
            tvTitle.setText("Результаты поиска");
        } else {
            mRecyclerView.setAdapter(new PointAdapter(this, getFilteredAndSortedList()));
            int donePoints = mDataManager.getCountDonePoints(routeId);
            if (donePoints > 0) {
                mProgressBar.setMax(mDataManager.getPointItems(routeId, PointFilter.ALL).size());
                mProgressBar.setProgress(donePoints);
                mProgressBar.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_route_and_point, menu);
        MenuItem filterIcon = menu.findItem(R.id.route_and_point_setFilters);
        MenuItem sortIcon = menu.findItem(R.id.route_and_point_setSort);
        PreferencesManager manager = PreferencesManager.getInstance();
        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);

        boolean isFilterOff = JsonUtil.isEmpty(manager.getFilter(PreferencesManager.POINT_FILTER_PREFS));
        boolean isSortOff = JsonUtil.isEmpty(manager.getSort(PreferencesManager.POINT_SORT_PREFS));
        filterIcon.setIcon(isFilterOff ? getDrawable(R.drawable.ic_filter_off_24dp) : getDrawable(R.drawable.ic_filter_on_24dp));
        sortIcon.setIcon(isSortOff ? getDrawable(R.drawable.ic_sort_off_24dp) : getDrawable(R.drawable.ic_sort_on_24dp));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.route_and_point_setFilters:
                startActivity(FilterActivity.getIntent(this, getIntent().getStringExtra(Names.ROUTE_ID)));
                return true;
            case R.id.route_and_point_setSort:
                startActivity(SortActivity.getIntent(this, getIntent().getStringExtra(Names.ROUTE_ID)));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.POINTS;
    }


    public void searchResult(String query) {
        if (query.equals(JsonUtil.EMPTY)) {
            mRecyclerView.setAdapter(new PointAdapter(this, getFilteredAndSortedList()));
        } else {
            PointSearchManager pointSearchManager = new PointSearchManager();
            List<PointItem> list;
            list = Arrays.asList(pointSearchManager.toFilters(getFilteredAndSortedList().toArray(new PointItem[0]), query));
            mRecyclerView.setAdapter(new PointAdapter(this, list));
        }
    }
    private List<PointItem> getFilteredAndSortedList() {
        String prefName = PreferencesManager.POINT_FILTER_PREFS;
        String sortPref = PreferencesManager.POINT_SORT_PREFS;

        String serializedFilter = PreferencesManager.getInstance().getFilter(prefName);
        String serializedSort = PreferencesManager.getInstance().getSort(sortPref);
        PointSortManager pointSortManager = new PointSortManager(sortPref, serializedSort);
        pointSortManager.deSerialize(serializedSort);
        PointFilterManager pointFilterManager = new PointFilterManager(prefName, serializedFilter);
        pointFilterManager.deSerialize(serializedFilter);
        List<PointItem> pointsList1 = Arrays.asList(pointFilterManager.toFilters(mDataManager.getPointItems(routeId, PointFilter.ALL).toArray(new PointItem[0])));
        return Arrays.asList(pointSortManager.toSorters(pointsList1.toArray(new PointItem[0])));
    }

    @Override
    public boolean onClose() {
        searchResult(JsonUtil.EMPTY);
        return false;
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        searchResult(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
