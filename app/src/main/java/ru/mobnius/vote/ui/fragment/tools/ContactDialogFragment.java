package ru.mobnius.vote.ui.fragment.tools;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ru.mobnius.vote.Command;
import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.data.storage.models.Answer;
import ru.mobnius.vote.utils.JsonUtil;

public class ContactDialogFragment extends AnswerFragmentDialog<String>
        implements View.OnClickListener, ContactHolder.OnContactItemListener {
    private final ArrayList<ContactItem> mContacts;
    private ContactAdapter mContactAdapter;
    private TextView mEmptyView;

    public ContactDialogFragment(Answer answer, String input, boolean isDone) {
        super(answer, Command.CONTACT, isDone);
        if (!isDone()) {
            mContacts = new ArrayList<>();
        } else {
            mContacts = (ArrayList<ContactItem>) JsonUtil.convertToContacts(input);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_fragment_contact, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.fContact_rvItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        boolean isDone = isDone();
        mContactAdapter = new ContactAdapter(getActivity(), mContacts, isDone, this);
        recyclerView.setAdapter(mContactAdapter);

        ImageButton btnAdd = v.findViewById(R.id.fContact_btnAdd);
        btnAdd.setOnClickListener(this);

        Button btnDone = v.findViewById(R.id.fContact_btnDone);
        btnDone.setOnClickListener(this);
        if(isDone) {
            btnDone.setText("ОК");
            btnAdd.setVisibility(Button.GONE);
        }

        mEmptyView = v.findViewById(R.id.fContact_emptyView);
        updateContactUI();
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.fContact_btnAdd:
                ContactItem item = new ContactItem();
                item.setDefault(true);
                mContacts.add(item);
                mContactAdapter.notifyDataSetChanged();

                updateContactUI();
                break;

            case R.id.fContact_btnDone:
                if(!isDone()) {
                    String contactsJson = JsonUtil.convertToJson(mContacts);
                    onAnswerListener(contactsJson);
                }
                this.dismiss();
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
        return IExceptionCode.CONTACT_DIALOG;
    }
}
