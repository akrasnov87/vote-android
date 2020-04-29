package ru.mobnius.vote.ui.fragment.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.mobnius.vote.R;
import ru.mobnius.vote.ui.activity.QuestionActivity;
import ru.mobnius.vote.ui.model.PointItem;

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
            holder.bindPoints(mPointsList.get(position));
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

        public void bindPoints(PointItem point) {
            tvDeviceNumber.setText(point.deviceNumber);
            if(point.done) {
                tvDeviceNumber.setTextColor(mContext.getResources().getColor(R.color.document_created_text));
            }else{
                tvDeviceNumber.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            }
        }

        @Override
        public void onClick(View v) {
            PointItem pointItem = mPointsList.get(getAdapterPosition());
            String routeId = pointItem.routeId;
            String pointId = pointItem.id;
            Intent intent = QuestionActivity.newIntent(mContext, routeId, pointId);
            mContext.startActivity(intent);
        }
    }
}
