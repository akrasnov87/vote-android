package ru.mobnius.vote.ui.fragment.photoGallery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.PhotoManager;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {

    private PhotoManager mPhotoManager;
    private Context mContext;

    public PhotoAdapter(Context context, PhotoManager photoManager) {
        mPhotoManager = photoManager;
        mContext = context;
    }

    @NonNull
    @Override
    public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_point_photo, parent, false);
        return new PhotoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoHolder holder, int position) {
        holder.bindPhoto(mPhotoManager.getImages()[position]);
    }

    @Override
    public int getItemCount() {
        return mPhotoManager.getImages().length;
    }
}
