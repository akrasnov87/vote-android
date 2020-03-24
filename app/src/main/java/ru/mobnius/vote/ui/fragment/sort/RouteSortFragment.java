package ru.mobnius.vote.ui.fragment.sort;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import ru.mobnius.vote.R;
import ru.mobnius.vote.data.Logger;
import ru.mobnius.vote.data.manager.BaseFragment;
import ru.mobnius.vote.data.manager.configuration.PreferencesManager;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.ui.data.RouteSortManager;
import ru.mobnius.vote.ui.model.SortItem;
import ru.mobnius.vote.utils.DateUtil;
import ru.mobnius.vote.utils.JsonUtil;

public class RouteSortFragment extends BaseFragment implements View.OnClickListener {
    private static final String START_DATE = "dateStart";
    private static final String END_DATE = "dateEnd";

    private TextView tvTitle;
    private AppCompatSpinner spStartDate;
    private AppCompatSpinner spEndDate;

    private SortAdapter mStartDateAdapter;
    private SortAdapter mEndDateAdapter;

    private PreferencesManager mPreferencesManager;
    private RouteSortManager mRouteSortManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStartDateAdapter = new SortAdapter(getContext(), new ArrayList<Map<String, Object>>(), true);
        mEndDateAdapter = new SortAdapter(getContext(), new ArrayList<Map<String, Object>>(), false);
        mPreferencesManager = PreferencesManager.getInstance();
        String filter = mPreferencesManager.getSort(PreferencesManager.ROUTE_SORT_PREFS);
        mRouteSortManager = new RouteSortManager(PreferencesManager.ROUTE_SORT_PREFS, filter);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_route_sort, container, false);

        tvTitle = v.findViewById(R.id.fRouteSort_tvTitle);

        spStartDate = v.findViewById(R.id.fRouteSort_spRouteStarts);
        spEndDate = v.findViewById(R.id.fRouteSort_spRouteEnds);

        Toolbar toolbar = v.findViewById(R.id.fRouteSort_tMenu);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(this);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mRouteSortManager.getItems().length > 0) {
            setPrevValues();
            try {
                String sortDate = "Сортировка установлена " + DateUtil.convertDateToUserString(mRouteSortManager.getDate(), DateUtil.USER_SHORT_FORMAT);
                tvTitle.setText(sortDate);
            } catch (ParseException e) {
                Logger.error(e);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        spStartDate.setAdapter(mStartDateAdapter);
        spEndDate.setAdapter(mEndDateAdapter);
    }

    @Override
    public void onClick(View v) {
        changeSort(mRouteSortManager, START_DATE, spStartDate.getSelectedItemPosition());
        changeSort(mRouteSortManager, END_DATE, spEndDate.getSelectedItemPosition());
        String json = mRouteSortManager.serialize();
        if (mRouteSortManager.getItems().length > 0) {
            mPreferencesManager.setSort(PreferencesManager.ROUTE_SORT_PREFS, json);
        } else {
            mPreferencesManager.setSort(PreferencesManager.ROUTE_SORT_PREFS, JsonUtil.EMPTY);
        }
        Objects.requireNonNull(getActivity()).finish();
    }

    private void setPrevValues() {
        for (SortItem item : mRouteSortManager.getItems()) {
            switch (item.getName()) {
                case START_DATE:
                    spStartDate.setSelection(item.getType());
                    break;
                case END_DATE:
                    int position = (item.getType());
                    spEndDate.setSelection(position);
                    break;
            }
        }
    }

    private void changeSort(RouteSortManager manager, String key, int type) {
        boolean delete = false;
        if (type == 0) {
            delete = true;
        }
        if (delete) {
            manager.removeItem(manager.getItem(key));
        } else {
            if (manager.getItem(key) == null) {
                manager.addItem(new SortItem(key, type));
            } else {
                manager.updateItem(key, type);
            }
        }
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.ROUTE_SORT;
    }
}
