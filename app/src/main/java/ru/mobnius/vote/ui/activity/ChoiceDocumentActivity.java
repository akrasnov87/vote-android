package ru.mobnius.vote.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import ru.mobnius.vote.Names;
import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.ui.fragment.ChoiceDocumentFragment;
import ru.mobnius.vote.ui.model.PointInfo;

public class ChoiceDocumentActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context context, String pointId, String routeId) {
        Intent intent = new Intent(context, ChoiceDocumentActivity.class);
        intent.putExtra(Names.POINT_ID, pointId);
        intent.putExtra(Names.ROUTE_ID, routeId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected Fragment createFragment() {
        return ChoiceDocumentFragment.newInstance(getIntent().getStringExtra(Names.POINT_ID), getIntent().getStringExtra(Names.ROUTE_ID));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_choice_document, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.choiceDocument_Info:
                startActivity(PointInfoActivity.newIntent(this, getIntent().getStringExtra(Names.POINT_ID)));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.CHOICE_DOCUMENT;
    }
}
