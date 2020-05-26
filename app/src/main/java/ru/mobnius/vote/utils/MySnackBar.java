package ru.mobnius.vote.utils;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;

import ru.mobnius.vote.Names;
import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.MobniusApplication;

public class MySnackBar {
    public static Snackbar make(@NonNull View view, @NonNull CharSequence text, int duration) {

        Snackbar snack = Snackbar.make(view, text, duration);
        View v = snack.getView();
        TextView tv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
        tv.setTextColor(v.getContext().getResources().getColor(R.color.colorSecondaryText));
        return snack;
    }
}
