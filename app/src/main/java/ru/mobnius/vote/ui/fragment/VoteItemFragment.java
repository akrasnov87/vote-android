package ru.mobnius.vote.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.mobnius.vote.Names;
import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.BaseFragment;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.data.storage.models.Question;
import ru.mobnius.vote.ui.fragment.adapter.VoteButtonAdapter;

public class VoteItemFragment extends BaseFragment {

    private TextView tvDescription;
    private RecyclerView rvButtons;

    private long mVoteId;
    private String mRouteId;
    private String mPointId;

    public static VoteItemFragment createInstance(String routeId, String pointId, long voteId) {
        Bundle args = new Bundle();
        args.putLong(Names.VOTE_ID, voteId);
        args.putString(Names.POINT_ID, pointId);
        args.putString(Names.ROUTE_ID, routeId);

        VoteItemFragment voteItemFragment = new VoteItemFragment();
        voteItemFragment.setArguments(args);
        return voteItemFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mVoteId = getArguments().getLong(Names.VOTE_ID);
        mRouteId = getArguments().getString(Names.ROUTE_ID);
        mPointId = getArguments().getString(Names.POINT_ID);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vote_item, container, false);
        rvButtons = view.findViewById(R.id.rvButtons);
        tvDescription = view.findViewById(R.id.tvDescription);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        DataManager dataManager = DataManager.getInstance();

        rvButtons.setAdapter(new VoteButtonAdapter(getActivity(), dataManager.getAnswers(mVoteId)));
        rvButtons.setLayoutManager(new LinearLayoutManager(getActivity()));

        Question question = dataManager.getQuestion(mVoteId);
        if(question != null) {
            tvDescription.setText(question.c_text);
        }
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.VOTE_ITEM;
    }
}
