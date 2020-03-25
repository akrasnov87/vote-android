package ru.mobnius.vote.ui.fragment.form.controlMeterReadings;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.util.Objects;

import ru.mobnius.vote.Names;
import ru.mobnius.vote.R;
import ru.mobnius.vote.data.Logger;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.DocumentManager;
import ru.mobnius.vote.data.manager.FileManager;
import ru.mobnius.vote.data.manager.GeoManager;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.data.storage.models.DaoSession;
import ru.mobnius.vote.data.storage.models.Results;
import ru.mobnius.vote.ui.component.TextFieldView;
import ru.mobnius.vote.ui.fragment.data.StringTextWatcher;
import ru.mobnius.vote.ui.fragment.form.BaseFormFragment;
import ru.mobnius.vote.ui.model.Document;
import ru.mobnius.vote.ui.fragment.data.DocumentUtil;
import ru.mobnius.vote.ui.model.Meter;

import static android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;

public class ControlMeterReadingsFragment extends BaseFormFragment
        implements View.OnClickListener {
    public static final int POINT_GALLERY_REQUEST_CODE = 1;

    private DaoSession mDaoSession;
    private GeoManager.GeoListener mGeoListener;

    private String mRouteId;
    private String mPointId;
    private String mUserPointId;
    private Long mResultTypeId;

    private RecyclerView mRecyclerView;
    private EditText mNotice;
    private ReadingsAdapter mReadingsAdapter;
    private Button btnSave;
    private DocumentManager mDocumentManager;
    private Meter[] mMeters;
    private Results mResult;


    /**
     * создание
     *
     * @param routeId      иден. маршрута, Routes
     * @param pointId      иден. точки маршрута, Points
     * @param resultTypeId иден. импа результата
     * @return
     */
    public static ControlMeterReadingsFragment createInstance(String routeId, String pointId, long resultTypeId) {
        Bundle args = new Bundle();
        args.putString(Names.POINT_ID, pointId);
        args.putString(Names.ROUTE_ID, routeId);
        args.putLong(Names.RESULT_TYPE_ID, resultTypeId);
        ControlMeterReadingsFragment controlMeterReadingsFragment = new ControlMeterReadingsFragment();
        controlMeterReadingsFragment.setArguments(args);
        return controlMeterReadingsFragment;
    }

    /**
     * обновление
     *
     * @param resultId иден. результата, Results
     * @return
     */
    public static ControlMeterReadingsFragment updateInstance(String resultId) {
        Bundle args = new Bundle();
        args.putString(Names.RESULT_ID, resultId);
        ControlMeterReadingsFragment controlMeterReadingsFragment = new ControlMeterReadingsFragment();
        controlMeterReadingsFragment.setArguments(args);
        return controlMeterReadingsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDaoSession = DataManager.getInstance().getDaoSession();
        String mNoticeText = "";
        Bundle arguments = getArguments();
        assert arguments != null;
        if (arguments.containsKey(Names.RESULT_ID)) {  // есть результат
            String resultId = getArguments().getString(Names.RESULT_ID);
            mResult = mDaoSession.getResultsDao().load(resultId);
            if (mResult != null) {
                mPointId = mResult.fn_point;
                mRouteId = mResult.fn_route;
                mUserPointId = mResult.fn_user_point;
                mResultTypeId = mResult.fn_type;
                mDocumentManager = new DocumentManager(mDaoSession, mRouteId, mPointId);
                Document document = mDocumentManager.getDocument(resultId);
                if (document != null) {
                    mNoticeText = document.getNotice();
                }
            } else {
                Logger.error(new Exception("Результат не найден."));
                return;
            }
        } else { // нужно создать результат
            mRouteId = getArguments().getString(Names.ROUTE_ID);
            mPointId = getArguments().getString(Names.POINT_ID);
            mResultTypeId = getArguments().getLong(Names.RESULT_TYPE_ID);
            mDocumentManager = new DocumentManager(mDaoSession, mRouteId, mPointId);
        }
        //setDocumentUtil(DocumentUtil.getInstance(mMeters, mNoticeText));

        Objects.requireNonNull(getActivity()).getWindow().setSoftInputMode(SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof GeoManager.GeoListener) {
            mGeoListener = (GeoManager.GeoListener) context;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_control_meter_readings, container, false);
        TextFieldView previousDate = view.findViewById(R.id.fMeterReadings_tfvPreviousDate);
        TextFieldView currentDate = view.findViewById(R.id.fMeterReadings_tfvCurrentDate);
        mNotice = view.findViewById(R.id.fMeterReadings_tietNotice);
        mRecyclerView = view.findViewById(R.id.fMeterReadings_rvReadings);
        btnSave = view.findViewById(R.id.fMeterReadings_btnSave);

        btnSave.setOnClickListener(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //currentDate.setFieldText(getCurrentDate());
        //previousDate.setFieldText(getPrevDate());
        // mNotice.setText(getDocumentUtil().getStringValue(DocumentUtil.NAME_NOTICE));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getDocumentUtil() != null) {
            mReadingsAdapter = new ReadingsAdapter(getContext(), mMeters, this, getDocumentUtil().getMeters());
            mRecyclerView.setAdapter(mReadingsAdapter);
            mNotice.addTextChangedListener(new StringTextWatcher(this, DocumentUtil.NAME_NOTICE));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        isChangedDocument(getDocumentUtil().isChanged());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fMeterReadings_btnSave:
                onSaveDocument();

                Objects.requireNonNull(getActivity()).finish();
                break;
        }
    }

    private void isChangedDocument(boolean changed) {
        btnSave.setEnabled(changed);
    }

    public void onSaveDocument() {
        if (DataManager.getInstance().isRouteFinish(mRouteId)) {
            Toast.makeText(getActivity(), "Маршрут завершен, внесение изменения запрещено.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!DataManager.getInstance().isWait(mRouteId)) {
            Toast.makeText(getActivity(), "Маршрут запрещен для изменений.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mResultTypeId <= 0) {
            Toast.makeText(getActivity(), "Тип результата не найден.", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(getActivity(), "Показания абонента сохранены", Toast.LENGTH_SHORT).show();
        String notice = mNotice.getText().toString();

        boolean isExistResult = mResult != null;
        if (isExistResult) {
            mDocumentManager.updateUserPoint(mUserPointId, null, null, null);
            mDocumentManager.updateResult(mResult.id, notice, null, false);
        } else {
            double longitude = 0;
            double latitude = 0;

            if (mGeoListener != null) {
                Location location = mGeoListener.getCurrentLocation();
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }

            mUserPointId = mDocumentManager.createUserPoint(null, null, longitude, latitude, null, false);
            mDocumentManager.createResult(mResultTypeId, mUserPointId, notice, null, false);
        }
        getActivity().finish();
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.CONTROL_METER_READINGS;
    }
}