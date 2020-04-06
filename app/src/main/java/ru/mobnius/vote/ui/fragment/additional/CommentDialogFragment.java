package ru.mobnius.vote.ui.fragment.additional;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import ru.mobnius.vote.R;

public class CommentDialogFragment extends BaseAdditionalInfoDialog implements View.OnClickListener {
    private TextInputEditText tietComment;
    private boolean isFinish;

    public CommentDialogFragment(boolean isUpdate, boolean isFinish) {
        super(isUpdate);
        this.isFinish = isFinish;
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
        String comment = Objects.requireNonNull(tietComment.getText()).toString();
        getInfoCallback().OnCommentFinish(comment, isFinish);
    }
}