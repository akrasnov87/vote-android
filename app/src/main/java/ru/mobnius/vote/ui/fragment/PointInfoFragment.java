package ru.mobnius.vote.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;

import ru.mobnius.vote.Names;
import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.BaseFragment;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.ui.component.TextFieldView;
import ru.mobnius.vote.ui.model.PointInfo;
import ru.mobnius.vote.utils.DateUtil;

public class PointInfoFragment extends BaseFragment implements View.OnClickListener {
    private PointInfo mPointInfo;
    private RecyclerView mPlombRecyclerView;

    public static PointInfoFragment newInstance(String pointId) {
        Bundle args = new Bundle();
        args.putString(Names.POINT_ID, pointId);

        PointInfoFragment pointInfoFragment = new PointInfoFragment();
        pointInfoFragment.setArguments(args);
        return pointInfoFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPointInfo = DataManager.getInstance().getPointInfo(getArguments().getString(Names.POINT_ID));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_point_info, container, false);
        TextFieldView tfvSubscrNumber = v.findViewById(R.id.fPointInfo_tfvSubscrNumber);
        TextFieldView tfvFIO = v.findViewById(R.id.fPointInfo_tfvFIO);
        TextFieldView tfvAdress = v.findViewById(R.id.fPointInfo_tfvAdress);
        TextFieldView tfvReadingsDate = v.findViewById(R.id.fPointInfo_tfvLastReadingsDate);
        TextFieldView tfvLastReadings = v.findViewById(R.id.fPointInfo_tfvLastReadingsValue);
        TextFieldView tfvSubDivision = v.findViewById(R.id.fPointInfo_tfvSubDivision);

        ImageView plombArrow = v.findViewById(R.id.fPointInfo_ivPlombArrow);
        ImageView meterInfoArrow = v.findViewById(R.id.fPointInfo_ivMeterInfoArrow);
        ImageView subscrInfoArrow = v.findViewById(R.id.fPointInfo_ivSubscrInfoArrow);

        mPlombRecyclerView = v.findViewById(R.id.fPointInfo_rvPlomb);
        mPlombRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        plombArrow.setOnClickListener(this);
        meterInfoArrow.setOnClickListener(this);
        subscrInfoArrow.setOnClickListener(this);

        tfvSubDivision.setFieldText(mPointInfo.getSubDivisionName());
        tfvSubscrNumber.setFieldText(mPointInfo.getSubscrNumber());
        tfvFIO.setFieldText(mPointInfo.getFio());
        tfvAdress.setFieldText(mPointInfo.getAddress());
        tfvReadingsDate.setFieldText(getPrevDate(mPointInfo.getMeters()));
        tfvLastReadings.setFieldText(getPrevValue(mPointInfo.getMeters()));

        return v;
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.POINT_INFO;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fPointInfo_ivPlombArrow:
                visibilityRecycler(mPlombRecyclerView);
                break;

        }
    }

    private void visibilityRecycler (RecyclerView rv){
        if (rv.isShown()) {
            rv.setVisibility(View.GONE);
        } else {
            rv.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Получение даты пред. ввода показаний
     * @param meters список показаний
     * @return дата ввода показания
     */
    private String getPrevDate(PointInfo.MeterReadingsInfo[] meters) {
        if(meters.length > 0) {
            Date datePrev = meters[0].getDatePrev();
            if(datePrev == null) {
                return "undefined";
            }
            return DateUtil.convertDateToUserString(datePrev);
        } else {
            return "undefined";
        }
    }

    /**
     * Получение пред. показаний
     * @param meters список показаний
     * @return пред. показания
     */
    private String getPrevValue(PointInfo.MeterReadingsInfo[] meters) {
        if(meters.length > 0) {
            return meters[0].prevValueToString();
        } else {
            return "undefined";
        }
    }
}
