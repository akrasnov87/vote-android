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

import java.util.List;
import java.util.Objects;

import ru.mobnius.vote.Names;
import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.BaseDialogFragment;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.MobniusApplication;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.data.storage.models.MobileHelp;
import ru.mobnius.vote.data.storage.models.Results;
import ru.mobnius.vote.ui.data.DigestsAsyncTask;
import ru.mobnius.vote.utils.HelpUtil;
import ru.mobnius.vote.utils.VersionUtil;

public class HelpDialogFragment extends BaseDialogFragment
    implements View.OnClickListener {

    private TextView tvDescription;
    private TextView tvSync;
    private TextView tvTitle;
    private MobileHelp mobileHelp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_fragment_help, container, false);

        tvSync = v.findViewById(R.id.help_no_sync);
        tvDescription = v.findViewById(R.id.help_txt);
        tvTitle = v.findViewById(R.id.help_title_text);

        setCancelable(false);

        ImageButton ibClose = v.findViewById(R.id.help_close);
        ibClose.setOnClickListener(this);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(getDialog())).getWindow()).setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        tvSync.setVisibility(mobileHelp == null ? View.VISIBLE : View.GONE);
        if(mobileHelp != null) {
            tvTitle.setText(mobileHelp.c_title);
            tvDescription.setText((Html.fromHtml(mobileHelp.c_html)));
        }
    }

    public void bind(String key) {
        if(HelpUtil.isShow(key)) {
            mobileHelp = HelpUtil.get(key);
        }
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.HELP;
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
