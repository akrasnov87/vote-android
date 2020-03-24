package ru.mobnius.vote.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import ru.mobnius.vote.Names;
import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.configuration.PreferencesManager;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.ui.fragment.PointFragment;
import ru.mobnius.vote.utils.JsonUtil;

public class PointActivity extends SingleFragmentActivity {
    private PointFragment mPointFragment;

    public static Intent newIntent(Context context, String routeId) {
        Intent intent = new Intent(context, PointActivity.class);
        intent.putExtra(Names.ROUTE_ID, routeId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return mPointFragment = PointFragment.newInstance(getIntent().getStringExtra(Names.ROUTE_ID));
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

        searchView.setOnQueryTextListener(mPointFragment);
        searchView.setOnCloseListener(mPointFragment);

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
}
