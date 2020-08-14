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
import ru.mobnius.vote.ui.adapter.holder.HouseHolder;
import ru.mobnius.vote.ui.adapter.holder.RouteHolder;
import ru.mobnius.vote.ui.model.RouteItem;

public class HouseListAdapter extends RecyclerView.Adapter<HouseHolder> {

    private final Context mContext;
    private final List<Contacts> mList;

    public HouseListAdapter(Context context, List<Contacts> items) {
        mContext = context;
        mList = items;
    }

    @NonNull
    @Override
    public HouseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_my_contact, parent, false);
        return new HouseHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HouseHolder holder, int position) {
        holder.bind(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
