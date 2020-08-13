package ru.mobnius.vote.ui.adapter.holder;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import ru.mobnius.vote.R;
import ru.mobnius.vote.data.storage.models.Feedbacks;
import ru.mobnius.vote.ui.adapter.FeedbackTypeAdapter;
import ru.mobnius.vote.ui.component.TextFieldView;
import ru.mobnius.vote.utils.DateUtil;

public class FeedbackAnswerHolder extends RecyclerView.ViewHolder {

    private TextFieldView tfvType;
    private TextFieldView tfvQuestionDate;
    private TextFieldView tfvQuestion;
    private TextFieldView tfvAnswerDate;
    private TextFieldView tfvAnswer;
    private Context mContext;

    public FeedbackAnswerHolder(Context context, @NonNull View itemView) {
        super(itemView);
        mContext = context;
        tfvType = itemView.findViewById(R.id.feedback_answer_question_type);
        tfvQuestionDate = itemView.findViewById(R.id.feedback_answer_question_date);
        tfvQuestion = itemView.findViewById(R.id.feedback_answer_question);
        tfvAnswerDate = itemView.findViewById(R.id.feedback_answer_date);
        tfvAnswer = itemView.findViewById(R.id.feedback_answer_message);
    }

    public void bindFeedbacks(Feedbacks feedback) {
        FeedbackTypeAdapter feedbackTypeAdapter = new FeedbackTypeAdapter(mContext, new ArrayList<Map<String, Object>>());
        int feedbackTypePosition = feedbackTypeAdapter.getPositionById(feedback.fn_type);
        String feedbackType = feedbackTypeAdapter.getStringValue(feedbackTypePosition);

        tfvType.setFieldText(feedbackType);
        try {
            Date questionDate = DateUtil.convertStringToDate(feedback.d_date_question);
            tfvQuestionDate.setFieldText(DateUtil.convertDateToUserString(questionDate, DateUtil.USER_SHORT_FORMAT));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tfvQuestion.setFieldText(feedback.c_question);
        if (feedback.d_date_answer.equals("null")) {
            tfvAnswerDate.setVisibility(View.GONE);
            tfvAnswer.setFieldText("Ответ еще не готов");
        } else {
            try {
                Date answerDate = DateUtil.convertStringToDate(feedback.d_date_answer);
                tfvAnswerDate.setFieldText(DateUtil.convertDateToUserString(answerDate, DateUtil.USER_SHORT_FORMAT));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            tfvAnswer.setFieldText(feedback.c_answer);
        }
    }
}
