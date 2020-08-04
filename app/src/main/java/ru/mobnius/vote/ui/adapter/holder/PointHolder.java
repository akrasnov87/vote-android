package ru.mobnius.vote.ui.adapter.holder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.mobnius.vote.R;
import ru.mobnius.vote.ui.activity.QuestionActivity;
import ru.mobnius.vote.ui.model.PointItem;

public class PointHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    private final Context mContext;
    private final TextView tvDeviceNumber;
    private final TextView tvDevicePriority;
    private PointItem mItem;

    public PointHolder(Context context, @NonNull View itemView) {
        super(itemView);

        mContext = context;
        tvDeviceNumber = itemView.findViewById(R.id.itemPoint_appartamentNumber);
        tvDevicePriority = itemView.findViewById(R.id.itemPoint_appartamentPriority);
        itemView.setOnClickListener(this);
    }

    public void bindPoints(PointItem point) {
        mItem = point;

        tvDeviceNumber.setText(point.appartament);
        tvDevicePriority.setText(point.priority != null ? String.valueOf(point.priority) : "");
        if(point.done) {
            tvDeviceNumber.setTextColor(mContext.getResources().getColor(R.color.colorSuccess));
        } else {
            tvDeviceNumber.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryText));
        }
    }

    @Override
    public void onClick(View v) {
        mContext.startActivity(QuestionActivity.newIntent(mContext, mItem));
    }
}
