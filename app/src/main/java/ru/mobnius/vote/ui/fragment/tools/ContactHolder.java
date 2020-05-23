package ru.mobnius.vote.ui.fragment.tools;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.mobnius.vote.R;

public class ContactHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final EditText etName;
    private final EditText etPhone;

    private final OnContactItemListener mListener;
    private final ContactHolder.OnContactChange mContactChange;

    public ContactHolder(@NonNull View itemView, OnContactItemListener listener, ContactHolder.OnContactChange onContactChange) {
        super(itemView);
        mListener = listener;
        mContactChange = onContactChange;

        etName = itemView.findViewById(R.id.itemContact_etName);

        etPhone = itemView.findViewById(R.id.itemContact_etPhone);

        ImageButton btnDelete = itemView.findViewById(R.id.itemContact_btnDelete);
        btnDelete.setOnClickListener(this);
    }

    public void bindItem(ContactItem contact) {
        etName.setText(contact.c_key);
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int position = getAdapterPosition();
                mContactChange.onNameChange(position, s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etPhone.setText(contact.c_value);
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int position = getAdapterPosition();
                mContactChange.onNumberChange(position, s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        mListener.onItemDelete(getAdapterPosition());
    }

    public interface OnContactItemListener {
        void onItemDelete(int position);
    }

    public interface OnContactChange {
        void onNumberChange(int position, String value);
        void onNameChange(int position, String value);
    }
}
