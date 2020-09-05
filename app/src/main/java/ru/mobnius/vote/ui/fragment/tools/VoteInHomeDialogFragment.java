package ru.mobnius.vote.ui.fragment.tools;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import ru.mobnius.vote.Command;
import ru.mobnius.vote.R;
import ru.mobnius.vote.data.Logger;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.authorization.Authorization;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.data.storage.models.Answer;
import ru.mobnius.vote.utils.JsonUtil;

public class VoteInHomeDialogFragment extends AnswerFragmentDialog<String>
        implements View.OnClickListener {

    public static final int YES_DOC_WRITE = 0;
    public static final int YES_DOC_NO_WRITE = 1;
    public static final int NO = 2;

    public static int getRating(JSONObject jsonObject) {
        if(jsonObject != null) {
            try {
                return jsonObject.getInt("n_rating");
            } catch (JSONException e) {
                return 1;
            }
        } else {
            return 1;
        }
    }

    public static int getStatus(JSONObject jsonObject) {
        if(jsonObject != null) {
            try {
                return jsonObject.getInt("n_status");
            } catch (JSONException e) {
                return NO;
            }
        } else {
            return NO;
        }
    }

    private JSONObject joInput;

    private TextView tvMessage;

    private Button btnYesDocWrite;
    private Button btnYesDocNoWrite;
    private Button btnNo;

    public VoteInHomeDialogFragment(Answer answer, String input, boolean isDone) {
        super(answer, Command.VOTE_IN_HOME, isDone);
        try {
            joInput = new JSONObject(input == null ? JsonUtil.EMPTY : input);
        } catch (JSONException e) {
            Logger.error(e);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_fragment_vote_in_home, container, false);

        tvMessage = v.findViewById(R.id.vote_in_home_message);

        setCancelable(false);

        boolean isDone = isDone();

        btnYesDocWrite = v.findViewById(R.id.vote_in_home_yes_doc_write);
        btnYesDocWrite.setOnClickListener(this);

        btnYesDocNoWrite = v.findViewById(R.id.vote_in_home_yes_doc_no_write);
        btnYesDocNoWrite.setOnClickListener(this);

        btnNo = v.findViewById(R.id.vote_in_home_no);
        btnNo.setOnClickListener(this);

        if(isDone) {
            btnYesDocWrite.setVisibility(View.GONE);
            btnYesDocNoWrite.setVisibility(View.GONE);
            btnNo.setVisibility(View.GONE);

            int status = getStatus(joInput);
            switch (status) {
                case YES_DOC_WRITE:
                    btnYesDocWrite.setBackgroundResource(R.drawable.button_success_state);
                    btnYesDocWrite.setVisibility(View.VISIBLE);
                    break;

                case YES_DOC_NO_WRITE:
                    btnYesDocNoWrite.setBackgroundResource(R.drawable.button_success_state);
                    btnYesDocNoWrite.setVisibility(View.VISIBLE);
                    break;

                case NO:
                    btnNo.setBackgroundResource(R.drawable.button_success_state);
                    btnNo.setVisibility(View.VISIBLE);
                    break;
            }
        }

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(getDialog())).getWindow()).setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onResume() {
        super.onResume();

        int uik = DataManager.getInstance().getProfile().uik;

        String message ="<p>«Скажите, пожалуйста, проживают ли у Вас в квартире жители, которые нуждаются в голосовании НА ДОМУ? У меня с собой имеется бланк заявления, если мы его сейчас заполним, то 13 сентября к Вам придут члены участковой избирательной комиссии № "+ uik +" для предоставления возможности проголосовать дома.</p>\n" +
                "<p>Такая возможность предоставляется только тем, кто зарегистрирован по данному адресу».\n" +
                "</p>";

        tvMessage.setText(Html.fromHtml(message));

        if(!isDone()) {
            confirmDialog(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (DialogInterface.BUTTON_NEGATIVE == which) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("n_rating", 1);
                            jsonObject.put("n_status", NO);
                        } catch (JSONException e) {
                            Logger.error(e);
                        }
                        onAnswerListener(jsonObject.toString());
                        dismiss();
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("n_rating", 8);

            switch (v.getId()) {
                case R.id.vote_in_home_yes_doc_write:
                    jsonObject.put("n_status", YES_DOC_WRITE);
                    break;

                case R.id.vote_in_home_yes_doc_no_write:
                    jsonObject.put("n_status", YES_DOC_NO_WRITE);
                    break;

                case R.id.vote_in_home_no:
                    jsonObject.put("n_status", NO);
                    break;
            }
        }catch (Exception e) {
            Logger.error(e);
        } finally {
            onAnswerListener(jsonObject.toString());
            dismiss();
        }
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.VOTE_IN_HOME_DIALOG;
    }

    /**
     * Вывод окна сообщения
     *
     * @param listener обработчик события нажатий
     */
    private void confirmDialog(DialogInterface.OnClickListener listener) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());

        adb.setPositiveButton("Положительно", listener);
        adb.setNegativeButton("Отрицательно", listener);

        AlertDialog alert = adb.create();
        alert.setTitle(getString(R.string.vote_rating));
        alert.show();
    }
}
