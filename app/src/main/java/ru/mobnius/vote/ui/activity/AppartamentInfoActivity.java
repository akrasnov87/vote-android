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

import java.util.Objects;

import ru.mobnius.vote.Names;
import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.BaseActivity;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.ui.component.TextFieldView;
import ru.mobnius.vote.ui.model.PointInfo;

/**
 * Экран вывода информации о точке
 */
public class AppartamentInfoActivity extends BaseActivity
        implements View.OnClickListener {

    public static final int POINT_INFO_CODE = 1;

    private Button btnReset;
    private String mPointID;

    private TextFieldView tfvNotice;
    private TextFieldView tfvAddress;

    public static Intent newIntent(Context context, String id) {
        Intent intent = new Intent(context, AppartamentInfoActivity.class);
        intent.putExtra(Names.POINT_ID, id);
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

        btnReset = findViewById(R.id.point_info_reset);
        btnReset.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        PointInfo pointInfo = DataManager.getInstance().getPointInfo(mPointID);

        tfvNotice.setFieldText(pointInfo.getNotice());
        tfvAddress.setFieldText(pointInfo.getAddress() + " д. " + pointInfo.getSubscrNumber());

        boolean done = DataManager.getInstance().getPointState(mPointID).isDone();
        if (done) {
            int FINISH_DONED = 1;
            resetButtonColor(FINISH_DONED);
        } else {
            int FINISH_CREATED = 0;
            resetButtonColor(FINISH_CREATED);
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
                btnReset.setBackgroundColor(getResources().getColor(R.color.disabled_color));
                btnReset.setText("Сбросить");
                btnReset.setEnabled(false);
                break;

            case 1: // FINISH_DONED
                btnReset.setBackgroundColor(getResources().getColor(R.color.colorFail));
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
        alert.setTitle("Сбросить результаты обхода по квартире?");
        alert.show();
    }
}