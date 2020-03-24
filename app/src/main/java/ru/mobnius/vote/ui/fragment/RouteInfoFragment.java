package ru.mobnius.vote.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.mobnius.vote.Names;
import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.BaseFragment;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.MobniusApplication;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.data.storage.models.RouteHistory;
import ru.mobnius.vote.ui.activity.LoginActivity;
import ru.mobnius.vote.ui.component.ExpandableLayout;
import ru.mobnius.vote.ui.component.TextFieldView;
import ru.mobnius.vote.ui.model.RouteInfo;
import ru.mobnius.vote.ui.model.RouteInfoHistory;
import ru.mobnius.vote.utils.DateUtil;
import ru.mobnius.vote.utils.StringUtil;

public class RouteInfoFragment extends BaseFragment implements View.OnClickListener {

    private final int FINISH_CREATED = 0;
    private final int FINISH_DONED = 1;
    private final int FINISH_LOCKED = 2;

    private String mRouteId;
    private RouteInfo mRouteInfo;
    private TextFieldView tfvBegin;
    private TextFieldView tfvEnd;
    private TextFieldView tfvHistory;
    private TextFieldView tvNotice;
    private TextFieldView tvExtended;
    private LinearLayout llPeriod;
    private ImageView ivPeriodArrow;
    private ImageView ivHistoryArrow;
    private ExpandableLayout mExpandableLayout;
    private Button mFinish;

    private boolean period = true;
    private boolean history = true;

    public static RouteInfoFragment newInstance(String routeId) {
        Bundle args = new Bundle();
        args.putString(Names.ROUTE_ID, routeId);
        RouteInfoFragment routeInfoFragment = new RouteInfoFragment();
        routeInfoFragment.setArguments(args);
        return routeInfoFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;

        mRouteId = getArguments().getString(Names.ROUTE_ID);
        mRouteInfo = DataManager.getInstance().getRouteInfo(mRouteId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_route_info, container, false);
        String dateFormat = "dd.MM.yyyy";
        tfvBegin = v.findViewById(R.id.fRouteInfo_tfvBegin);
        tfvEnd = v.findViewById(R.id.fRouteInfo_tfvEnd);
        tfvHistory = v.findViewById(R.id.fRouteInfo_tvStatus);
        tvExtended = v.findViewById(R.id.fRouteInfo_tfvExtended);
        tvNotice = v.findViewById(R.id.fRouteInfo_tfvNotice);
        ivPeriodArrow = v.findViewById(R.id.fRouteInfo_ivPeriodArrow);
        ivHistoryArrow = v.findViewById(R.id.fRouteInfo_ivHistoryArrow);
        llPeriod = v.findViewById(R.id.fRouteInfo_llPeriod);
        mExpandableLayout = v.findViewById(R.id.fRouteInfo_rvExpandableLayout);
        mFinish = v.findViewById(R.id.fRouteInfo_bFinish);

        ivPeriodArrow.setOnClickListener(this);
        ivHistoryArrow.setOnClickListener(this);
        mFinish.setOnClickListener(this);

        tfvBegin.setFieldText(DateUtil.convertDateToUserString(mRouteInfo.getDateStart(), dateFormat));
        tfvEnd.setFieldText(DateUtil.convertDateToUserString(mRouteInfo.getDateEnd(), dateFormat));
        tfvHistory.setFieldText(getStatus(mRouteInfo.getHistories()));
        mExpandableLayout.setCustomRecLayManager(new LinearLayoutManager(getActivity()));
        mExpandableLayout.setCustomRecAdapter(new RouteInfoAdapter());
        return v;
    }

    private String getStatus(RouteInfoHistory[] routeInfoHistory) {
        StringBuilder builder = new StringBuilder();
        // TODO: 17/01/2020 Можно переделать на HTML
        for (int i = 0; i < routeInfoHistory.length; i++) {
            builder.append(routeInfoHistory[i].getStatus())
                    .append(" - ")
                    .append(DateUtil.convertDateToUserString(routeInfoHistory[i].getDate()));

            if(i != routeInfoHistory.length - 1) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.ROUTE_INFO;
    }

    @Override
    public void onStart() {
        super.onStart();

        DataManager dataManager = DataManager.getInstance();

        if(dataManager.isRouteFinish(mRouteId)) {
            if(!dataManager.isRevertRouteFinish(mRouteId)) {
                finishButtonColor(FINISH_LOCKED);
            } else {
                finishButtonColor(FINISH_DONED);
            }
        } else {
            finishButtonColor(FINISH_CREATED);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fRouteInfo_ivPeriodArrow:
                if (period) {
                    if (mRouteInfo.isExtended()) {
                        String extended = DateUtil.convertDateToUserString(mRouteInfo.getDateExtended(), "dd.MM.yyyy");
                        tvExtended.setFieldText(extended);
                        tvExtended.setVisibility(View.VISIBLE);
                    }

                    if (!StringUtil.isEmptyOrNull(mRouteInfo.getNotice())) {
                        tvNotice.setFieldText(mRouteInfo.getNotice());
                        tvNotice.setVisibility(View.VISIBLE);
                    }

                    llPeriod.setVisibility(View.VISIBLE);
                } else {
                    llPeriod.setVisibility(View.GONE);
                }
                period = !period;
                break;

            case R.id.fRouteInfo_ivHistoryArrow:
                if (history) {
                    tfvHistory.setVisibility(View.VISIBLE);
                } else {
                    tfvHistory.setVisibility(View.GONE);
                }
                history = !history;
                break;

            case R.id.fRouteInfo_bFinish:
                // тут нужно
                final DataManager dataManager = DataManager.getInstance();
                if(dataManager.isRouteFinish(mRouteId)) {
                    if(dataManager.isRevertRouteFinish(mRouteId)) {
                        // тут можно возобновить
                        confirmDialog(getString(R.string.revert_route_title), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which == DialogInterface.BUTTON_POSITIVE) {
                                    finishButtonColor(FINISH_CREATED);
                                    dataManager.revertRouteFinish(mRouteId);
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getContext(), "Маршрут завершен. Возобновление невозможно.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    confirmDialog(getString(R.string.finish_route_title), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(which == DialogInterface.BUTTON_POSITIVE) {
                                finishButtonColor(FINISH_DONED);
                                dataManager.setRouteFinish(mRouteId);
                            }
                        }
                    });
                }
                break;
        }
    }

    /**
     * Вывод окна сообщения
     * @param title заголовок окна
     * @param listener обработчик события нажатий
     */
    private void confirmDialog(String title, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getContext());

        adb.setPositiveButton(getResources().getString(R.string.yes), listener);
        adb.setNegativeButton(getResources().getString(R.string.no), listener);

        AlertDialog alert = adb.create();
        alert.setTitle(title);
        alert.show();
    }

    private void finishButtonColor(int status) {
        //mFinish.setEnabled(true);

        switch (status) {
            case 0: // FINISH_CREATED
                mFinish.setBackgroundColor(getResources().getColor(R.color.colorFail));
                mFinish.setText("Завершить");
                break;

            case 1: // FINISH_DONED
                mFinish.setBackgroundColor(getResources().getColor(R.color.colorSuccess));
                mFinish.setText("Возобновить");
                break;

            case 2: // FINISH_LOCKED
                mFinish.setBackgroundColor(getResources().getColor(R.color.colorSuccess));
                //mFinish.setEnabled(false);
                mFinish.setText("Завершено");
                break;
        }
    }

    private class RouteInfoHolder extends RecyclerView.ViewHolder{
        private TextView tvState;
        private TextView tvDate;

        RouteInfoHolder(@NonNull View itemView) {
            super(itemView);
            tvState = itemView.findViewById(android.R.id.text1);
            tvDate = itemView.findViewById(android.R.id.text2);
        }

        void bindPoint(RouteInfoHistory routeHistory) {
            tvState.setText(routeHistory.getStatus());
            tvDate.setText(DateUtil.convertDateToUserString(routeHistory.getDate()));
        }
    }

    private class RouteInfoAdapter extends RecyclerView.Adapter<RouteInfoHolder> {
        @NonNull
        @Override
        public RouteInfoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(android.R.layout.simple_list_item_2, parent, false);
            return new RouteInfoHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RouteInfoHolder holder, int position) {
            holder.bindPoint(mRouteInfo.getHistories()[position]);
        }

        @Override
        public int getItemCount() {
            return mRouteInfo.getHistories().length;
        }
    }
}
