package ru.mobnius.vote.ui.fragment.tools;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

import ru.mobnius.vote.Command;
import ru.mobnius.vote.R;
import ru.mobnius.vote.data.storage.models.Answer;

/**
 * Вывод окна с комментарием
 */
public class CommentDialogFragment extends AnswerFragmentDialog<String> implements View.OnClickListener {
    private EditText etComment;
    private Button btnDone;
    private String mInput;

    public CommentDialogFragment(Answer answer, String input, boolean isDone) {
        super(answer, Command.COMMENT, input, isDone);
        mInput = input == null ? "" : input;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_fragment_comment, container, false);
        btnDone = v.findViewById(R.id.fComment_btnDone);
        etComment = v.findViewById(R.id.fComment_etComment);
        btnDone.setOnClickListener(this);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        etComment.setText(mInput);
        if(isDone()) {
            btnDone.setText("ОК");
        }
        etComment.setEnabled(!isDone());
    }

    @Override
    public void onClick(View v) {
        onAnswerListener(Objects.requireNonNull(etComment.getText()).toString());
    }
}