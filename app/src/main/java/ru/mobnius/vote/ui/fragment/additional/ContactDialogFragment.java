package ru.mobnius.vote.ui.fragment.additional;

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
import java.util.HashMap;
import java.util.Objects;

import ru.mobnius.vote.R;

public class ContactDialogFragment extends BaseAdditionalInfoDialog implements View.OnClickListener {

    public final static String CONTACT_NAME = "contact_name";
    final static String CONTACT_EMAIL = "contact_email";
    final static String CONTACT_PHONE = "contact_phone";

    private ArrayList<HashMap<String, String>> mContacts;
    private RecyclerView mRecyclerView;
    private ContactAdapter mContactAdapter;

    public ContactDialogFragment(boolean isUpdate) {
        super(isUpdate);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContacts = new ArrayList<>();
        HashMap<String, String> m = new HashMap<>();
        m.put(CONTACT_NAME, "");
        m.put(CONTACT_EMAIL, "");
        m.put(CONTACT_PHONE, "");
        mContacts.add(m);
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
                HashMap<String, String> m = new HashMap<>();
                m.put(CONTACT_NAME, "");
                m.put(CONTACT_EMAIL, "");
                m.put(CONTACT_PHONE, "");
                if (getContacts().size()>0){ //сохраняем ранее введенные данные если они были введены
                    ArrayList<HashMap<String, String>> list = getContacts();
                    for (int i = 0; i < list.size(); i++) {
                        mContacts.set(i, list.get(i));
                    }
                }
                mContacts.add(m);
                mContactAdapter.notifyDataSetChanged();
                break;
            case R.id.fContact_btnDone:
                getInfoCallback().OnContactFinish(getContacts());
                break;
        }
    }

    /**
     *
     * @return массив пар ключ-значение с контактными дынными
     */

    private ArrayList<HashMap<String, String>> getContacts() {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        for (int i = 0; i < mContacts.size(); i++) {
            View item = Objects.requireNonNull(mRecyclerView.getLayoutManager()).findViewByPosition(i);
            if (item != null) {
                TextInputEditText name = item.findViewById(R.id.itemContact_tietName);
                TextInputEditText email = item.findViewById(R.id.itemContact_tietEmail);
                TextInputEditText phone = item.findViewById(R.id.itemContact_tietPhone);
                if (!Objects.requireNonNull(name.getText()).toString().isEmpty()) {
                    HashMap<String, String> m = new HashMap<>();
                    m.put(CONTACT_NAME, name.getText().toString());
                    m.put(CONTACT_EMAIL, Objects.requireNonNull(email.getText()).toString());
                    m.put(CONTACT_PHONE, Objects.requireNonNull(phone.getText()).toString());
                    list.add(m);
                }
            }
        }
        return list;
    }
}
