package ru.mobnius.vote.ui.fragment.filter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import ru.mobnius.vote.data.manager.configuration.ConfigurationSetting;
import ru.mobnius.vote.data.manager.configuration.PreferencesManager;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.ui.data.PointFilterManager;
import ru.mobnius.vote.ui.model.FilterItem;
import ru.mobnius.vote.utils.DateUtil;
import ru.mobnius.vote.utils.IntUtil;
import ru.mobnius.vote.utils.JsonUtil;

public class PointFilterFragment extends BaseFragment implements MenuItem.OnMenuItemClickListener, View.OnClickListener {

    private final String STATUS_ID = "done";
    private final String DEVICE_NUMBER = "deviceNumber";
    private final String SUBSCR_NUMBER = "subscrNumber";
    private final String ADDRESS = "address";

    private EditText etMeterFilter;
    private EditText etSubscrFilter;
    private EditText etAddressFilter;
    private TextView tvTitle;
    private AppCompatSpinner spPointStatus;

    private PointFilterManager mPointFilterManager;
    private PreferencesManager mPreferencesManager;
    private PointStatusAdapter mStatusAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferencesManager = PreferencesManager.getInstance();
        mStatusAdapter = new PointStatusAdapter(getContext(), new ArrayList<Map<String, Object>>());
        String filter = mPreferencesManager.getFilter(PreferencesManager.POINT_FILTER_PREFS);
        mPointFilterManager = new PointFilterManager(PreferencesManager.POINT_FILTER_PREFS, filter);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_point_filter, container, false);

        tvTitle = v.findViewById(R.id.fPointFilter_tvTitle);

        etMeterFilter = v.findViewById(R.id.fPointFilter_etMeterName);
        etSubscrFilter = v.findViewById(R.id.fPointFilter_etSubscrName);
        etAddressFilter = v.findViewById(R.id.fPointFilter_etAddress);

        spPointStatus = v.findViewById(R.id.fPointFilter_spPointStatus);

        Toolbar toolbar = v.findViewById(R.id.fPointFilter_tMenu);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.inflateMenu(R.menu.filter_menu);
        toolbar.getMenu().findItem(R.id.filterMenu_Cancel).setOnMenuItemClickListener(this);
        toolbar.setNavigationOnClickListener(this);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        spPointStatus.setAdapter(mStatusAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mPointFilterManager.getItems().length > 0) {
            setPreviousValues();
            try {
                setTitle("Фильтр установлен " + DateUtil.convertDateToUserString(mPointFilterManager.getDate(), DateUtil.USER_SHORT_FORMAT));
            } catch (ParseException e) {
                Logger.error(e);
            }
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.filterMenu_Cancel) {
            etMeterFilter.setText("");
            etSubscrFilter.setText("");
            etAddressFilter.setText("");
            spPointStatus.setSelection(0);
            setTitle(getString(R.string.point_filters));
            Toast.makeText(getActivity(), "Фильтры сброшены", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private void changeFilter(String key, String type, Object value) {
        boolean delete = false;
        String valueStr = null;

        switch (type) {
            case ConfigurationSetting.BOOLEAN:
                int i = IntUtil.convertToInt(value);
                if (i == 0) {
                    delete = true;
                } else {
                    valueStr = String.valueOf(i == 1);
                }
                break;

            case ConfigurationSetting.TEXT:
                if (value.toString().isEmpty()) {
                    delete = true;
                } else {
                    valueStr = String.valueOf(value);
                }
                break;
        }

        if (delete) {
            mPointFilterManager.removeItem(mPointFilterManager.getItem(key));
        } else {
            if (mPointFilterManager.getItem(key) == null) {
                mPointFilterManager.addItem(new FilterItem(key, type, valueStr));
            } else {
                mPointFilterManager.updateItem(key, valueStr);
            }
        }
    }

    private void setPreviousValues() {
        for (FilterItem item : mPointFilterManager.getItems()) {
            switch (item.getName()) {
                case DEVICE_NUMBER:
                    etMeterFilter.setText(item.getValue());
                    break;
                case SUBSCR_NUMBER:
                    etSubscrFilter.setText(item.getValue());
                    break;
                case ADDRESS:
                    etAddressFilter.setText(item.getValue());
                    break;
                case STATUS_ID:
                    int selection = 2;
                    if (Boolean.parseBoolean(item.getValue())) {
                        selection = 1;
                    }
                    spPointStatus.setSelection(selection);
                    break;
            }
        }
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.FILTER;
    }

    @Override
    public void onClick(View v) {
        String meterFilter = etMeterFilter.getText().toString();
        String subscrFilter = etSubscrFilter.getText().toString();
        String addressFilter = etAddressFilter.getText().toString();
        long statusId = mStatusAdapter.getId(spPointStatus.getSelectedItemPosition());


        changeFilter(DEVICE_NUMBER, ConfigurationSetting.TEXT, meterFilter);
        changeFilter(SUBSCR_NUMBER, ConfigurationSetting.TEXT, subscrFilter);
        changeFilter(ADDRESS, ConfigurationSetting.TEXT, addressFilter);
        changeFilter(STATUS_ID, ConfigurationSetting.BOOLEAN, statusId);

        String preferencesType = PreferencesManager.POINT_FILTER_PREFS;
        String json = mPointFilterManager.serialize();
        if (mPointFilterManager.getItems().length > 0) {
                mPreferencesManager.setFilter(preferencesType, json);
            } else {
                mPreferencesManager.setFilter(preferencesType, JsonUtil.EMPTY);
            }
        Objects.requireNonNull(getActivity()).finish();
    }

    private void setTitle(String title) {
        tvTitle.setText(title);
    }
}