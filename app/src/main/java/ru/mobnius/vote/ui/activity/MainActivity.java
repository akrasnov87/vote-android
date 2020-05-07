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
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.MobniusApplication;
import ru.mobnius.vote.data.manager.configuration.PreferencesManager;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.ui.fragment.RouteFragment;
import ru.mobnius.vote.ui.model.PointFilter;
import ru.mobnius.vote.ui.model.RouteItem;

public class MainActivity extends SingleFragmentActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    private RouteFragment mFragment;

    public MainActivity() {
        super(true);
    }

    @Override
    protected Fragment createFragment() {
        mFragment = new RouteFragment();
        return mFragment;
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

        Toolbar toolbar = findViewById(R.id.mainMenu_Toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_open_drawer_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
        Objects.requireNonNull(getSupportActionBar()).setTitle("Дома");
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Intent intent;
        switch (menuItem.getItemId()) {
            case R.id.navigationDrawerSynchronization:
                intent = new Intent(this, SynchronizationActivity.class);
                startActivity(intent);
                break;

            case R.id.navigationDrawerStatistic:
                intent = new Intent(this, StatisticActivity.class);
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
        inflater.inflate(R.menu.menu_route, menu);
        MenuItem filterIcon = menu.findItem(R.id.route_setFilters);

        boolean isFilter = PreferencesManager.getInstance().isUndoneRoutes();

        filterIcon.setIcon(getDrawable(isFilter ? R.drawable.ic_filter_on_24dp:R.drawable.ic_filter_off_24dp ));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.route_setFilters:
                boolean isFilter = PreferencesManager.getInstance().isUndoneRoutes();

                item.setIcon(getDrawable(isFilter ? R.drawable.ic_filter_off_24dp: R.drawable.ic_filter_on_24dp));
               mFragment.invalidateList();
               break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public int getExceptionCode() {
        return IExceptionCode.MAIN;
    }

}
