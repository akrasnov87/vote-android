package ru.mobnius.vote.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Objects;

import ru.mobnius.vote.Names;
import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.BaseActivity;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.data.storage.models.Results;
import ru.mobnius.vote.ui.component.TextFieldView;
import ru.mobnius.vote.ui.model.PointInfo;
import ru.mobnius.vote.utils.AuditUtils;
import ru.mobnius.vote.utils.StringUtil;

/**
 * Экран вывода информации о точке
 */
public class PointInfoActivity extends BaseActivity
        implements View.OnClickListener {

    public static final int POINT_INFO_CODE = 1;

    private Button btnReset;
    private String mPointID;

    private TextFieldView tfvNotice;
    private TextFieldView tfvAddress;
    private RatingBar mRatingBar;

    private PointInfo mPointInfo;
    private List<Results> mResults;

    public static Intent newIntent(Context context, String point_id) {
        Intent intent = new Intent(context, PointInfoActivity.class);
        intent.putExtra(Names.POINT_ID, point_id);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_info);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mPointID = getIntent().getStringExtra(Names.POINT_ID);

        tfvNotice = findViewById(R.id.point_info_notice);
        tfvAddress = findViewById(R.id.point_info_address);
        mRatingBar = findViewById(R.id.point_info_rating);
        mResults = DataManager.getInstance().getPointResults(mPointID);
        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                if(mResults.size() > 0) {
                    DataManager.getInstance().updateRating(mResults.get(0).id, (int)rating);
                }
            }
        });

        btnReset = findViewById(R.id.point_info_reset);
        btnReset.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mPointInfo = DataManager.getInstance().getPointInfo(mPointID);
        if(!StringUtil.isEmptyOrNull(mPointInfo.getNotice())) {
            tfvNotice.setFieldText(mPointInfo.getNotice());
            tfvNotice.setVisibility(View.VISIBLE);
        }
        tfvAddress.setFieldText(mPointInfo.getAddress() + " кв. " + mPointInfo.getAppartament());

        boolean done = DataManager.getInstance().getPointState(mPointID).isDone();
        if (done) {
            int FINISH_DONED = 1;
            resetButtonColor(FINISH_DONED);
        } else {
            int FINISH_CREATED = 0;
            resetButtonColor(FINISH_CREATED);
        }

        if(mResults.size() > 0) {
            mRatingBar.setEnabled(mResults.get(0).n_rating != null && done);
            mRatingBar.setRating(mResults.get(0).n_rating == null ? 0 : mResults.get(0).n_rating);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.POINT_INFO;
    }

    @Override
    public void onClick(View v) {
        confirmDialog(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    AuditUtils.write(mPointID, AuditUtils.RESET_APPARTAMENT, AuditUtils.Level.HIGH);
                    DataManager.getInstance().removeVoteResult(mPointID);
                    setResult(Activity.RESULT_OK);
                    finish();
                }
            }
        });
    }

    private void resetButtonColor(int status) {
        switch (status) {
            case 0: // FINISH_CREATED
                btnReset.setText("Сбросить");
                btnReset.setEnabled(false);
                break;

            case 1: // FINISH_DONED
                btnReset.setText("Сбросить");
                btnReset.setEnabled(true);
                break;
        }
    }

    /**
     * Вывод окна сообщения
     *
     * @param listener обработчик события нажатий
     */
    private void confirmDialog(DialogInterface.OnClickListener listener) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);

        adb.setPositiveButton(getResources().getString(R.string.yes), listener);
        adb.setNegativeButton(getResources().getString(R.string.no), listener);

        AlertDialog alert = adb.create();
        alert.setTitle(String.format("Сбросить результаты обхода по квартире %s?", mPointInfo.getAppartament()));
        alert.show();
    }
}