package ru.mobnius.vote.ui.fragment.photoGallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;

import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.ui.model.Image;
import ru.mobnius.vote.utils.DateUtil;

public class PhotoHolder extends RecyclerView.ViewHolder {
    private ImageView ivPhoto;
    private TextView tvType;
    private TextView tvLocation;
    private TextView tvNotice;


    public PhotoHolder(@NonNull View itemView) {
        super(itemView);
        ivPhoto = itemView.findViewById(R.id.itemPointPhoto_ivThumbPhoto);
        tvType = itemView.findViewById(R.id.itemPointPhoto_tvActType);
        tvLocation = itemView.findViewById(R.id.itemPointPhoto_tvDate);
        tvNotice = itemView.findViewById(R.id.itemPointPhoto_tvNotice);
    }

    public void bindPhoto(Image image) {
        byte[] thumbPhoto = image.getThumbs();
        Bitmap bitmap = BitmapFactory.decodeByteArray(thumbPhoto, 0, thumbPhoto.length);
        ivPhoto.setImageBitmap(bitmap);

        tvType.setText(DataManager.getInstance().getImageType(image.getType()).c_name);

        Location location = image.getLocation();
        String locationInfo = DateUtil.convertDateToUserString(new Date(location.getTime()));
        tvLocation.setText(locationInfo);

        tvNotice.setText(image.getNotice());
    }
}
