package ru.mobnius.vote.ui.fragment.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.ui.activity.ControlMeterReadingsActivity;
import ru.mobnius.vote.ui.component.TextFieldView;
import ru.mobnius.vote.ui.model.PointItem;
import ru.mobnius.vote.ui.model.PointResult;

public class PointAdapter extends RecyclerView.Adapter<PointAdapter.PointHolder> {

    private List<PointItem> mPointsList;
    private Context mContext;

    public PointAdapter(Context context, List<PointItem> pointItems) {
        this.mPointsList = pointItems;
        this.mContext = context;
    }

    @NonNull
    @Override
    public PointHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_point, parent, false);
        return new PointHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PointHolder holder, int position) {
        if (mPointsList.size() > 0) {
            Resources resources = mContext.getResources();
            String status = resources.getString(R.string.not_done);
            PointItem point = mPointsList.get(position);
            if (point.done) {
                status = resources.getString(R.string.done);
                if (point.sync) {
                    status = status + "/" + resources.getString(R.string.synced);
                }
            }
            holder.bindPoints(point, status);
        }
    }

    @Override
    public int getItemCount() {
        return mPointsList.size();
    }

    class PointHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvDeviceNumber;

        public PointHolder(@NonNull View itemView) {
            super(itemView);
            tvDeviceNumber = itemView.findViewById(R.id.itemPoint_tvDeviceNumber);

            itemView.setOnClickListener(this);
        }

        public void bindPoints(PointItem point, String status) {
            tvDeviceNumber.setText(point.deviceNumber);
            if(point.done){
                tvDeviceNumber.setTextColor(mContext.getResources().getColor(R.color.document_created_text));
            } else {
            }

        }

        @Override
        public void onClick(View v) {
            String routeId = mPointsList.get(getAdapterPosition()).routeId;
            String pointId = mPointsList.get(getLayoutPosition()).id;
            Intent intent = ControlMeterReadingsActivity.newIntent(mContext, routeId, pointId);
            mContext.startActivity(intent);
        }
    }
}
