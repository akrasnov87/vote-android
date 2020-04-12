package ru.mobnius.vote.ui.fragment.tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import ru.mobnius.vote.R;

class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder> {
    private ContactDialogFragment mFragment;
    private Context mContext;
    private ArrayList<ContactItem> mContacts;
    ContactAdapter(Context context, ArrayList<ContactItem> contacts, ContactDialogFragment fragment){
        this.mContacts = contacts;
        this.mContext = context;
        this.mFragment = fragment;
    }
    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_contact, parent, false);
        return new ContactHolder(view, mFragment);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
            holder.bindPoints(mContacts.get(position));
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }




    static class ContactHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextInputEditText tietName;
        private TextInputEditText tietPhone;
        private Button btnDelete;
        private IDeleteItem mIDeleteItem;

        ContactHolder(@NonNull View itemView, ContactDialogFragment fragment) {
            super(itemView);
            mIDeleteItem = (IDeleteItem)fragment;
            tietName = itemView.findViewById(R.id.itemContact_tietName);
            tietPhone = itemView.findViewById(R.id.itemContact_tietPhone);
            btnDelete = itemView.findViewById(R.id.itemContact_btnDelete);
            btnDelete.setOnClickListener(this);
        }

        void bindPoints(ContactItem contact) {
            tietName.setText(contact.c_key);
            tietPhone.setText(contact.c_value);
        }

        @Override
        public void onClick(View v) {
            mIDeleteItem.OnItemDelete(getAdapterPosition());
        }
    }

}
