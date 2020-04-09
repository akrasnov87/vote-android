package ru.mobnius.vote.ui.fragment.tools;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Objects;

import ru.mobnius.vote.Command;
import ru.mobnius.vote.R;
import ru.mobnius.vote.data.storage.models.Answer;
import ru.mobnius.vote.utils.JsonUtil;

public class ContactDialogFragment extends AnswerFragmentDialog implements View.OnClickListener {

    private ArrayList<ContactItem> mContacts;
    private RecyclerView mRecyclerView;
    private ContactAdapter mContactAdapter;
    private boolean isCreateMode;

    public ContactDialogFragment(Answer answer, String contactsJson) {
        super(answer, Command.CONTACT);
        isCreateMode = contactsJson == null;
        if (isCreateMode) {
            mContacts = new ArrayList<>();
            ContactItem item = new ContactItem();
            item.setDefault(true);
            mContacts.add(item);
        } else {
            mContacts = (ArrayList<ContactItem>) JsonUtil.convertToContacts(contactsJson);
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_fragment_contact, container, false);
        mRecyclerView = v.findViewById(R.id.fContact_rvContacts);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mContactAdapter = new ContactAdapter(getContext(), mContacts);
        mRecyclerView.setAdapter(mContactAdapter);
        Button btnAdd = v.findViewById(R.id.fContact_btnAdd);
        btnAdd.setOnClickListener(this);
        Button btnDone = v.findViewById(R.id.fContact_btnDone);
        btnDone.setOnClickListener(this);
        return v;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fContact_btnAdd:
                ContactItem item = new ContactItem();
                item.setDefault(true);
                if (mContacts.size() > 0) { //сохраняем ранее введенные данные если они были введены
                    restoreContacts();
                }
                mContacts.add(item);
                mContactAdapter.notifyDataSetChanged();
                break;
            case R.id.fContact_btnDone:
                String contactsJson = JsonUtil.convertToJson(mContacts);
                onAnswerListener(contactsJson);
                this.dismiss();
                break;
        }
    }

    /**
     * @return массив контактов
     */

    private void restoreContacts() {
        for (int i = 0; i < mContacts.size(); i++) {
            View item = Objects.requireNonNull(mRecyclerView.getLayoutManager()).findViewByPosition(i);
            if (item != null) {
                TextInputEditText name = item.findViewById(R.id.itemContact_tietName);
                TextInputEditText phone = item.findViewById(R.id.itemContact_tietPhone);
                if (hasData(name, phone) && mContacts.get(i).b_default) {
                    ContactItem contactItem = new ContactItem(name.getText().toString(), phone.getText().toString());
                    contactItem.setDefault(false);
                    mContacts.set(i, contactItem);
                }
            }
        }
    }

    private boolean hasData(TextInputEditText name, TextInputEditText phone) {
        if (name.getText() != null && phone.getText() != null) {
            String fio = name.getText().toString();
            String telephone = phone.getText().toString();
            return !fio.isEmpty() || !telephone.isEmpty();
        }
        return false;
    }
}
