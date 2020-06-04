package ru.mobnius.vote.ui.adapter.holder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.mobnius.vote.R;
import ru.mobnius.vote.ui.model.RatingItemModel;

public class RatingHolder extends RecyclerView.ViewHolder {

    private final TextView tvId;
    private final TextView tvFio;
    private final TextView tvUik;
    private final TextView tvCount;
    private final TextView tvCountToday;

    private final Context mContext;

    public RatingHolder(Context context, @NonNull View itemView) {
        super(itemView);

        mContext = context;

        tvId = itemView.findViewById(R.id.rating_item_id);
        tvFio = itemView.findViewById(R.id.rating_item_fio);
        tvUik = itemView.findViewById(R.id.rating_item_uik);
        tvCount = itemView.findViewById(R.id.rating_item_count);
        tvCountToday = itemView.findViewById(R.id.rating_item_count_today);
    }

    public void bind(RatingItemModel item) {
        tvId.setText(String.valueOf(item.id));
        tvFio.setText(item.c_login);
        tvUik.setText(String.valueOf(item.n_uik));
        tvCount.setText(String.valueOf(item.n_count));
        tvCountToday.setText(item.n_today_count > 0 ? "+" + item.n_today_count : "0");
        if(item.n_today_count == 0) {
            tvCountToday.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryText));
        } else {
            tvCountToday.setTextColor(mContext.getResources().getColor(R.color.colorSuccessLight));
        }
    }
}
