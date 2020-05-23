package ru.mobnius.vote.ui.component;

import android.content.Context;
import android.content.res.TypedArray;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import ru.mobnius.vote.R;

public class TextFieldView extends LinearLayout {

    private TextView tvFieldLabel;
    private TextView tvFieldText;

    public TextFieldView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.TextFieldView, 0, 0);
        String labelText = a.getString(R.styleable.TextFieldView_fieldLabel);
        @SuppressWarnings("ResourceAsColor")
        String valueText = a.getString(R.styleable.TextFieldView_fieldText);
        a.recycle();
        setOrientation(LinearLayout.VERTICAL);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        inflater.inflate(R.layout.text_field, this, true);

        tvFieldLabel = findViewById(R.id.text_field_label);
        tvFieldText = findViewById(R.id.text_field_value);

        tvFieldLabel.setText(labelText);
        tvFieldText.setText(valueText);

    }

    public TextFieldView(Context context) {
        this(context, null);
    }

    public void setFieldText(String value){
        tvFieldText.setText(value);
    }
}
