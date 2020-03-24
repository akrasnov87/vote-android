package ru.mobnius.vote.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import java.util.Objects;

import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.ui.fragment.sort.PointSortFragment;
import ru.mobnius.vote.ui.fragment.sort.RouteSortFragment;

public class SortActivity extends SingleFragmentActivity {
    public final static String ROUTE_SORT = "ROUTE_SORT";
    public final static String POINT_SORT = "POINT_SORT";

    public final static String SORT = "SORT";

    @Override
    protected Fragment createFragment() {
        Intent intent = getIntent();
        if (getSort(intent).equals(ROUTE_SORT)) {
            return new RouteSortFragment();
        } else {
            return new PointSortFragment();
        }
    }

    /**
     *
     * @param context
     * @param sortName тип сортировки. ROUTE_SORT или POINT_SORT
     * @return
     */
    public static Intent getIntent(Context context, String sortName) {
        Intent intent = new Intent(context, SortActivity.class);
        intent.putExtra(SORT, sortName);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.ROUTE_SORT;
    }

    /**
     * Получение типа сортировки
     * @param intent
     * @return
     */
    public static String getSort(Intent intent) {
        return intent.getStringExtra(SORT);
    }
}
