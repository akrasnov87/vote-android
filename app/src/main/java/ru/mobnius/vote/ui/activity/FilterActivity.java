package ru.mobnius.vote.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import java.util.Objects;

import ru.mobnius.vote.Names;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.ui.fragment.filter.PointFilterFragment;
import ru.mobnius.vote.ui.fragment.filter.RouteFilterFragment;

public class FilterActivity extends SingleFragmentActivity {

    public final static String ROUTE_FILTER = "ROUTE_FILTER";
    public final static String POINT_FILTER = "POINT_FILTER";
    private final static String FILTER = "FILTER";

    /**
     *
     * @param context
     * @param filterName тип фильтра. ROUTE_FILTER или POINT_FILTER
     * @return
     */
    public static Intent getIntent(Context context, String filterName) {
        Intent intent = new Intent(context, FilterActivity.class);
        intent.putExtra(FILTER, filterName);
        return intent;
    }

    /**
     * Получение типа фильтра
     * @param intent
     * @return
     */
    public static String getFilter(Intent intent) {
        return intent.getStringExtra(FILTER);
    }

    @Override
    protected Fragment createFragment() {
        Intent intent = getIntent();
        if (getFilter(intent).equals(ROUTE_FILTER)) {
            return new RouteFilterFragment();
        } else {
            return new PointFilterFragment();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.FILTER;
    }
}
