package ru.mobnius.vote.ui.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.mobnius.vote.R;

public class ExpandableLayout extends LinearLayout {
    private TextView mTextView;
    private ImageView mImageView;
    private RecyclerView mRecyclerView;

    public ExpandableLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        TypedArray a = context.obtainStyledAttributes(attributeSet,
                R.styleable.ExpandableLayout, 0, 0);
        String labelText = a.getString(R.styleable.ExpandableLayout_labelText);
        a.recycle();
        setOrientation(LinearLayout.VERTICAL);
        setBackground(getResources().getDrawable(R.drawable.card_background, null));
        setElevation(4);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.expandable_layout, this, true);
        }

        mTextView = findViewById(R.id.text_label);
        mImageView = findViewById(R.id.image_arrow);
        mRecyclerView = findViewById(R.id.recycler_view_container);

        mTextView.setText(labelText);

        mImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mRecyclerView.isShown()) {
                    mRecyclerView.setVisibility(GONE);
                }else mRecyclerView.setVisibility(VISIBLE);
            }
        });
    }

    public void setTextLabel(String value){ mTextView.setText(value);}

    public void setArrowImage(Drawable drawable){ mImageView.setImageDrawable(drawable);}

    public void setCustomRecAdapter (RecyclerView.Adapter adapter){
       mRecyclerView.setAdapter(adapter);
    }
    public void setCustomRecLayManager (LinearLayoutManager layManager){
        mRecyclerView.setLayoutManager(layManager);
    }
}
