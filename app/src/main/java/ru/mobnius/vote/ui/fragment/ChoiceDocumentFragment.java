package ru.mobnius.vote.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import ru.mobnius.vote.Names;
import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.BaseFragment;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.ui.activity.ControlMeterReadingsActivity;
import ru.mobnius.vote.ui.model.PointResult;

public class ChoiceDocumentFragment extends BaseFragment {
    private RecyclerView mRecyclerView;
    private List<PointResult> mPointResults;
    private String mPointId;
    private String mRouteId;

    public static ChoiceDocumentFragment newInstance(String pointId, String routeId) {
        Bundle args = new Bundle();
        args.putString(Names.POINT_ID, pointId);
        args.putString(Names.ROUTE_ID, routeId);
        ChoiceDocumentFragment choiceDocumentFragment = new ChoiceDocumentFragment();
        choiceDocumentFragment.setArguments(args);
        return choiceDocumentFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        mPointId = getArguments().getString(Names.POINT_ID);
        mRouteId = getArguments().getString(Names.ROUTE_ID);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_control_meter_readings, container, false);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPointResults = DataManager.getInstance().getPointDocuments(mPointId);
        mRecyclerView.setAdapter(new ChoiceDocumentAdapter());
    }

    private class ChoiceDocumentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvDocumentType;
        private PointResult mPointResult;

        public ChoiceDocumentHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
        }

        public void bindDocuments(PointResult pointResult) {
            mPointResult = pointResult;

            tvDocumentType.setText(pointResult.getName());
            if (pointResult.isExists()) {
                tvDocumentType.setTextColor(getResources().getColor(R.color.document_created_text));
            }
        }

        public void disableDocuments() {
            Objects.requireNonNull(getView()).setEnabled(false);
            tvDocumentType.setTextColor(Color.GRAY);
        }

        @Override
        public void onClick(View v) {
            // TODO: 21/01/2020 добавить проверку на выбор типа акта
            Intent intent;
            if(mPointResult.isExists()) {
                intent = ControlMeterReadingsActivity.newIntent(getActivity(), mPointResult.getResultId());
            } else {
                intent = ControlMeterReadingsActivity.newIntent(getActivity(), mRouteId, mPointId, mPointResult.getResultTypeId());
            }
            startActivity(intent);
        }
    }

    private class ChoiceDocumentAdapter extends RecyclerView.Adapter<ChoiceDocumentHolder> {
        @NonNull
        @Override
        public ChoiceDocumentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = null;
            return new ChoiceDocumentHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ChoiceDocumentHolder holder, int position) {
            PointResult pointResult = mPointResults.get(position);
            holder.bindDocuments(pointResult);

            if (pointResult.isLock()) {
                holder.disableDocuments();
            }
        }

        @Override
        public int getItemCount() {
            return mPointResults.size();
        }
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.CHOICE_DOCUMENT;
    }
}
