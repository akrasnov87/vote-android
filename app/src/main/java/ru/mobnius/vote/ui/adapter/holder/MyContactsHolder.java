package ru.mobnius.vote.ui.adapter.holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.util.Date;

import ru.mobnius.vote.R;
import ru.mobnius.vote.data.Logger;
import ru.mobnius.vote.data.storage.models.Contacts;
import ru.mobnius.vote.data.storage.models.Streets;
import ru.mobnius.vote.utils.DateUtil;
import ru.mobnius.vote.utils.StringUtil;

public class MyContactsHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    private TextView tvAppartament;
    private TextView tvName;
    private TextView tvDate;

    private Contacts mContact;
    private OnMyContactListeners mListeners;

    public MyContactsHolder(@NonNull View itemView) {
        super(itemView);
        tvAppartament = itemView.findViewById(R.id.item_my_contact_appartament);
        tvName = itemView.findViewById(R.id.item_my_contact_name);
        tvDate = itemView.findViewById(R.id.item_my_contact_date);
        itemView.setOnClickListener(this);

        mListeners = (OnMyContactListeners)itemView.getContext();
    }

    public void bind(Contacts contact) {
        mContact = contact;

        tvAppartament.setText("кв. " + contact.c_appartament);
        Streets item = contact.getStreet();
        String name = item.c_type + " " + item.c_name + " д. " + contact.c_house_num + (!StringUtil.isEmptyOrNull(contact.c_house_build) ? " корп. " + contact.c_house_build : "");
        tvName.setText(name);

        try {
            Date date = DateUtil.convertStringToDate(contact.d_date);
            tvDate.setText(DateUtil.convertDateToUserString(date, DateUtil.USER_FORMAT));
        } catch (ParseException e) {
            Logger.error(e);
        }
    }

    @Override
    public void onClick(View v) {
        mListeners.onSelectedContact(mContact);
    }

    public interface OnMyContactListeners {
        void onSelectedContact(Contacts contact);
        void onContactUpdate();
    }
}
