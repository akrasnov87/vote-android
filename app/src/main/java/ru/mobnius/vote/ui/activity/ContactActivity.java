package ru.mobnius.vote.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import ru.mobnius.vote.Names;
import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.BaseActivity;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.data.storage.models.Contacts;
import ru.mobnius.vote.data.storage.models.ContactsDao;
import ru.mobnius.vote.data.storage.models.Feedbacks;
import ru.mobnius.vote.ui.adapter.MyContactsAdapter;
import ru.mobnius.vote.ui.adapter.NotificationAdapter;
import ru.mobnius.vote.ui.adapter.holder.MyContactsHolder;
import ru.mobnius.vote.ui.fragment.MyContactDialogFragment;

public class ContactActivity extends BaseActivity implements MyContactsHolder.OnMyContactListeners {

    private TextView tvEmpty;
    private RecyclerView rvList;

    private MyContactDialogFragment mDialogFragment;

    public static Intent getIntent(Context context) {
        return new Intent(context, ContactActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        tvEmpty = findViewById(R.id.my_contact_empty);
        rvList = findViewById(R.id.my_contact_list);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Override
    protected void onStart() {
        super.onStart();

        onUpdateList();
    }

    private void onUpdateList() {
        List<Contacts> contacts = DataManager.getInstance().getDaoSession().getContactsDao().queryBuilder().where(ContactsDao.Properties.B_disabled.eq(false)).orderDesc(ContactsDao.Properties.D_date).list();

        if (contacts.size() == 0) {
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvList.setAdapter(new MyContactsAdapter(this, contacts));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contact, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        if(item.getItemId() == R.id.contact_add) {
            mDialogFragment = new MyContactDialogFragment();
            mDialogFragment.show(getSupportFragmentManager(), "my-contacts");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.CONTACT_LIST;
    }

    @Override
    public void onSelectedContact(Contacts contact) {
        mDialogFragment = new MyContactDialogFragment();
        mDialogFragment.bind(contact);
        mDialogFragment.show(getSupportFragmentManager(), "my-contacts");
    }

    @Override
    public void onContactUpdate() {
        onUpdateList();
    }
}