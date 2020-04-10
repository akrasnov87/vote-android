package ru.mobnius.vote.ui.fragment.tools;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import ru.mobnius.vote.Command;
import ru.mobnius.vote.R;
import ru.mobnius.vote.data.storage.models.Answer;

public class CommentDialogFragment extends AnswerFragmentDialog<String> implements View.OnClickListener {
    private TextInputEditText tietComment;

    public CommentDialogFragment(Answer answer, String input) {
        super(answer, Command.COMMENT, input);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_fragment_comment, container, false);
        Button btnDone = v.findViewById(R.id.fComment_btnDone);
        tietComment = v.findViewById(R.id.fComment_tietComment);
        btnDone.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        onAnswerListener(Objects.requireNonNull(tietComment.getText()).toString());
    }
}