package ru.mobnius.vote.ui.fragment;

import android.os.Bundle;
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
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.data.storage.models.Answer;
import ru.mobnius.vote.data.storage.models.Question;
import ru.mobnius.vote.ui.fragment.adapter.VoteButtonAdapter;
import ru.mobnius.vote.ui.fragment.data.onQuestionListener;

public class VoteItemFragment extends BaseFragment implements onQuestionListener {

    private TextView tvDescription;
    private RecyclerView rvButtons;

    public static VoteItemFragment createInstance() {
        return new VoteItemFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vote_item, container, false);
        rvButtons = view.findViewById(R.id.rvButtons);
        tvDescription = view.findViewById(R.id.tvDescription);
        return view;
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.VOTE_ITEM;
    }

    /**
     * Привязка данных
     * @param questionID иден. вопроса
     * @param exclusionAnswerID идентификатор существ. ответа
     */
    @Override
    public void onQuestionBind(long questionID, long exclusionAnswerID) {
        DataManager dataManager = DataManager.getInstance();
        rvButtons.setAdapter(new VoteButtonAdapter(getActivity(), dataManager.getAnswers(questionID), exclusionAnswerID));
        rvButtons.setLayoutManager(new LinearLayoutManager(getActivity()));

        Question question = dataManager.getQuestion(questionID);
        if(question != null) {
            tvDescription.setText(question.c_text);
        }
    }


}
