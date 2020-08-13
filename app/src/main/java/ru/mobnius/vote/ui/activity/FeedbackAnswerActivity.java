package ru.mobnius.vote.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.BaseActivity;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.authorization.Authorization;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.data.storage.models.Feedbacks;
import ru.mobnius.vote.data.storage.models.FeedbacksDao;
import ru.mobnius.vote.ui.adapter.FeedbackAnswerAdapter;
import ru.mobnius.vote.ui.component.TextFieldView;
import ru.mobnius.vote.utils.HardwareUtil;

public class FeedbackAnswerActivity extends BaseActivity {

    public static Intent getIntent(Context context) {
        return new Intent(context, FeedbackAnswerActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_answer);
        List<Feedbacks> feedbacks = DataManager.getInstance().getDaoSession().getFeedbacksDao().queryBuilder().where(FeedbacksDao.Properties.D_date_answer.isNotNull()).list();
        TextFieldView tfvSerial = findViewById(R.id.feedback_answer_item_serial);
        TextFieldView tfvUser = findViewById(R.id.feedback_answer_item_user);
        tfvUser.setFieldText(String.valueOf(Authorization.getInstance().getUser().getUserId()));
        tfvSerial.setFieldText(HardwareUtil.getIMEI(this));
        if (feedbacks.size() == 0) {
            TextView tvNoFeedbacks = findViewById(R.id.feedback_answer_no_feedbacks);
            tvNoFeedbacks.setVisibility(View.VISIBLE);
            return;
        }

        RecyclerView rvFeedbackAnswers = findViewById(R.id.feedback_answer_recycler_view);
        rvFeedbackAnswers.setLayoutManager(new LinearLayoutManager(this));
        rvFeedbackAnswers.setAdapter(new FeedbackAnswerAdapter(this, feedbacks));
        rvFeedbackAnswers.setNestedScrollingEnabled(false);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.FEEDBACK;
    }

}