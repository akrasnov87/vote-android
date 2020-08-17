package ru.mobnius.vote.ui.adapter.holder;

import android.text.Html;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import ru.mobnius.vote.R;
import ru.mobnius.vote.data.Logger;
import ru.mobnius.vote.data.storage.models.FeedbackTypes;
import ru.mobnius.vote.data.storage.models.Feedbacks;
import ru.mobnius.vote.ui.adapter.FeedbackTypeAdapter;
import ru.mobnius.vote.utils.DateUtil;
import ru.mobnius.vote.utils.StringUtil;

public class NotificationHolder extends RecyclerView.ViewHolder {

    private TextView tvType;
    private TextView tvQuestionDate;
    private TextView tvQuestion;
    private TextView tvAnswerDate;
    private TextView tvAnswer;
    private List<FeedbackTypes> mFeedbackTypes;

    public NotificationHolder(@NonNull View itemView, List<FeedbackTypes> feedbackTypes) {
        super(itemView);
        mFeedbackTypes = feedbackTypes;

        tvType = itemView.findViewById(R.id.notification_item_type);
        tvQuestionDate = itemView.findViewById(R.id.notification_item_question_date);
        tvQuestion = itemView.findViewById(R.id.notification_item_question);
        tvAnswerDate = itemView.findViewById(R.id.notification_item_answer_date);
        tvAnswer = itemView.findViewById(R.id.notification_item_answer);
    }

    public void bind(Feedbacks item) {
        FeedbackTypes feedbackType = getItem(item.fn_type);

        if(feedbackType != null) {
            tvType.setText(feedbackType.getC_short_name());
        }

        try {
            Date questionDate = DateUtil.convertStringToDate(item.d_date_question);
            tvQuestionDate.setText(DateUtil.convertDateToUserString(questionDate, DateUtil.USER_SHORT_FORMAT));

            tvQuestion.setText(item.c_question);

            Date answerDate = DateUtil.convertStringToDate(item.d_date_answer);
            tvAnswerDate.setText(DateUtil.convertDateToUserString(answerDate, DateUtil.USER_SHORT_FORMAT));

            tvAnswer.setText(Html.fromHtml(item.c_answer));
        } catch (ParseException e) {
            Logger.error(e);
        }
    }

    private FeedbackTypes getItem(long id) {
        for (FeedbackTypes feedbackType:
             mFeedbackTypes) {
            if(feedbackType.getId() == id) {
                return feedbackType;
            }
        }

        return null;
    }
}