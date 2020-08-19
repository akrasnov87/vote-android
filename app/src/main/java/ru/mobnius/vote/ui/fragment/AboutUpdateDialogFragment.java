package ru.mobnius.vote.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
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

import org.w3c.dom.Text;

import java.util.List;
import java.util.Objects;

import ru.mobnius.vote.Names;
import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.BaseDialogFragment;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.MobniusApplication;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.data.storage.models.Results;
import ru.mobnius.vote.ui.data.DigestsAsyncTask;
import ru.mobnius.vote.utils.VersionUtil;

public class AboutUpdateDialogFragment extends BaseDialogFragment
    implements View.OnClickListener, DigestsAsyncTask.OnDigestsLoadedListener {

    private TextView tvDescription;
    private TextView tvSync;
    private Button mButtonUpdate;
    private DigestsAsyncTask mDigestsAsyncTask;
    private LocaleDataAsyncTask mLocaleDataAsyncTask;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_fragment_about_update, container, false);

        tvSync = v.findViewById(R.id.about_update_no_sync);
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
        mDigestsAsyncTask = new DigestsAsyncTask(this);
        mDigestsAsyncTask.execute(VersionUtil.getVersionName(getContext()));

        mLocaleDataAsyncTask = new LocaleDataAsyncTask();
        mLocaleDataAsyncTask.execute();
    }

    @Override
    public void onStop() {
        super.onStop();

        if(mDigestsAsyncTask != null){
            mDigestsAsyncTask.cancel(true);
        }

        if(mLocaleDataAsyncTask != null){
            mLocaleDataAsyncTask.cancel(true);
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

    @SuppressLint("StaticFieldLeak")
    class LocaleDataAsyncTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            int pointCount = 0;
            List<Results> results = DataManager.getInstance().getDaoSession().getResultsDao().loadAll();
            for (Results result : results) {
                if (!result.isSynchronization) {
                    pointCount++;
                }
            }
            return pointCount;
        }

        @Override
        protected void onPostExecute(final Integer integer) {
            super.onPostExecute(integer);

            tvSync.setVisibility(integer > 0 ? View.VISIBLE : View.GONE);
            mButtonUpdate.setEnabled(integer == 0);
        }
    }
}
