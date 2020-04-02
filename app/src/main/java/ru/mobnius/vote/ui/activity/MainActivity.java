package ru.mobnius.vote.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import com.google.android.material.navigation.NavigationView;

import java.util.Date;
import java.util.Objects;

import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.MobniusApplication;
import ru.mobnius.vote.data.manager.authorization.Authorization;
import ru.mobnius.vote.data.manager.authorization.AuthorizationCache;
import ru.mobnius.vote.data.manager.configuration.PreferencesManager;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.ui.fragment.PinCodeFragment;
import ru.mobnius.vote.ui.fragment.PointFragment;
import ru.mobnius.vote.ui.fragment.RouteFragment;
import ru.mobnius.vote.utils.JsonUtil;

public class MainActivity extends SingleFragmentActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private PointFragment mPointFragment;
    private boolean mIsRouteView;

    public MainActivity() {
        super(true);
    }

    @Override
    protected Fragment createFragment() {
        mIsRouteView = PreferencesManager.getInstance().getIsRouteView();

        if (mIsRouteView) {
            return new RouteFragment();
        } else {
            mPointFragment = PointFragment.newInstance(null);
            return mPointFragment;
        }
    }


    @LayoutRes
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main_with_menu;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        NavigationView navigationView = findViewById(R.id.mainMenu_NavigationView);
        navigationView.setNavigationItemSelectedListener(this);
        mDrawerLayout = findViewById(R.id.mainMenuDrawerLayout);

        mToolbar = findViewById(R.id.mainMenu_Toolbar);
        setSupportActionBar(mToolbar);

        mToolbar.setNavigationIcon(R.drawable.ic_open_drawer_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Intent intent;
        String login = Authorization.getInstance().getLastAuthUser().getCredentials().login;
        String password = Authorization.getInstance().getLastAuthUser().getCredentials().password;
        switch (menuItem.getItemId()) {
            case R.id.navigationDrawerSynchronization:
                intent = new Intent(this, SynchronizationActivity.class);
                startActivity(intent);
                break;

            case R.id.navigationDrawerSettings:
                intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                break;

            case R.id.navigationDrawerExit:
                showDialog();
                break;

        }
        mDrawerLayout.closeDrawers();

        return false;
    }

    public void showDialog() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((MobniusApplication) getApplication()).unAuthorized(true);

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        adb.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = adb.create();
        alert.setTitle(getResources().getString(R.string.confirmExit));
        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_route_and_point, menu);
        MenuItem filterIcon = menu.findItem(R.id.route_and_point_setFilters);
        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (mIsRouteView) {
                    PointFragment fragment = PointFragment.newSearchInstance(query);
                    getSupportFragmentManager().beginTransaction().replace(R.id.single_fragment_container, fragment).commit();
                } else {
                    if (mPointFragment != null) {
                        mPointFragment.searchResult(query);
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                if (mIsRouteView) {
                    RouteFragment fragment = new RouteFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.single_fragment_container, fragment).commit();
                } else {
                    if (mPointFragment != null) {
                        mPointFragment.searchResult("");
                    }
                }
                return false;
            }
        });

        MenuItem sortIcon = menu.findItem(R.id.route_and_point_setSort);
        PreferencesManager manager = PreferencesManager.getInstance();
        boolean isFilter = mIsRouteView ?
                JsonUtil.isEmpty(manager.getFilter(PreferencesManager.ROUTE_FILTER_PREFS)) :
                JsonUtil.isEmpty(manager.getFilter(PreferencesManager.ALL_POINTS_FILTER_PREFS));

        boolean isSort = mIsRouteView ?
                JsonUtil.isEmpty(manager.getSort(PreferencesManager.ROUTE_SORT_PREFS)) :
                JsonUtil.isEmpty(manager.getSort(PreferencesManager.ALL_POINTS_SORT_PREFS));

        filterIcon.setIcon(getDrawable(isFilter ? R.drawable.ic_filter_off_24dp : R.drawable.ic_filter_on_24dp));
        sortIcon.setIcon(getDrawable(isSort ? R.drawable.ic_sort_off_24dp : R.drawable.ic_sort_on_24dp));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.route_and_point_setFilters:
                if (mIsRouteView) {
                    startActivity(FilterActivity.getIntent(this, FilterActivity.ROUTE_FILTER));
                } else {
                    startActivity(FilterActivity.getIntent(this, FilterActivity.POINT_FILTER));
                }
                return true;

            case R.id.route_and_point_setSort:
                if (mIsRouteView) {
                    startActivity(SortActivity.getIntent(this, SortActivity.ROUTE_SORT));
                } else {
                    startActivity(SortActivity.getIntent(this, SortActivity.POINT_SORT));
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public int getExceptionCode() {
        return IExceptionCode.MAIN;
    }

}
