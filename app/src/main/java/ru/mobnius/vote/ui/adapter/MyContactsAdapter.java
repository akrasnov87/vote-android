package ru.mobnius.vote.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.mobnius.vote.R;
import ru.mobnius.vote.data.storage.models.Contacts;
import ru.mobnius.vote.ui.adapter.holder.MyContactsHolder;

public class MyContactsAdapter extends RecyclerView.Adapter<MyContactsHolder> {

    private final Context mContext;
    private final List<Contacts> mList;

    public MyContactsAdapter(Context context, List<Contacts> items) {
        mContext = context;
        mList = items;
    }

    @NonNull
    @Override
    public MyContactsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_my_contact, parent, false);
        return new MyContactsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyContactsHolder holder, int position) {
        holder.bind(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
