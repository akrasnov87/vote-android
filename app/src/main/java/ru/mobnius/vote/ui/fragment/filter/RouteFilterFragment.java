package ru.mobnius.vote.ui.fragment.filter;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Objects;

import ru.mobnius.vote.R;
import ru.mobnius.vote.data.Logger;
import ru.mobnius.vote.data.manager.BaseFragment;
import ru.mobnius.vote.data.manager.FilterManager;
import ru.mobnius.vote.data.manager.configuration.ConfigurationSetting;
import ru.mobnius.vote.data.manager.configuration.PreferencesManager;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.ui.activity.MainActivity;
import ru.mobnius.vote.ui.data.RouteFilterManager;
import ru.mobnius.vote.ui.model.FilterItem;
import ru.mobnius.vote.utils.DateUtil;
import ru.mobnius.vote.utils.IntUtil;
import ru.mobnius.vote.utils.JsonUtil;
import ru.mobnius.vote.utils.LongUtil;

public class RouteFilterFragment extends BaseFragment implements View.OnClickListener, MenuItem.OnMenuItemClickListener {

    private final String STATUS_ID = "statusId";
    private final String TYPE_ID = "typeId";
    private final String DATE = "date";

    private RouteStatusAdapter mRouteStatusAdapter;
    private RouteTypeAdapter mRouteTypeAdapter;

    private AppCompatSpinner spStatuses;
    private AppCompatSpinner spTypes;
    private TextView tvDate;
    private TextView tvTitle;
    private ImageView ivCancel;

    private RouteFilterManager mRouteFilterManager;
    private PreferencesManager mPreferencesManager;

    private Date mDateFilter = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mPreferencesManager = PreferencesManager.getInstance();
        String filter = mPreferencesManager.getFilter(PreferencesManager.ROUTE_FILTER_PREFS);
        mRouteFilterManager = new RouteFilterManager(PreferencesManager.ROUTE_FILTER_PREFS, filter);
        mRouteStatusAdapter = new RouteStatusAdapter(getContext(), new ArrayList<Map<String, Object>>());
        mRouteTypeAdapter = new RouteTypeAdapter(getContext(), new ArrayList<Map<String, Object>>());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_route_filter, container, false);

        Toolbar toolbar = v.findViewById(R.id.fRouteFilter_tMenu);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.inflateMenu(R.menu.filter_menu);

        spStatuses = v.findViewById(R.id.fRouteFilter_spRouteStatus);
        spTypes = v.findViewById(R.id.fRouteFilter_spRouteTypes);
        tvDate = v.findViewById(R.id.fRouteFilter_tvRouteDate);
        tvTitle = v.findViewById(R.id.fRouteFilter_tvTitle);
        ivCancel = v.findViewById(R.id.fRouteFilter_ivCancel);

        toolbar.getMenu().findItem(R.id.filterMenu_Cancel).setOnMenuItemClickListener(this);
        tvDate.setOnClickListener(this);
        ivCancel.setOnClickListener(this);
        toolbar.setNavigationOnClickListener(this);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        spStatuses.setAdapter(mRouteStatusAdapter);
        spTypes.setAdapter(mRouteTypeAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mRouteFilterManager.getItems().length > 0) {
            setPrevValues();

            try {
                setTitle("Фильтр установлен " + DateUtil.convertDateToUserString(mRouteFilterManager.getDate(), DateUtil.USER_FORMAT));
            } catch (ParseException e) {
                Logger.error(e);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fRouteFilter_tvRouteDate:
                Calendar calendar = Calendar.getInstance();
                if(mDateFilter != null) {
                    calendar.setTime(mDateFilter);
                } else {
                    calendar.setTime(new Date());
                }
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getContext()), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        mDateFilter = new GregorianCalendar(year, month, day).getTime();
                        setDateFilter(mDateFilter);
                        ivCancel.setVisibility(View.VISIBLE);
                    }
                }, year, month, day);

                datePickerDialog.show();
            break;

            // кнопка назад
            case -1:
                long statusId = mRouteStatusAdapter.getId(spStatuses.getSelectedItemPosition());
                changeFilter(mRouteFilterManager, STATUS_ID, ConfigurationSetting.INTEGER, statusId);

                long typeId = mRouteTypeAdapter.getId(spTypes.getSelectedItemPosition());
                changeFilter(mRouteFilterManager, TYPE_ID, ConfigurationSetting.INTEGER, typeId);

                changeFilter(mRouteFilterManager, DATE, ConfigurationSetting.DATE, mDateFilter);

                String json = mRouteFilterManager.serialize();
                if(mRouteFilterManager.getItems().length > 0) {
                    mPreferencesManager.setFilter(PreferencesManager.ROUTE_FILTER_PREFS, json);
                } else {
                    mPreferencesManager.setFilter(PreferencesManager.ROUTE_FILTER_PREFS, JsonUtil.EMPTY);
                }
                Objects.requireNonNull(getActivity()).finish();
                break;

            case R.id.fRouteFilter_ivCancel:
                ivCancel.setVisibility(View.GONE);
                tvDate.setText(null);
                mDateFilter = null;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.filterMenu_Cancel) {
            setDateFilter(null);
            spStatuses.setSelection(0);
            spTypes.setSelection(0);
            setTitle(getString(R.string.route_filters));
        }
        return true;
    }

    private void setPrevValues() {
        for (FilterItem item : mRouteFilterManager.getItems()) {
            switch (item.getName()) {
                case STATUS_ID:
                    spStatuses.setSelection(mRouteStatusAdapter.getPositionById(LongUtil.convertToLong(item.getValue())));
                    break;
                case TYPE_ID:
                    spTypes.setSelection(mRouteTypeAdapter.getPositionById(LongUtil.convertToLong(item.getValue())));
                    break;
                case DATE:
                    try {
                        setDateFilter(DateUtil.convertStringToDate(item.getValue()));
                    } catch (ParseException e) {
                        Logger.error(e);
                    }
                    break;
            }
        }
    }

    private void changeFilter(FilterManager manager, String key, String type, Object value) {
        boolean delete = false;
        String valueStr = null;

        switch (type) {
            case ConfigurationSetting.INTEGER:
                int i = IntUtil.convertToInt(value);
                if(i < 0) {
                    delete = true;
                } else {
                    valueStr = String.valueOf(value);
                }
                break;

            case ConfigurationSetting.DATE:
                if(value == null) {
                    delete = true;
                } else {
                    valueStr = DateUtil.convertDateToString((Date)value);
                }
                break;
        }

        if(delete) {
            manager.removeItem(manager.getItem(key));
        } else {
            if (manager.getItem(key) == null) {
                manager.addItem(new FilterItem(key, type, valueStr));
            } else {
                manager.updateItem(key, valueStr);
            }
        }
    }

    private void setTitle(String title) {
        tvTitle.setText(title);
    }

    private void setDateFilter(Date date) {
        mDateFilter = date;
        if(date == null) {
            tvDate.setText("");
        } else {
            tvDate.setText(DateUtil.convertDateToUserString(date, DateUtil.USER_SHORT_FORMAT));
        }
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.FILTER;
    }
}
