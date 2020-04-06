package ru.mobnius.vote.ui.fragment.additional;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;

import ru.mobnius.vote.R;

class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder> {
    private Context mContext;
    private ArrayList<HashMap<String, String>> mContacts;
    public ContactAdapter(Context context, ArrayList<HashMap<String, String>> contacts){
        this.mContacts = contacts;
        this.mContext = context;
    }
    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_contact, parent, false);
        return new ContactHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
            holder.bindPoints(mContacts.get(position));
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }


    static class ContactHolder extends RecyclerView.ViewHolder{
        private TextInputEditText tietName;
        private TextInputEditText tietEmail;
        private TextInputEditText tietPhone;

        ContactHolder(@NonNull View itemView) {
            super(itemView);
            tietName = itemView.findViewById(R.id.itemContact_tietName);
            tietEmail = itemView.findViewById(R.id.itemContact_tietEmail);
            tietPhone = itemView.findViewById(R.id.itemContact_tietPhone);
        }

        void bindPoints(HashMap<String, String> contact) {
            tietName.setText(contact.get(ContactDialogFragment.CONTACT_NAME));
            tietEmail.setText(contact.get(ContactDialogFragment.CONTACT_EMAIL));
            tietPhone.setText(contact.get(ContactDialogFragment.CONTACT_PHONE));
        }

    }
}
