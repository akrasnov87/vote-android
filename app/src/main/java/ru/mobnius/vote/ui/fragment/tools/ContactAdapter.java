package ru.mobnius.vote.ui.fragment.tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ru.mobnius.vote.R;

public class ContactAdapter extends RecyclerView.Adapter<ContactHolder> implements ContactHolder.OnContactChange {
    private final ContactHolder.OnContactItemListener mListener;
    private final Context mContext;
    private final ArrayList<ContactItem> mContacts;
    private boolean mIsDone;

    public ContactAdapter(Context context, ArrayList<ContactItem> contacts, boolean isDone, ContactHolder.OnContactItemListener listener) {
        mContacts = contacts;
        mContext = context;
        mListener = listener;
        mIsDone = isDone;
    }
    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_contact, parent, false);
        return new ContactHolder(view, mListener, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
        holder.bindItem(mContacts.get(position), mIsDone);
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    @Override
    public void onNumberChange(int position, String value) {
        mContacts.get(position).c_key = value;
    }

    @Override
    public void onNameChange(int position, String value) {
        mContacts.get(position).c_value = value;
    }
}
