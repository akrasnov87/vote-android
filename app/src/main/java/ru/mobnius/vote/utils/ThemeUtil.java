package ru.mobnius.vote.utils;

import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;

import ru.mobnius.vote.R;

import static ru.mobnius.vote.data.GlobalSettings.ENVIRONMENT;

public class ThemeUtil {
    public static void changeColor(AppCompatActivity activity) {
        if(ENVIRONMENT.equals("test")) {
            if (Build.VERSION.SDK_INT >= 21) {
                activity.getWindow().setNavigationBarColor(activity.getResources().getColor(R.color.colorSecondary));
                activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.colorSecondaryLight));
            }
        }
    }
}
