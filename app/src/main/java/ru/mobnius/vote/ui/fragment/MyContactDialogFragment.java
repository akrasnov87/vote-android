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
import ru.mobnius.vote.data.storage.models.FeedbackTypes;
import ru.mobnius.vote.data.storage.models.Houses;
import ru.mobnius.vote.ui.adapter.FeedbackTypeAdapter;
import ru.mobnius.vote.ui.adapter.HouseAdapter;
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
    private Spinner mHouses;
    private EditText mAppartament;
    private EditText mPhone;
    private EditText mDescription;

    private Houses mHouse;
    private HouseAdapter mHouseAdapter;
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

        btnDel = v.findViewById(R.id.my_contact_del);
        btnDel.setOnClickListener(this);

        btnClose = v.findViewById(R.id.my_contact_close);
        btnClose.setOnClickListener(this);

        btnSave = v.findViewById(R.id.my_contact_done);
        btnSave.setOnClickListener(this);

        mFirstName = v.findViewById(R.id.my_contact_first_name);
        mLastName = v.findViewById(R.id.my_contact_last_name);
        mPatronymic = v.findViewById(R.id.my_contact_patronymic);
        mHouses = v.findViewById(R.id.my_contact_house);

        mHouseAdapter = new HouseAdapter(getContext(), new ArrayList<Map<String, Object>>());
        mHouses.setOnItemSelectedListener(this);
        mHouses.setAdapter(mHouseAdapter);

        mAppartament = v.findViewById(R.id.my_contact_appartament);
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
            mPhone.setText(mContact.c_phone);
            mDescription.setText(mContact.c_description);
            mHouses.setSelection(mHouseAdapter.getPositionById(mContact.fn_house));
        }
        btnDel.setVisibility(mContact != null ? View.VISIBLE : View.GONE);
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
                mContact.c_description = mDescription.getText().toString();
                mContact.c_phone = mPhone.getText().toString();
                mContact.fn_house = mHouse.id;

                if(isCreate) {
                    DataManager.getInstance().getDaoSession().getContactsDao().insert(mContact);
                } else {
                    DataManager.getInstance().getDaoSession().getContactsDao().update(mContact);
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
        mHouse = DataManager.getInstance().getDaoSession().getHousesDao().load(String.valueOf(hashMap.get(Names.ID)));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
