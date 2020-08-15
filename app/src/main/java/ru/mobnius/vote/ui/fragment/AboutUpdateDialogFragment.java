package ru.mobnius.vote.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

import ru.mobnius.vote.Names;
import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.BaseDialogFragment;
import ru.mobnius.vote.data.manager.MobniusApplication;
import ru.mobnius.vote.data.manager.RequestManager;
import ru.mobnius.vote.data.manager.authorization.Authorization;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.data.manager.rpc.QueryData;
import ru.mobnius.vote.ui.data.DigestsAsyncTask;
import ru.mobnius.vote.ui.data.RatingAsyncTask;
import ru.mobnius.vote.utils.VersionUtil;

public class AboutUpdateDialogFragment extends BaseDialogFragment
    implements View.OnClickListener, DigestsAsyncTask.OnDigestsLoadedListener {

    private TextView tvDescription;
    private Button mButtonUpdate;
    private DigestsAsyncTask mTask;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_fragment_about_update, container, false);

        tvDescription = v.findViewById(R.id.about_update_txt);

        mButtonUpdate = v.findViewById(R.id.about_update_done);
        mButtonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = MobniusApplication.getBaseUrl() + Names.UPDATE_URL;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        setCancelable(false);

        ImageButton ibClose = v.findViewById(R.id.about_update_close);
        ibClose.setOnClickListener(this);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(getDialog())).getWindow()).setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        tvDescription.setText("Идет загрузка описания...");
        mTask = new DigestsAsyncTask(this);
        //mTask.execute("1.141.3.436");
        mTask.execute(VersionUtil.getVersionName(getContext()));
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mTask != null){
            mTask.cancel(true);
        }
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.UPDATE_ABOUT;
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }

    @Override
    public void onDigestsLoaded(String html) {
        tvDescription.setText(Html.fromHtml(html));
    }
}
