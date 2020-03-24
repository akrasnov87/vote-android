package ru.mobnius.vote.ui.fragment.adapter;

import android.content.Context;
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
import ru.mobnius.vote.ui.activity.ChoiceDocumentActivity;
import ru.mobnius.vote.ui.component.TextFieldView;
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
        private TextFieldView tvSubscrNumber;
        private TextFieldView tvAdress;
        private TextFieldView tvFIO;
        private ImageView ivSyncStatus;
        private ImageView ivDoneStatus;

        public PointHolder(@NonNull View itemView) {
            super(itemView);
            tvDeviceNumber = itemView.findViewById(R.id.itemPoint_tvDeviceNumber);
            tvSubscrNumber = itemView.findViewById(R.id.itemPoint_tvSubscrNumber);
            tvAdress = itemView.findViewById(R.id.itemPoint_tvAdress);
            tvFIO = itemView.findViewById(R.id.itemPoint_tvFIO);
            ivSyncStatus = itemView.findViewById(R.id.itemPoint_ivSyncStatus);
            ivDoneStatus = itemView.findViewById(R.id.itemPoint_ivDoneStatus);

            itemView.setOnClickListener(this);
        }

        public void bindPoints(PointItem point, String status) {
            tvDeviceNumber.setText(point.deviceNumber);
            if(point.done){
                tvDeviceNumber.setTextColor(mContext.getResources().getColor(R.color.document_created_text));
                ivDoneStatus.setVisibility(View.VISIBLE);
                ivSyncStatus.setImageDrawable(point.sync ? mContext.getDrawable(R.drawable.ic_sync_done_green_24dp) : mContext.getDrawable(R.drawable.ic_sync_problem_red_24dp));
            } else {
                tvSubscrNumber.setVisibility(View.VISIBLE);
                tvAdress.setVisibility(View.VISIBLE);
                tvFIO.setVisibility(View.VISIBLE);
                tvFIO.setFieldText(point.fio);
                tvSubscrNumber.setFieldText(point.subscrNumber);
                tvAdress.setFieldText(point.address);
            }

        }

        @Override
        public void onClick(View v) {
            String routeId = mPointsList.get(getAdapterPosition()).routeId;
            mContext.startActivity(ChoiceDocumentActivity.newIntent(mContext, mPointsList.get(getLayoutPosition()).id, routeId));
        }
    }
}
