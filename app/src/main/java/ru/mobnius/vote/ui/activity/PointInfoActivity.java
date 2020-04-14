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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import ru.mobnius.vote.Names;
import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.BaseActivity;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.ui.component.TextFieldView;
import ru.mobnius.vote.ui.model.PointInfo;

public class PointInfoActivity extends BaseActivity implements View.OnClickListener {
    private PointInfo mPointInfo;
    private Button mReset;
    private String mPointID;

    private final int FINISH_CREATED = 0;
    private final int FINISH_DONED = 1;
    public static final int POINT_INFO_CODE = 1;


    public static Intent newIntent(Context context, String id) {
        Intent intent = new Intent(context, PointInfoActivity.class);
        intent.putExtra(Names.POINT_ID, id);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_point_info);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        mPointID = getIntent().getStringExtra(Names.POINT_ID);
        mPointInfo = DataManager.getInstance().getPointInfo(mPointID);
        TextFieldView tfvNotice = findViewById(R.id.fPointInfo_tfvNotice);
        tfvNotice.setFieldText(mPointInfo.getNotice());
        TextFieldView tfvAddress = findViewById(R.id.fPointInfo_tfvAddress);
        tfvAddress.setFieldText(mPointInfo.getAddress() + " д. " + mPointInfo.getSubscrNumber());
        mReset = findViewById(R.id.fPointInfo_bReset);
        mReset.setOnClickListener(this);

        boolean done = DataManager.getInstance().getPointState(mPointID).isDone();
        if (done) {
            finishButtonColor(FINISH_DONED);
        } else {
            finishButtonColor(FINISH_CREATED);
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
        confirmDialog("Сбросить результаты обхода по квартире?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    DataManager.getInstance().removeVoteResult(mPointID);
                    //finishButtonColor(FINISH_CREATED);
                    setResult(Activity.RESULT_OK);
                    finish();
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

    /**
     * Вывод окна сообщения
     *
     * @param title    заголовок окна
     * @param listener обработчик события нажатий
     */
    private void confirmDialog(String title, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);

        adb.setPositiveButton(getResources().getString(R.string.yes), listener);
        adb.setNegativeButton(getResources().getString(R.string.no), listener);

        AlertDialog alert = adb.create();
        alert.setTitle(title);
        alert.show();
    }
}