package ru.mobnius.vote.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ru.mobnius.vote.Names;
import ru.mobnius.vote.R;
import ru.mobnius.vote.data.Logger;
import ru.mobnius.vote.data.manager.BaseActivity;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.authorization.Authorization;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.data.storage.models.FeedbackTypes;
import ru.mobnius.vote.data.storage.models.FeedbackTypesDao;
import ru.mobnius.vote.ui.adapter.FeedbackTypeAdapter;
import ru.mobnius.vote.ui.component.TextFieldView;
import ru.mobnius.vote.utils.DateUtil;
import ru.mobnius.vote.utils.HardwareUtil;

/**
 * Форма обратной связи
 */
public class FeedbackActivity extends BaseActivity
        implements TextWatcher, AdapterView.OnItemSelectedListener {

    public static String QUESTION = "QUESTION";
    public static String NO_DATA = "NO_DATA";
    public static String EXCESS_DATA = "EXCESS_DATA";
    public static String CHANGE_NUMBER = "CHANGE_APPARTAMENT_NUMBER";
    public static String CHANGE_HOUSE_NUMBER = "CHANGE_HOUSE_NUMBER";

    public static String TYPE = "type";
    public static String DATA = "data";

    public static Intent getIntent(Context context) {
        return new Intent(context, FeedbackActivity.class);
    }

    /**
     *
     * @param type тип вопроса
     * @param data дополнпительные данные
     */
    public static Intent getIntent(Context context, String type, String data) {
        Intent intent = new Intent(context, FeedbackActivity.class);
        intent.putExtra(TYPE, type);
        intent.putExtra(DATA, data);
        return intent;
    }

    private MenuItem miSend;

    private AppCompatSpinner sType;
    private EditText etMessage;
    private TextFieldView tfvSerial;
    private TextFieldView tfvUser;
    private TextFieldView tfvDate;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        sType = findViewById(R.id.feedback_item_type);
        tfvSerial = findViewById(R.id.feedback_item_serial);
        tfvUser = findViewById(R.id.feedback_item_user);
        tfvDate = findViewById(R.id.feedback_item_date);

        etMessage = findViewById(R.id.feedback_item_message);
        etMessage.addTextChangedListener(this);

        FeedbackTypeAdapter feedbackTypeAdapter = new FeedbackTypeAdapter(this, new ArrayList<Map<String, Object>>());
        sType.setOnItemSelectedListener(this);
        sType.setAdapter(feedbackTypeAdapter);

        sType.setEnabled(false);

        if(getIntent().hasExtra(TYPE)) {
            String type = getIntent().getStringExtra(TYPE);
            String data = getIntent().getStringExtra(DATA);

            List<FeedbackTypes> typesList = DataManager.getInstance().getDaoSession().getFeedbackTypesDao().queryBuilder().where(FeedbackTypesDao.Properties.C_const.eq(type)).list();
            sType.setSelection(feedbackTypeAdapter.getPositionById(typesList.get(0).getId()));
            if(Objects.requireNonNull(type).equals(NO_DATA)) {
                etMessage.setHint("Укажите номер квартиры или помещение, которое отсутствует. Если их несколько, то нужно указать через запятую.");
            } else if(type.equals(EXCESS_DATA)) {
                try {
                    JSONObject jsonObject = new JSONObject(Objects.requireNonNull(data));
                    etMessage.setText("Квартира или помещение по адресу " + jsonObject.getString("c_address") + " с номером " + jsonObject.getString("c_appartament") + " лишнее.");
                } catch (JSONException e) {
                    Logger.error(e);
                }
            } else if(type.equals(CHANGE_NUMBER)) {
                try {
                    JSONObject jsonObject = new JSONObject(Objects.requireNonNull(data));
                    etMessage.setHint("Укажите новый номер квартиры, которая находится по адресу " + jsonObject.getString("c_address") + " с номером " + jsonObject.getString("c_appartament"));
                } catch (JSONException e) {
                    Logger.error(e);
                }
            } else if(type.equals(CHANGE_HOUSE_NUMBER)) {
                etMessage.setHint("Укажите новый номер дома. Например, 25 или 25/23 корп. 1");
            } else {
                sType.setSelection(feedbackTypeAdapter.getPositionById(typesList.get(0).getId()));
            }
        } else {
            List<FeedbackTypes> typesList = DataManager.getInstance().getDaoSession().getFeedbackTypesDao().queryBuilder().where(FeedbackTypesDao.Properties.C_const.eq("QUESTION")).list();
            sType.setSelection(feedbackTypeAdapter.getPositionById(typesList.get(0).getId()));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        tfvUser.setFieldText(String.valueOf(Authorization.getInstance().getUser().getUserId()));
        tfvSerial.setFieldText(HardwareUtil.getIMEI(this));
        tfvDate.setFieldText(DateUtil.convertDateToUserString(new Date()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_feedback, menu);

        miSend = menu.findItem(R.id.action_feedback_send);

        String type = getIntent().getStringExtra(TYPE);
        miSend.setEnabled(Objects.requireNonNull(type).equals(EXCESS_DATA));

        return super.onCreateOptionsMenu(menu);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        if(item.getItemId() == R.id.action_feedback_send) {
            String txt = etMessage.getText().toString();

            // тут сохранение формы
            HashMap hashMap = (HashMap) sType.getSelectedItem();
            DataManager.getInstance().saveFeedback(this,
                    (Long) hashMap.get(Names.ID),
                    txt,
                    getIntent().hasExtra(DATA) ? getIntent().getStringExtra(DATA) : null);
            Toast.makeText(this, "Вопрос сохранен", Toast.LENGTH_SHORT).show();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.FEEDBACK;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(miSend != null) {
            miSend.setEnabled(count > 0);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        HashMap hashMap = (HashMap) parent.getItemAtPosition(position);
        FeedbackTypes feedbackTypes = DataManager.getInstance().getDaoSession().getFeedbackTypesDao().load((Long) hashMap.get(Names.ID));
        Objects.requireNonNull(getSupportActionBar()).setSubtitle(feedbackTypes.getC_short_name());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}