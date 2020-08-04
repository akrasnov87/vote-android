package ru.mobnius.vote.ui.fragment;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.BaseFragment;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.authorization.Authorization;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.data.storage.models.Question;
import ru.mobnius.vote.ui.adapter.VoteButtonAdapter;
import ru.mobnius.vote.ui.data.OnQuestionListener;
import ru.mobnius.vote.ui.model.PointInfo;

public class VoteItemFragment extends BaseFragment
        implements OnQuestionListener {

    private TextView tvDescription;
    private RecyclerView rvButtons;

    public static VoteItemFragment createInstance() {
        return new VoteItemFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vote_item, container, false);

        rvButtons = view.findViewById(R.id.question_item_answers);
        tvDescription = view.findViewById(R.id.question_item_description);

        return view;
    }

    /**
     * Привязка данных
     * @param questionID иден. вопроса
     * @param exclusionAnswerID идентификатор существ. ответа
     */
    @Override
    public void onQuestionBind(PointInfo pointInfo, long questionID, long exclusionAnswerID, long lastAnswerId) {
        DataManager dataManager = DataManager.getInstance();

        rvButtons.setAdapter(new VoteButtonAdapter(getActivity(), dataManager.getAnswers(questionID), exclusionAnswerID, lastAnswerId));
        rvButtons.setLayoutManager(new LinearLayoutManager(getActivity()));

        Question question = dataManager.getQuestion(questionID);
        if(question != null) {
            question.c_text = question.c_text.replace("[c_fio]", pointInfo.getUserName() == null ? "<font color='#FF0000'>(ФИО не найдено)</font>" : pointInfo.getUserName());
            question.c_text = question.c_text.replace("[c_my_fio]", dataManager.getProfile().fio);
            tvDescription.setText(Html.fromHtml(question.c_text));
        }
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.VOTE_ITEM;
    }
}
