package ru.mobnius.vote.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ru.mobnius.vote.Names;
import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.BaseFragment;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.ui.component.TextFieldView;
import ru.mobnius.vote.ui.model.PointInfo;

public class PointInfoFragment extends BaseFragment implements View.OnClickListener {
    private PointInfo mPointInfo;
    private Button mReset;
    private String mPointID;

    private final int FINISH_CREATED = 0;
    private final int FINISH_DONED = 1;

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
        assert getArguments() != null;
        mPointID = getArguments().getString(Names.POINT_ID);
        mPointInfo = DataManager.getInstance().getPointInfo(mPointID);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_point_info, container, false);
        TextFieldView tfvNotice = v.findViewById(R.id.fPointInfo_tfvNotice);
        tfvNotice.setFieldText(mPointInfo.getNotice());
        TextFieldView tfvAddress = v.findViewById(R.id.fPointInfo_tfvAddress);
        tfvAddress.setFieldText(mPointInfo.getAddress() + " д. " + mPointInfo.getSubscrNumber());
        mReset = v.findViewById(R.id.fPointInfo_bReset);
        mReset.setOnClickListener(this);

        boolean done = DataManager.getInstance().getPointState(mPointID).isDone();
        if(done) {
            finishButtonColor(FINISH_DONED);
        } else {
            finishButtonColor(FINISH_CREATED);
        }
        return v;
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.POINT_INFO;
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

    @Override
    public void onClick(View v) {
        confirmDialog("Сбросить результаты обхода по квартире?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            if(which == DialogInterface.BUTTON_POSITIVE) {
                DataManager.getInstance().removeVoteResult(mPointID);
                //finishButtonColor(FINISH_CREATED);
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            }
            }
        });
    }

    private void finishButtonColor(int status) {
        switch (status) {
            case 0: // FINISH_CREATED
                mReset.setBackgroundColor(getResources().getColor(R.color.disabled_color));
                mReset.setText("Сбросить");
                mReset.setEnabled(false);
                break;

            case 1: // FINISH_DONED
                mReset.setBackgroundColor(getResources().getColor(R.color.colorFail));
                mReset.setText("Сбросить");
                mReset.setEnabled(true);
                break;
        }
    }
}
