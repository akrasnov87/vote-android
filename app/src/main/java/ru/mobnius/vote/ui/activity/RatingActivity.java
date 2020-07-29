package ru.mobnius.vote.ui.activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.BaseActivity;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.authorization.Authorization;
import ru.mobnius.vote.data.manager.configuration.PreferencesManager;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.ui.adapter.RatingAdapter;
import ru.mobnius.vote.ui.data.RatingAsyncTask;
import ru.mobnius.vote.ui.model.RatingItemModel;
import ru.mobnius.vote.utils.DateUtil;
import ru.mobnius.vote.utils.NetworkUtil;

public class RatingActivity extends BaseActivity
    implements RatingAsyncTask.OnRatingLoadedListener{

    public static Intent getIntent(Context context) {
        return new Intent(context, RatingActivity.class);
    }

    private RatingAdapter mRatingAdapter;
    private RecyclerView recyclerView;
    private TextView tvNoInternet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setSubtitle(DateUtil.convertDateToUserString(new Date(), DateUtil.USER_SHORT_FORMAT));

        recyclerView = findViewById(R.id.rating_list);
        tvNoInternet = findViewById(R.id.rating_no_internet);

        mRatingAdapter = new RatingAdapter(this);
        recyclerView.setAdapter(mRatingAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        startProgress();
        boolean isFilter = PreferencesManager.getInstance().getRating();
        mRatingAdapter.update(isFilter ? DataManager.getInstance().getProfile().uik : null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_rating, menu);

        MenuItem filterIcon = menu.findItem(R.id.action_rating_filters);
        boolean isFilter = PreferencesManager.getInstance().getRating();
        filterIcon.setIcon(getResources().getDrawable(isFilter ? R.drawable.ic_filter_on_24dp : R.drawable.ic_filter_off_24dp));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        if(item.getItemId() == R.id.action_rating_filters) {
            boolean isFilter = !PreferencesManager.getInstance().getRating();
            item.setIcon(getResources().getDrawable(isFilter ? R.drawable.ic_filter_on_24dp : R.drawable.ic_filter_off_24dp));
            startProgress();
            mRatingAdapter.update(isFilter ? DataManager.getInstance().getProfile().uik : null);
            PreferencesManager.getInstance().setRating(isFilter);
        }

        if(item.getItemId() == R.id.action_rating_me) {
            int position = mRatingAdapter.getPosition(Authorization.getInstance().getUser().getUserId());
            recyclerView.scrollToPosition(position);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.RATING;
    }

    @Override
    public void onRatingLoaded(List<RatingItemModel> items) {
        tvNoInternet.setVisibility(NetworkUtil.isNetworkAvailable(this) && NetworkUtil.isConnectionFast(this) ? View.GONE : View.VISIBLE);
        mRatingAdapter.updateList(items);
        stopProgress();
    }
}
