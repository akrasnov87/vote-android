package ru.mobnius.vote.ui.fragment;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.BaseDialogFragment;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.authorization.Authorization;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.data.storage.models.Answer;
import ru.mobnius.vote.data.storage.models.Points;
import ru.mobnius.vote.data.storage.models.Results;
import ru.mobnius.vote.ui.fragment.tools.ContactAdapter;

public class StatisticDialogFragment extends BaseDialogFragment
    implements View.OnClickListener {

    private ImageButton ibClose;
    private TextView tvTxt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_fragment_statistic, container, false);

        setCancelable(false);

        ibClose = v.findViewById(R.id.statistic_close);
        ibClose.setOnClickListener(this);

        tvTxt = v.findViewById(R.id.statistic_txt);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(getDialog())).getWindow()).setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onResume() {
        super.onResume();

        List<Points> all = DataManager.getInstance().getDaoSession().getPointsDao().loadAll();
        List<Results> allResults = DataManager.getInstance().getDaoSession().getResultsDao().loadAll();

        StringBuilder builder = new StringBuilder();
        builder.append("<p><b>«" + DataManager.getInstance().getProfile().fio + "»</b>, доброго времени суток!</p>");

        builder.append("<p>А) Кол-во квартир – ").append(all.size()).append(" (100%)<br />");
        builder.append(" - кол-во пройденных квартир – " + allResults.size() + " ("+ getPercent(all.size(), allResults.size()) + "%)<br />");
        builder.append("из них:<br />");
        int i1 = getCount(allResults, "#000000");
        builder.append("\t\t- никого не было дома – " +i1+" ("+getPercent(allResults.size(), i1)+"%)<br />");
        int i2 = getCount(allResults, "#8b0000");
        builder.append("\t\t- отказ открывать дверь – общение не состоялось – " + i2 +" (" + getPercent(allResults.size(), i2)+"%)<br />");
        int i3 = getCount(allResults, "#800080");
        builder.append("\t\t- отказ открывать дверь – общение через дверь – " + i3 +" (" + getPercent(allResults.size(), i3)+"%)<br />");
        int i4 = getCount(allResults, "#00008b,#0000ff,#00ffff");
        builder.append("\t\t- дверь открыли – общение состоялось, АПМ НЕ вручен в руки – " + i4 +" (" + getPercent(allResults.size(), i4)+"%)<br />");
        int i5 = getCount(allResults, "#808000,#006400,#00ff00");
        builder.append("\t\t- дверь открыли – общение состоялось, АПМ вручен в руки – " + i5 +" (" + getPercent(allResults.size(), i5)+"%)<br />");
        int i6 = getCount(allResults, "#F57F17");
        builder.append("\t\t- другое – " + i6 +" (" + getPercent(allResults.size(), i6) + "%)</p>");

        int t1 = i4 + i5;
        builder.append("<p>Б) Кол-во открытых квартир – " + t1 +" (" + getPercent(allResults.size(), t1) + "%)<br />");
        builder.append("из них:<br />");

        int i7 = getOpenDoors(allResults, 1);
        builder.append(" - кол-во квартир, негативно настроенных – " + i7 +" (" + getPercent(t1, i7) + "%)<br />");
        int i8 = getOpenDoors(allResults, 2);
        builder.append(" - кол-во квартир потенциальных сторонников – " + i8 +" (" + getPercent(t1, i8) + "%)<br />");
        int i9 = getOpenDoors(allResults, 3);
        builder.append(" - кол-во квартир сторонников – " + i9 +" (" + getPercent(t1, i9) + "%)</p>");

        tvTxt.setText(Html.fromHtml(builder.toString()));
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.STATISTIC_DIALOG;
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }

    private int getPercent(int i, int j) {
        if(i == 0)
            return 0;
        return (int)((j * 100) / i);
    }

    private int getCount(List<Results> allResults, String color) {
        int colorCount = 0;
        for(Results result : allResults) {
            if(result.getAnswer().c_color.toLowerCase().equals(color.toLowerCase())) {
                colorCount++;
            }
        }
        return colorCount;
    }

    private int getOpenDoors(List<Results> allResults, int type) {
        int count = 0;
        for(Results result : allResults) {
            Answer answer = result.getAnswer();
            if(answer.c_color.toLowerCase().equals("#00008b,#0000ff,#00ffff") ||
                    answer.c_color.toLowerCase().equals("#808000,#006400,#00ff00")) {
                if(result.n_rating != null) {
                    if (type == 1  && result.n_rating <= 4) {
                        count++;
                    } else if(type == 2 && result.n_rating > 4 && result.n_rating <= 7) {
                        count++;
                    } else if(type == 3 && result.n_rating > 7) {
                        count++;
                    }
                }
            }
        }
        return count;
    }
}
