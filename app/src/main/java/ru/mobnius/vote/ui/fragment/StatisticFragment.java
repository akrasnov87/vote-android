package ru.mobnius.vote.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.BaseFragment;

public class StatisticFragment extends BaseFragment {
    private static final String APPS = "total_apartments";
    private static final String OPEN = "open_apartments";
    private static final String SIGNATURES = "signatures_apartments";
    private String appTotal = "";
    private String openTotal = "";
    private String signsReceived = "";

    public static StatisticFragment openStatisticFragment(String apartments, String openApartments, String signatures){
        StatisticFragment f = new StatisticFragment();
        Bundle args = new Bundle();
        args.putString(APPS, apartments);
        args.putString(OPEN, openApartments);
        args.putString(SIGNATURES, signatures);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            appTotal = String.format(getString(R.string.apartment_count), getArguments().getString(APPS));
            openTotal = String.format(getString(R.string.apartment_open), getArguments().getString(OPEN));
            signsReceived = String.format(getString(R.string.signs_received), getArguments().getString(SIGNATURES));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_statistic, container, false);
        TextView apartmentsTotal = v.findViewById(R.id.fStatistic_tvAppTotal);
        TextView apartmentsOpen = v.findViewById(R.id.fStatistic_tvOpenApp);
        TextView signaturesReceived = v.findViewById(R.id.fStatistic_tvSignsTotal);
        apartmentsTotal.setText(appTotal);
        apartmentsOpen.setText(openTotal);
        signaturesReceived.setText(signsReceived);
        return v;
    }


    @Override
    public int getExceptionCode() {
        return 0;
    }
}
