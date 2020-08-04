package ru.mobnius.vote.ui.adapter.holder;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.mobnius.vote.R;
import ru.mobnius.vote.data.Logger;
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

        if(point.color == null) {
            if (point.done) {
                tvDeviceNumber.setTextColor(mContext.getResources().getColor(R.color.colorSuccess));
            } else {
                tvDeviceNumber.setTextColor(mContext.getResources().getColor(R.color.colorHint));
            }
        } else {
            if(point.rating != null) {
                try {
                    if (point.rating <= 4) {
                        tvDeviceNumber.setTextColor(Color.parseColor(point.color[0]));
                    } else if (point.rating <= 7) {
                        tvDeviceNumber.setTextColor(Color.parseColor(point.color[1]));
                    } else {
                        tvDeviceNumber.setTextColor(Color.parseColor(point.color[2]));
                    }
                }catch (Exception e) {
                    tvDeviceNumber.setTextColor(mContext.getResources().getColor(R.color.colorHint));
                    Logger.error(e);
                }
            } else {
                tvDeviceNumber.setTextColor(Color.parseColor(point.color[0]));
            }
        }
    }

    @Override
    public void onClick(View v) {
        mContext.startActivity(QuestionActivity.newIntent(mContext, mItem));
    }
}
