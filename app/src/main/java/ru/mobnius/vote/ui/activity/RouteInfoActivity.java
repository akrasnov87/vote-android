package ru.mobnius.vote.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import ru.mobnius.vote.Names;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.ui.fragment.PointFragment;
import ru.mobnius.vote.ui.fragment.RouteInfoFragment;

public class RouteInfoActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return RouteInfoFragment.newInstance(getIntent().getStringExtra(Names.ROUTE_ID));
    }

    public static Intent newIntent(Context context, String id) {
        Intent intent = new Intent(context, RouteInfoActivity.class);
        intent.putExtra(Names.ROUTE_ID, id);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.ROUTE_INFO;
    }
}
