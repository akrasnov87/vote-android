package ru.mobnius.vote.ui.fragment.data;

import android.content.Intent;

import ru.mobnius.vote.Names;
import ru.mobnius.vote.data.manager.PhotoManager;

public class GalleryUtil {
    private final static String MANAGER = "manager";

    public static PhotoManager deSerializable(Intent intent) {
        if(intent.hasExtra(MANAGER)) {
            return (PhotoManager) intent.getSerializableExtra(MANAGER);
        } else {
            return null;
        }
    }

    public static void serializable(Intent intent, PhotoManager photoManager) {
        intent.putExtra(MANAGER, photoManager);
    }
}
