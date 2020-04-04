package ru.mobnius.vote.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class PointInfoFragment extends BaseFragment {
    private PointInfo mPointInfo;

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
        TextFieldView tfvNotice = v.findViewById(R.id.fPointInfo_tfvNotice);
        tfvNotice.setFieldText(mPointInfo.getNotice());
        TextFieldView tfvAddress = v.findViewById(R.id.fPointInfo_tfvAddress);
        tfvAddress.setFieldText(mPointInfo.getAddress() + " д. " + mPointInfo.getSubscrNumber());

        return v;
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.POINT_INFO;
    }
}
