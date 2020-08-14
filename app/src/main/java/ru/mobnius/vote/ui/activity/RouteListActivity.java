package ru.mobnius.vote.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ru.mobnius.vote.Names;
import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.BaseActivity;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.MobniusApplication;
import ru.mobnius.vote.data.manager.RequestManager;
import ru.mobnius.vote.data.manager.authorization.Authorization;
import ru.mobnius.vote.data.manager.configuration.PreferencesManager;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.ui.adapter.RouteAdapter;
import ru.mobnius.vote.ui.component.MySnackBar;
import ru.mobnius.vote.ui.data.RatingAsyncTask;
import ru.mobnius.vote.ui.data.RatingCandidateAsyncTask;
import ru.mobnius.vote.ui.fragment.StatisticDialogFragment;
import ru.mobnius.vote.ui.model.ProfileItem;
import ru.mobnius.vote.ui.model.RatingItemModel;
import ru.mobnius.vote.ui.model.RouteItem;
import ru.mobnius.vote.utils.JsonUtil;
import ru.mobnius.vote.utils.LocationChecker;
import ru.mobnius.vote.utils.StringUtil;
import ru.mobnius.vote.utils.VersionUtil;

public class RouteListActivity extends BaseActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener,
        DialogInterface.OnClickListener,
        LocationChecker.OnLocationAvailable,
        RatingAsyncTask.OnRatingLoadedListener,
        SearchView.OnQueryTextListener {

    private DrawerLayout mDrawerLayout;
    private RecyclerView rvHouses;
    private Button btnSync;
    private TextView tvMeRating;
    private TextView tvMessage;

    private AsyncTask<Integer, Void, List<RatingItemModel>> mRatingAsyncTask;

    private ServerAppVersionAsyncTask mServerAppVersionAsyncTask;

    public static Intent getIntent(Context context) {
        return new Intent(context, RouteListActivity.class);
    }

    public RouteListActivity() {
        super(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_with_menu);

        tvMessage = findViewById(R.id.house_list_message);
        rvHouses = findViewById(R.id.house_list);
        rvHouses.setLayoutManager(new LinearLayoutManager(this));
        rvHouses.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));

        btnSync = findViewById(R.id.house_sync);
        btnSync.setOnClickListener(this);

        NavigationView navigationView = findViewById(R.id.mainMenu_NavigationView);
        MenuItem contactMenuItem = navigationView.getMenu().findItem(R.id.nav_contact);
        navigationView.setNavigationItemSelectedListener(this);
        mDrawerLayout = findViewById(R.id.mainMenuDrawerLayout);

        View headerLayout = navigationView.getHeaderView(0);
        ImageView ivIcon = headerLayout.findViewById(R.id.app_icon);
        TextView tvName = headerLayout.findViewById(R.id.app_name);

        if(Authorization.getInstance().getUser().isCandidate()) {
            tvName.setText("Кандидат");
            ivIcon.setBackgroundResource(R.mipmap.ic_candidate_launcher_round);
            contactMenuItem.setVisible(true);
        }

        TextView tvDescription = headerLayout.findViewById(R.id.app_description);
        tvMeRating = headerLayout.findViewById(R.id.app_rating);
        tvMeRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(RatingActivity.getIntent(getBaseContext()));
            }
        });
        ProfileItem profile = DataManager.getInstance().getProfile();
        if (profile != null) {
            tvDescription.setText(profile.fio);
            tvDescription.setVisibility(View.VISIBLE);
        } else {
            tvDescription.setVisibility(View.GONE);
        }
        Toolbar toolbar = findViewById(R.id.mainMenu_Toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_open_drawer_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateList(PreferencesManager.getInstance().getFilter(), null);
        if(Authorization.getInstance().getUser().isCandidate()) {
            mRatingAsyncTask = new RatingCandidateAsyncTask(this);
        } else {
            mRatingAsyncTask = new RatingAsyncTask(this);
        }
        mRatingAsyncTask.execute((Integer) null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();

        mServerAppVersionAsyncTask = new ServerAppVersionAsyncTask();
        mServerAppVersionAsyncTask.execute();

        LocationChecker.start(this);

        if(DataManager.getInstance().getDaoSession().getPointsDao().count() > 0 && !MobniusApplication.isWelcome) {
            MobniusApplication.isWelcome = true;
            StatisticDialogFragment dialogFragment = new StatisticDialogFragment();
            dialogFragment.show(getSupportFragmentManager(), "statistic");
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_sync:
                startActivity(SynchronizationActivity.getIntent(this));
                break;

            case R.id.nav_statistic:
                startActivity(StatisticActivity.getIntent(this));
                break;

            case R.id.nav_contact:
                startActivity(ContactActivity.getIntent(this));
                break;

            case R.id.nav_setting:
                startActivity(SettingActivity.getIntent(this));
                break;

            case R.id.nav_feedback:
                if(DataManager.getInstance().getProfile() != null) {
                    startActivity(FeedbackActivity.getIntent(this));
                } else {
                    alert(String.format("%s доступна только после синхронизации.", getString(R.string.feedback)));
                }
                break;

            case R.id.nav_feedback_answers:
                startActivity(NotificationActivity.getIntent(this));
                break;

            case R.id.nav_doc:
                String url = "https://1drv.ms/w/s!AnBjlQFDvsITgbtcv-7t9wMAfMWQkw?e=i5hILE";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;

            case R.id.nav_exit:
                AlertDialog.Builder adb = new AlertDialog.Builder(this);
                adb.setPositiveButton(getResources().getString(R.string.yes), this);
                adb.setNegativeButton(getResources().getString(R.string.no), this);
                AlertDialog alert = adb.create();
                alert.setTitle(getResources().getString(R.string.confirmExit));
                alert.setMessage("При следующем входе в приложение потребуется вводить логин и пароль");
                alert.show();
                break;

        }
        mDrawerLayout.closeDrawers();

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_route, menu);

        MenuItem searchItem = menu.findItem(R.id.action_route_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(this);

        MenuItem filterIcon = menu.findItem(R.id.action_route_filters);
        boolean isFilter = PreferencesManager.getInstance().getFilter();
        filterIcon.setIcon(getResources().getDrawable(isFilter ? R.drawable.ic_filter_on_24dp : R.drawable.ic_filter_off_24dp));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_route_filters) {
            boolean isFilter = !PreferencesManager.getInstance().getFilter();
            item.setIcon(getResources().getDrawable(isFilter ? R.drawable.ic_filter_on_24dp : R.drawable.ic_filter_off_24dp));
            updateList(isFilter, null);
            PreferencesManager.getInstance().setFilter(isFilter);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.MAIN;
    }

    @Override
    public void onClick(View v) {
        startActivity(SynchronizationActivity.getIntent(this));
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            ((MobniusApplication) getApplication()).unAuthorized(true);

            startActivity(LoginActivity.getIntent(this));
            finish();
        }
    }

    private void updateList(boolean isFilter, String query) {
        List<RouteItem> routes = DataManager.getInstance().getRouteItems(DataManager.RouteFilter.ALL);

        if(query != null) {
            List<RouteItem> filterRoutes = new ArrayList<>();
            query = query.toLowerCase();

            for(RouteItem routeItem : routes) {
                if(routeItem.number.toLowerCase().contains(query)) {
                    filterRoutes.add(routeItem);
                }
            }

            routes.clear();
            routes.addAll(filterRoutes);
        }

        btnSync.setVisibility(routes.size() == 0 ? View.VISIBLE : View.GONE);

        if (isFilter) {
            List<RouteItem> undoneRoutes = new ArrayList<>();
            for (RouteItem route : routes) {
                int done = DataManager.getInstance().getCountDonePoints(route.id);
                if (route.count != done) {
                    undoneRoutes.add(route);
                }
            }
            rvHouses.setAdapter(new RouteAdapter(this, undoneRoutes));

            if(routes.size() > 0 && undoneRoutes.size() == 0){
                tvMessage.setVisibility(View.VISIBLE);
            } else {
                tvMessage.setVisibility(View.GONE);
            }
        } else {
            rvHouses.setAdapter(new RouteAdapter(this, routes));
            tvMessage.setVisibility(View.GONE);
        }
    }

    private void searchResult(String query) {
        if (JsonUtil.isEmpty(query)) {
            updateList(PreferencesManager.getInstance().getFilter(), null);
        } else {
            updateList(PreferencesManager.getInstance().getFilter(), query);
        }
    }

    @Override
    public void onLocationAvailable(int mode) {
        switch (mode) {
            case LocationChecker.LOCATION_OFF:
                geoAlert("Для работы приложения необходимо включить доступ к геолокации","Включить геолокацию");
                break;
            case LocationChecker.LOCATION_ON_LOW_ACCURACY:
                geoAlert("Включен режим определения геолокации \"По спутникам GPS\". Для корректной работы приложения " +
                        "необходимо выбрать режим: \"По всем источникам\" или \"По координатам сети\"", "Изменить режим");
                break;
        }
    }

    private void geoAlert(String message, String buttonText){
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        RouteListActivity.this.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setCancelable(false)
                .show();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onRatingLoaded(List<RatingItemModel> items) {
        int idx = 0;
        long userId = Authorization.getInstance().getUser().getUserId();
        for(RatingItemModel item : items) {
            idx++;
            if(item.user_id == userId) {
                tvMeRating.setVisibility(View.VISIBLE);
                tvMeRating.setText(String.format(" %d", idx));
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mServerAppVersionAsyncTask.cancel(true);
        mServerAppVersionAsyncTask = null;

        mRatingAsyncTask.cancel(true);
        mRatingAsyncTask = null;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchResult(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(StringUtil.isEmptyOrNull(newText)) {
            searchResult(JsonUtil.EMPTY);
        }
        return false;
    }

    @SuppressLint("StaticFieldLeak")
    private class ServerAppVersionAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                return RequestManager.version(MobniusApplication.getBaseUrl());
            } catch (IOException e) {
                return "0.0.0.0";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (VersionUtil.isUpgradeVersion(getBaseContext(), s)) {
                // тут доступно новая версия
                String message = "Доступна новая версия " + s;
                MySnackBar.make(rvHouses, message, Snackbar.LENGTH_LONG).setAction("Загрузить", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = MobniusApplication.getBaseUrl() + Names.UPDATE_URL;
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                }).show();
            }
        }
    }
}
