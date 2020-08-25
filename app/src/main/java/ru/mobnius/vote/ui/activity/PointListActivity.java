package ru.mobnius.vote.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
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
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import ru.mobnius.vote.Names;
import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.BaseActivity;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.authorization.Authorization;
import ru.mobnius.vote.data.manager.configuration.PreferencesManager;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.data.storage.models.Routes;
import ru.mobnius.vote.ui.adapter.PointAdapter;
import ru.mobnius.vote.ui.adapter.holder.PointHolder;
import ru.mobnius.vote.ui.data.PointSearchManager;
import ru.mobnius.vote.ui.model.PointFilter;
import ru.mobnius.vote.ui.model.PointItem;
import ru.mobnius.vote.ui.model.RouteInfo;
import ru.mobnius.vote.utils.HelpUtil;
import ru.mobnius.vote.utils.JsonUtil;
import ru.mobnius.vote.utils.StringUtil;

public class PointListActivity extends BaseActivity
        implements SearchView.OnQueryTextListener, PointHolder.OnPointItemListeners {

    private DataManager mDataManager;
    private String routeId;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private PreferencesManager mPreferencesManager;
    private TextView tvMessage;
    private int mPositionSelected = 0;
    public static int POINT_LIST_REQUEST_CODE = 2;

    public static Intent newIntent(Context context, String routeId) {
        Intent intent = new Intent(context, PointListActivity.class);
        intent.putExtra(Names.ROUTE_ID, routeId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_list);

        if (savedInstanceState != null) {
            mPositionSelected = savedInstanceState.getInt(Names.ID, 0);
        }

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        mPreferencesManager = PreferencesManager.getInstance();

        routeId = getIntent().getStringExtra(Names.ROUTE_ID);
        mDataManager = DataManager.getInstance();
        mProgressBar = findViewById(R.id.point_list_progress);
        mRecyclerView = findViewById(R.id.point_list);
        tvMessage = findViewById(R.id.point_list_message);
        mRecyclerView.setAdapter(new PointAdapter(this, setPriorityList()));
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!DataManager.getInstance().isRouteStatus(routeId, "RECEIVED")) {
            // Принят
            DataManager.getInstance().setRouteStatus(routeId, "RECEIVED");
        }

        RouteInfo routeInfo = DataManager.getInstance().getRouteInfo(routeId);
        Objects.requireNonNull(getSupportActionBar()).setSubtitle(routeInfo.getNumber());
        if (routeInfo.getDateEnd().getTime() <= new Date().getTime()) {
            // Просрочен
            if (!DataManager.getInstance().isRouteStatus(routeId, "EXPIRED")) {
                // Выполняется
                DataManager.getInstance().setRouteStatus(routeId, "EXPIRED");
            }
        }

        int donePoints = mDataManager.getCountDonePoints(routeId);
        if (donePoints > 0) {
            if (!DataManager.getInstance().isRouteStatus(routeId, "PROCCESS")) {
                // Выполняется
                DataManager.getInstance().setRouteStatus(routeId, "PROCCESS");
            }

            Routes routeItem = mDataManager.getDaoSession().getRoutesDao().load(routeId);
            mProgressBar.setMax(routeItem.n_count);
            mProgressBar.setProgress(donePoints);
            mProgressBar.setVisibility(View.VISIBLE);

            if (donePoints == routeItem.n_count) {
                if (!DataManager.getInstance().isRouteStatus(routeId, "DONED")) {
                    // Выполнено
                    DataManager.getInstance().setRouteStatus(routeId, "DONED");
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();

        Objects.requireNonNull(mRecyclerView.getLayoutManager()).scrollToPosition(mPositionSelected);
    }

    @Override
    public String getHelpKey() {
        return "points";
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Names.ID, mPositionSelected);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_point, menu);
        MenuItem sortIcon = menu.findItem(R.id.point_filter);
        MenuItem searchItem = menu.findItem(R.id.point_search);
        MenuItem priorityItem = menu.findItem(R.id.point_zero_priority);
        if(Authorization.getInstance().getUser().isCandidate()) {
            priorityItem.setTitle(PreferencesManager.getInstance().isZeroPriority() ? getResources().getString(R.string.hide_zero_priority) : getResources().getString(R.string.show_zero_priority));
        } else {
            priorityItem.setVisible(false);
        }
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setInputType(InputType.TYPE_CLASS_NUMBER);

        searchView.setOnQueryTextListener(this);
        sortIcon.setIcon(getResources().getDrawable(mPreferencesManager.getSort() ? R.drawable.ic_filter_on_24dp : R.drawable.ic_filter_off_24dp));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.point_filter) {
            item.setIcon(getResources().getDrawable(mPreferencesManager.getSort() ? R.drawable.ic_filter_off_24dp : R.drawable.ic_filter_on_24dp));
            PreferencesManager.getInstance().setSort(!mPreferencesManager.getSort());
            List<PointItem> list = setPriorityList();
            mRecyclerView.setAdapter(new PointAdapter(this, list));
        }

        if (item.getItemId() == R.id.point_feedback) {
            // NO_DATA
            startActivity(FeedbackActivity.getIntent(this, FeedbackActivity.NO_DATA, "{\"route_id\": \"" + routeId + "\"}"));
        }

        if (item.getItemId() == R.id.point_zero_priority) {
            item.setTitle(PreferencesManager.getInstance().isZeroPriority() ? getResources().getString(R.string.show_zero_priority):getResources().getString(R.string.hide_zero_priority)  );
            mPreferencesManager.setZeroPriority(!mPreferencesManager.isZeroPriority());
            mRecyclerView.setAdapter(new PointAdapter(this, setPriorityList()));
        }
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private List<PointItem> setPriorityList() {
        List<PointItem> list= getSortedList(mPreferencesManager.getSort());
        if(!mPreferencesManager.isZeroPriority() && Authorization.getInstance().getUser().isCandidate()) {
            for (Iterator<PointItem> it = list.iterator(); it.hasNext(); ) {
                PointItem pointItem = it.next();
                if (pointItem.priority == 0) {
                    it.remove();
                }
            }
        }
        return list;
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.POINTS;
    }

    private void searchResult(String query) {
        if (JsonUtil.isEmpty(query)) {
            mRecyclerView.setAdapter(new PointAdapter(this, setPriorityList()));
        } else {
            PointSearchManager pointSearchManager = new PointSearchManager();
            List<PointItem> list;
            list = Arrays.asList(pointSearchManager.toFilters(setPriorityList().toArray(new PointItem[0]), query));
            mRecyclerView.setAdapter(new PointAdapter(this, list));
        }
    }

    private List<PointItem> getSortedList(boolean sort) {
        List<PointItem> list = mDataManager.getPointItems(routeId, sort ? PointFilter.UNDONE : PointFilter.ALL);
        if (sort && list.size() == 0) {
            tvMessage.setVisibility(View.VISIBLE);
        } else {
            tvMessage.setVisibility(View.GONE);
        }
        return list;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        searchResult(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (StringUtil.isEmptyOrNull(newText)) {
            searchResult(JsonUtil.EMPTY);
        }
        return false;
    }

    @Override
    public void onPointItemClick(PointItem pointItem, int position) {
        mPositionSelected = position;
        startActivityForResult(QuestionActivity.newIntent(this, pointItem), QuestionActivity.QUESTION_REQUEST_CODE);
    }
}
