package ru.mobnius.vote.ui.fragment.photoGallery;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.mobnius.vote.Names;
import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.BaseFragment;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;

public class GalleryFragment extends BaseFragment implements View.OnClickListener, UpdateUIListener {
    private GalleryListener mGalleryListener;
    private RecyclerView recyclerView;
    private TextView tvNoAttachments;

    public static GalleryFragment createInstance(String pointId) {
        Bundle args = new Bundle();
        args.putString(Names.POINT_ID, pointId);

        GalleryFragment galleryFragment = new GalleryFragment();
        galleryFragment.setArguments(args);
        return galleryFragment;
    }

    public static GalleryFragment updateInstance(String resultId) {
        Bundle args = new Bundle();
        args.putString(Names.RESULT_ID, resultId);

        GalleryFragment galleryFragment = new GalleryFragment();
        galleryFragment.setArguments(args);
        return galleryFragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof GalleryListener) {
            mGalleryListener = (GalleryListener)context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments().containsKey(Names.RESULT_ID)) {
            String resultId = getArguments().getString(Names.RESULT_ID);
            mGalleryListener.getPhotoManager().updatePictures(DataManager.getInstance().getImages(resultId));
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        recyclerView = view.findViewById(R.id.fPointPhoto_rvMeters);
        tvNoAttachments = view.findViewById(R.id.fPointPhoto_tvNoAttachments);
        if (mGalleryListener.getPhotoManager().getImages().length > 0) {
            onUpdateUI();
        }

        ImageButton ibGoToDocument = view.findViewById(R.id.fPointPhoto_ibGoToAct);
        ImageView ivBig = view.findViewById(R.id.fPointPhoto_ivPhotoContainer);
        Button btnSave = view.findViewById(R.id.fPointPhoto_btnSave);
        ivBig.setOnClickListener(this);
        ibGoToDocument.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        return view;
    }



    @Override
    public void onClick(View v) {
        FragmentManager fm = getActivity().getSupportFragmentManager();

        switch (v.getId()) {
            case R.id.fPointPhoto_ibGoToAct:
            case R.id.fPointPhoto_btnSave:
                fm.popBackStack();
                if(v.getId() == R.id.fPointPhoto_btnSave) {
                    mGalleryListener.onSave();
                }
                break;

            case R.id.fPointPhoto_ivPhotoContainer:
                mGalleryListener.onCamera();

                break;
        }
    }

    /**
     * Обновление списка
     */
    @Override
    public void onUpdateUI() {
        recyclerView.setAdapter(new PhotoAdapter(getContext(), mGalleryListener.getPhotoManager()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (mGalleryListener.getPhotoManager().getImages().length>0){
            tvNoAttachments.setVisibility(View.GONE);
        }
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.POINT_PHOTO;
    }
}
