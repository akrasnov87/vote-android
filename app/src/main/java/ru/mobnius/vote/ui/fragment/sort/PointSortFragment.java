package ru.mobnius.vote.ui.fragment.sort;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import ru.mobnius.vote.ui.data.PointSortManager;
import ru.mobnius.vote.ui.model.SortItem;
import ru.mobnius.vote.utils.DateUtil;
import ru.mobnius.vote.utils.JsonUtil;

public class PointSortFragment extends BaseFragment implements View.OnClickListener {
    private static final String ADDRESS = "address";

    private TextView tvTitle;
    private AppCompatSpinner spAddress;
    private SortAdapter mSortAdapter;
    private String sortPrefs;

    private PreferencesManager mPreferencesManager;
    private PointSortManager mPointSortManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSortAdapter = new SortAdapter(getContext(), new ArrayList<Map<String, Object>>(), true);
        mPreferencesManager = PreferencesManager.getInstance();
        sortPrefs = PreferencesManager.POINT_SORT_PREFS;
        String filter = mPreferencesManager.getSort(sortPrefs);
        mPointSortManager = new PointSortManager(sortPrefs, filter);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_point_sort, container, false);

        tvTitle = v.findViewById(R.id.fPointSort_tvTitle);

        spAddress = v.findViewById(R.id.fPointSort_spPointAddress);

        Toolbar toolbar = v.findViewById(R.id.fPointSort_tMenu);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(this);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPointSortManager.getItems().length > 0) {
            setPrevValues();
            try {
                String sortDate = "Сортировка установлена " + DateUtil.convertDateToUserString(mPointSortManager.getDate(), DateUtil.USER_SHORT_FORMAT);
                tvTitle.setText(sortDate);
            } catch (ParseException e) {
                Logger.error(e);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        spAddress.setAdapter(mSortAdapter);
    }

    @Override
    public void onClick(View v) {
        changeSort(mPointSortManager, spAddress.getSelectedItemPosition());
        String json = mPointSortManager.serialize();
        if (mPointSortManager.getItems().length > 0) {
            mPreferencesManager.setSort(sortPrefs, json);
        } else {
            mPreferencesManager.setSort(sortPrefs, JsonUtil.EMPTY);
        }
        Objects.requireNonNull(getActivity()).finish();
    }

    private void setPrevValues() {
        int x = (mPointSortManager.getItems()[0].getType());
        spAddress.setSelection(x);
    }

    private void changeSort(PointSortManager manager, int type) {
        boolean delete = false;
        if (type == 0) {
            delete = true;
        }
        if (delete) {
            manager.removeItem(manager.getItem(PointSortFragment.ADDRESS));
        } else {
            if (manager.getItem(PointSortFragment.ADDRESS) == null) {
                manager.addItem(new SortItem(PointSortFragment.ADDRESS, type));
            } else {
                manager.updateItem(PointSortFragment.ADDRESS, type);
            }
        }
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.POINT_SORT;
    }
}