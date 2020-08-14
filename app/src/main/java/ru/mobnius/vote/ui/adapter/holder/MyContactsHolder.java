package ru.mobnius.vote.ui.adapter.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import ru.mobnius.vote.R;
import ru.mobnius.vote.data.Logger;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.storage.models.Contacts;
import ru.mobnius.vote.data.storage.models.Houses;
import ru.mobnius.vote.ui.activity.FeedbackActivity;
import ru.mobnius.vote.ui.activity.PointListActivity;
import ru.mobnius.vote.ui.model.PointFilter;
import ru.mobnius.vote.ui.model.PointItem;
import ru.mobnius.vote.ui.model.RouteInfo;
import ru.mobnius.vote.ui.model.RouteItem;
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

        tvAppartament.setText(contact.c_appartament);
        Houses item = contact.getMHouse();
        String name = item.c_street + " " + item.c_number + (!StringUtil.isEmptyOrNull(item.c_build) ? " корп. " + item.c_build : "");
        tvName.setText(name);

        try {
            Date date = DateUtil.convertStringToDate(contact.d_date);
            tvDate.setText(DateUtil.convertDateToUserString(date, DateUtil.USER_SHORT_FORMAT));
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
