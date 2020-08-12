package ru.mobnius.vote.ui.fragment.tools;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Objects;

import ru.mobnius.vote.Command;
import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.data.storage.models.Answer;
import ru.mobnius.vote.utils.JsonUtil;

public class VotingDialogFragment extends AnswerFragmentDialog<String>
        implements View.OnClickListener, ContactHolder.OnContactItemListener {

    private final ArrayList<ContactItem> mContacts;
    private ContactAdapter mContactAdapter;
    private TextView mEmptyView;

    public VotingDialogFragment(Answer answer, String input, boolean isDone) {
        super(answer, Command.VOTING, isDone);
        if (!isDone()) {
            mContacts = new ArrayList<>();
        } else {
            mContacts = (ArrayList<ContactItem>) JsonUtil.convertToContacts(input);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_fragment_voting, container, false);

        setCancelable(false);

        RecyclerView recyclerView = v.findViewById(R.id.contact_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        boolean isDone = isDone();
        mContactAdapter = new ContactAdapter(getActivity(), mContacts, isDone, this);
        recyclerView.setAdapter(mContactAdapter);

        ImageButton btnAdd = v.findViewById(R.id.contact_add);
        btnAdd.setOnClickListener(this);

        Button btnDone = v.findViewById(R.id.contact_done);
        btnDone.setOnClickListener(this);
        if(isDone) {
            btnDone.setText("ОК");
            btnAdd.setVisibility(Button.GONE);
        }

        mEmptyView = v.findViewById(R.id.contact_empty);
        updateContactUI();
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

        if(!isDone()) {
            confirmDialog(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (DialogInterface.BUTTON_NEGATIVE == which) {
                        onAnswerListener("[]");
                        dismiss();
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.contact_add:
                onAdd();
                break;

            case R.id.contact_done:
                String contactsJson = JsonUtil.convertToJson(mContacts);
                onAnswerListener(contactsJson);

                dismiss();
                break;
        }
    }

    @Override
    public void onItemDelete(int position) {
        if(!isDone()) {
            mContacts.remove(position);
            mContactAdapter.notifyDataSetChanged();

            updateContactUI();
        }
    }

    private void updateContactUI() {
        mEmptyView.setVisibility(mContacts.size() > 0 ? TextView.GONE : TextView.VISIBLE);
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.VOTING_DIALOG;
    }

    /**
     * Вывод окна сообщения
     *
     * @param listener обработчик события нажатий
     */
    private void confirmDialog(DialogInterface.OnClickListener listener) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());

        adb.setPositiveButton(getResources().getString(R.string.yes), listener);
        adb.setNegativeButton(getResources().getString(R.string.no), listener);

        AlertDialog alert = adb.create();
        alert.setTitle(getString(R.string.voting_lost));
        alert.show();
    }

    private void onAdd() {
        ContactItem item = new ContactItem();
        item.setDefault(true);
        mContacts.add(item);
        mContactAdapter.notifyDataSetChanged();

        updateContactUI();
    }
}
