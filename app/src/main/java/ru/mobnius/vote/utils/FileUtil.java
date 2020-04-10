package ru.mobnius.vote.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import java.io.File;

public class FileUtil {
    /**
     *
     * @param context
     * @param documentFolder Environment.DIRECTORY_PICTURES | Environment.DIRECTORY_DOCUMENTS
     * @return
     */
    @SuppressWarnings({"deprecation"})
    public static File getRoot(Context context, String documentFolder) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            return Environment.getExternalStorageDirectory();
        } else {
            return context.getExternalFilesDir(documentFolder);
        }
    }
}
