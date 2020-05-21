package ru.mobnius.vote.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import ru.mobnius.vote.Names;
import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.BaseActivity;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.configuration.PreferencesManager;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.ui.data.PointSearchManager;
import ru.mobnius.vote.ui.fragment.adapter.PointAdapter;
import ru.mobnius.vote.ui.model.PointFilter;
import ru.mobnius.vote.ui.model.PointItem;
import ru.mobnius.vote.utils.JsonUtil;

public class PointActivity extends BaseActivity implements SearchView.OnQueryTextListener, SearchView.OnCloseListener {
    private DataManager mDataManager;
    private String routeId;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private static final String QUERY_RESULT = "query_result";
    private PreferencesManager mPreferencesManager;

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
        Objects.requireNonNull(getSupportActionBar()).setTitle("Квартиры");
        routeId = getIntent().getStringExtra(Names.ROUTE_ID);
        mDataManager = DataManager.getInstance();
        mProgressBar = findViewById(R.id.fPoint_pbRoutesProgress);
        mRecyclerView = findViewById(R.id.fPoint_rvPoints);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
        mPreferencesManager = PreferencesManager.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        String query = null;
        query = getIntent().getStringExtra(QUERY_RESULT);

        if (query != null) {
            searchResult(query);
            mProgressBar.setVisibility(View.GONE);
        } else {
            mRecyclerView.setAdapter(new PointAdapter(this, getSortedList(mPreferencesManager.isSort())));
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
        inflater.inflate(R.menu.menu_point, menu);
        MenuItem sortIcon = menu.findItem(R.id.route_and_point_setSort);
        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);
        sortIcon.setIcon(getResources().getDrawable(mPreferencesManager.isSort() ? R.drawable.ic_sort_on_24dp : R.drawable.ic_sort_off_24dp));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.route_and_point_setSort:
                item.setIcon(getResources().getDrawable(mPreferencesManager.isSort() ? R.drawable.ic_sort_off_24dp : R.drawable.ic_sort_on_24dp));
                if (mPreferencesManager.isSort()) {
                    PreferencesManager.getInstance().getSharedPreferences().edit().
                            putBoolean(PreferencesManager.POINT_SORT_PREFS, false).apply();
                } else {
                    PreferencesManager.getInstance().getSharedPreferences().edit().
                            putBoolean(PreferencesManager.POINT_SORT_PREFS, true).apply();
                }
                mRecyclerView.setAdapter(new PointAdapter(this, getSortedList(mPreferencesManager.isSort())));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.POINTS;
    }


    public void searchResult(String query) {
        if (query.equals(JsonUtil.EMPTY)) {
            mRecyclerView.setAdapter(new PointAdapter(this, getSortedList(mPreferencesManager.isSort())));
        } else {
            PointSearchManager pointSearchManager = new PointSearchManager();
            List<PointItem> list;
            list = Arrays.asList(pointSearchManager.toFilters(getSortedList(mPreferencesManager.isSort()).toArray(new PointItem[0]), query));
            mRecyclerView.setAdapter(new PointAdapter(this, list));
        }
    }

    private List<PointItem> getSortedList(boolean sort) {
        List<PointItem> pointsList;
        if (sort) {
            pointsList = mDataManager.getPointItems(routeId, PointFilter.UNDONE);
        } else {
            pointsList = mDataManager.getPointItems(routeId, PointFilter.ALL);
        }
        return pointsList;
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
