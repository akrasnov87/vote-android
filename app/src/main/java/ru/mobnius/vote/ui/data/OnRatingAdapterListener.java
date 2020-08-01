package ru.mobnius.vote.ui.data;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.mobnius.vote.ui.adapter.holder.RatingHolder;
import ru.mobnius.vote.ui.model.RatingItemModel;

public interface OnRatingAdapterListener {
    void updateList(List<RatingItemModel> list);

    void update(Integer uik);

    int getPosition(long userId);
}
