package ru.mobnius.vote.ui.fragment.tools;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

import ru.mobnius.vote.Command;
import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.data.storage.models.Answer;

/**
 * Вывод окна с комментарием
 */
public class RatingDialogFragment extends AnswerFragmentDialog<String> implements View.OnClickListener {
    private RatingBar mRatingBar;
    private Button btnDone;
    private final Integer mRating;

    public RatingDialogFragment(Answer answer, Integer rating, boolean isDone) {
        super(answer, Command.RATING, isDone);
        mRating = rating == null ? 0 : rating;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_fragment_rating, container, false);
        btnDone = v.findViewById(R.id.rating_done);
        mRatingBar = v.findViewById(R.id.rating_bar);
        btnDone.setOnClickListener(this);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(getDialog())).getWindow()).setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        mRatingBar.setRating(mRating);
        if(isDone()) {
            btnDone.setText("ОК");
        }
        mRatingBar.setEnabled(!isDone());
    }

    @Override
    public void onClick(View v) {
        onAnswerListener(String.valueOf((int)mRatingBar.getRating()));
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.COMMENT_DIALOG;
    }
}