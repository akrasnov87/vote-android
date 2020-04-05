package ru.mobnius.vote.ui.additional;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;

import ru.mobnius.vote.R;

public class BaseAdditionalInfoDialog extends DialogFragment implements View.OnClickListener {

    private final static String LAYOUT_ID = "layout_id";
    private final static String BTN_ID = "btn_id";
    private final static String TIET_ID = "tiet_id";

    private IAdditionalInfoCallback mInfoCallback;
    private String dialogType;
    private Button btnDone;
    private TextInputEditText tietComment;

    public BaseAdditionalInfoDialog(String type){
        this.dialogType = type;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof IAdditionalInfoCallback){
            mInfoCallback = (IAdditionalInfoCallback) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        HashMap<String, Integer> m = getResId(dialogType);
        View v = inflater.inflate(m.get(LAYOUT_ID), container, false);
        btnDone = v.findViewById(m.get(BTN_ID));
        tietComment = v.findViewById(m.get(TIET_ID));
        btnDone.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        String comment = "";
        if(!tietComment.getText().toString().isEmpty()){
            comment = tietComment.getText().toString() + dialogType;
        }
        this.dismiss();
    }



    private HashMap<String, Integer> getResId(String type) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();

        switch (type){
            case IAdditionalInfoCallback.COMMENT:
                map.put(LAYOUT_ID, R.layout.dialog_fragment_contact);
                map.put(BTN_ID, R.id.fFinish_btnDone);
                map.put(TIET_ID, R.id.fFinish_tfvComment);
                break;
            case IAdditionalInfoCallback.COMMENT_FINISH:
                map.put(LAYOUT_ID, R.layout.dialog_fragment_comment);
                map.put(BTN_ID, R.id.fFinishComment_btnDone);
                map.put(TIET_ID, R.id.fFinishComment_tfvComment);
                break;
            case IAdditionalInfoCallback.CONTACT:
                map.put(LAYOUT_ID, R.layout.dialog_fragment_contact);
                break;

        }

        return map;

    }
    public interface IAdditionalInfoCallback{
        String FINISH = "FINISH";
        String COMMENT_FINISH = "FINISH,COMMENT";
        String CONTACT = "CONTACT";
        String COMMENT = "COMMENT";
        void OnCommentFinish(String comment);

        void OnContact(String comment);

        void onComment();
    }
}
