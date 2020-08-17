package ru.mobnius.vote.ui.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import ru.mobnius.vote.Names;
import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.BaseDialogFragment;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.DbOperationType;
import ru.mobnius.vote.data.manager.authorization.Authorization;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.data.storage.models.Contacts;
import ru.mobnius.vote.data.storage.models.Streets;
import ru.mobnius.vote.ui.adapter.StreetAdapter;
import ru.mobnius.vote.ui.adapter.holder.MyContactsHolder;
import ru.mobnius.vote.utils.DateUtil;

public class MyContactDialogFragment extends BaseDialogFragment
    implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Contacts mContact;

    private ImageButton btnDel;
    private Button btnClose;
    private Button btnSave;

    private EditText mFirstName;
    private EditText mLastName;
    private EditText mPatronymic;
    private Spinner mStreets;
    private EditText mAppartament;
    private EditText mHouseNum;
    private EditText mHouseBuild;
    private EditText mPhone;
    private EditText mDescription;

    private Streets mStreet;
    private StreetAdapter mStreetAdapter;
    private MyContactsHolder.OnMyContactListeners mListeners;

    public MyContactDialogFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof MyContactsHolder.OnMyContactListeners) {
            mListeners = (MyContactsHolder.OnMyContactListeners)context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_fragment_my_contact, container, false);

        if(savedInstanceState != null) {
            String mContactId = (String) savedInstanceState.getSerializable(Names.ID);
            mContact = DataManager.getInstance().getDaoSession().getContactsDao().load(mContactId);
        }

        btnDel = v.findViewById(R.id.my_contact_del);
        btnDel.setOnClickListener(this);

        btnClose = v.findViewById(R.id.my_contact_close);
        btnClose.setOnClickListener(this);

        btnSave = v.findViewById(R.id.my_contact_done);
        btnSave.setOnClickListener(this);

        mFirstName = v.findViewById(R.id.my_contact_first_name);
        mLastName = v.findViewById(R.id.my_contact_last_name);
        mPatronymic = v.findViewById(R.id.my_contact_patronymic);
        mStreets = v.findViewById(R.id.my_contact_street);

        mStreetAdapter = new StreetAdapter(getContext(), new ArrayList<Map<String, Object>>());
        mStreets.setOnItemSelectedListener(this);
        mStreets.setAdapter(mStreetAdapter);

        mAppartament = v.findViewById(R.id.my_contact_appartament);
        mHouseNum = v.findViewById(R.id.my_contact_house_num);
        mHouseBuild = v.findViewById(R.id.my_contact_house_build);
        mPhone = v.findViewById(R.id.my_contact_phone);
        mDescription = v.findViewById(R.id.my_contact_description);

        setCancelable(false);

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
        if(mContact != null) {
            mFirstName.setText(mContact.c_first_name);
            mLastName.setText(mContact.c_last_name);
            mPatronymic.setText(mContact.c_patronymic);
            mAppartament.setText(mContact.c_appartament);
            mHouseNum.setText(mContact.c_house_num);
            mHouseBuild.setText(mContact.c_house_build);
            mPhone.setText(mContact.c_phone);
            mDescription.setText(mContact.c_description);
            mStreets.setSelection(mStreetAdapter.getPositionById(mContact.fn_street));
        }
        btnDel.setVisibility(mContact != null ? View.VISIBLE : View.GONE);

        Objects.requireNonNull(getDialog()).setOnKeyListener(new DialogInterface.OnKeyListener()
        {
            @Override
            public boolean onKey(android.content.DialogInterface dialog,
                                 int keyCode,android.view.KeyEvent event)
            {
                if ((keyCode ==  android.view.KeyEvent.KEYCODE_BACK))
                {
                    // To dismiss the fragment when the back-button is pressed.
                    dismiss();
                    return true;
                }
                // Otherwise, do nothing else
                else return false;
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mContact != null) {
            outState.putSerializable(Names.ID, mContact.id);
        }
    }

    public void bind(Contacts contact) {
        mContact = contact;
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.MY_CONTACT_ITEM;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_contact_del:
                confirmDialog(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == DialogInterface.BUTTON_POSITIVE) {
                            mContact.b_disabled = true;
                            mContact.isSynchronization = false;
                            mContact.objectOperationType = DbOperationType.UPDATED;
                            DataManager.getInstance().getDaoSession().getContactsDao().update(mContact);
                            dismiss();
                            mListeners.onContactUpdate();
                        }
                    }
                });
                break;

            case R.id.my_contact_close:
                dismiss();
                break;

            case R.id.my_contact_done:
                boolean isCreate = false;
                if(mContact == null) {
                    isCreate = true;
                    mContact = new Contacts();
                    mContact.id = UUID.randomUUID().toString();
                    mContact.fn_user = Authorization.getInstance().getUser().getUserId();
                    mContact.d_date = DateUtil.convertDateToString(new Date());
                    mContact.n_order = new Date().getTime();
                    mContact.objectOperationType = DbOperationType.CREATED;
                } else {
                    if(mContact.isSynchronization) {
                        mContact.objectOperationType = DbOperationType.UPDATED;
                    }
                }
                mContact.isSynchronization = false;
                mContact.c_first_name = mFirstName.getText().toString();
                mContact.c_last_name = mLastName.getText().toString();
                mContact.c_patronymic = mPatronymic.getText().toString();
                mContact.c_appartament = mAppartament.getText().toString();
                mContact.c_house_num = mHouseNum.getText().toString();
                mContact.c_house_build = mHouseBuild.getText().toString();
                mContact.c_description = mDescription.getText().toString();
                mContact.c_phone = mPhone.getText().toString();
                if(mStreet != null) {
                    mContact.fn_street = mStreet.id;
                    if(isCreate) {
                        DataManager.getInstance().getDaoSession().getContactsDao().insert(mContact);
                    } else {
                        DataManager.getInstance().getDaoSession().getContactsDao().update(mContact);
                    }
                }

                dismiss();
                mListeners.onContactUpdate();
                break;
        }
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
        alert.setTitle("Удалить запись о контакте?");
        alert.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        HashMap hashMap = (HashMap) parent.getItemAtPosition(position);
        mStreet = DataManager.getInstance().getDaoSession().getStreetsDao().load(String.valueOf(hashMap.get(Names.ID)));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
