package ru.mobnius.vote.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;

import ru.mobnius.vote.Names;
import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.BaseActivity;
import ru.mobnius.vote.data.manager.MobniusApplication;
import ru.mobnius.vote.data.manager.RequestManager;
import ru.mobnius.vote.data.manager.authorization.Authorization;
import ru.mobnius.vote.data.manager.authorization.AuthorizationCache;
import ru.mobnius.vote.data.manager.configuration.PreferencesManager;
import ru.mobnius.vote.data.manager.credentials.BasicUser;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.data.manager.exception.IExceptionGroup;
import ru.mobnius.vote.data.manager.exception.IExceptionIntercept;
import ru.mobnius.vote.data.manager.exception.MyUncaughtExceptionHandler;
import ru.mobnius.vote.ui.fragment.PinCodeFragment;
import ru.mobnius.vote.utils.AuditUtils;
import ru.mobnius.vote.utils.VersionUtil;

/**
 * Настройки
 */
public class SettingActivity extends BaseActivity {

    public static void setPinCodeFragment(AppCompatActivity context, String pin, String title) {
        BasicUser user = Authorization.getInstance().getLastAuthUser();
        PinCodeFragment fragment = PinCodeFragment.newInstance(pin, user.getCredentials().login);
        context.getSupportFragmentManager().beginTransaction().replace(R.id.single_fragment_container, fragment).addToBackStack(null).commit();
        Objects.requireNonNull(context.getSupportActionBar()).setSubtitle(title);
    }

    public static void setPrefFragment(AppCompatActivity context) {
        context.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.single_fragment_container, new PrefFragment())
                .commit();
        Objects.requireNonNull(context.getSupportActionBar()).setSubtitle(null);
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, SettingActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.master_container);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        setPrefFragment(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.SETTING;
    }

    public static class PrefFragment extends PreferenceFragmentCompat
            implements IExceptionIntercept,
            Preference.OnPreferenceChangeListener,
            Preference.OnPreferenceClickListener {

        private final String debugSummary = "Режим отладки: %s";
        private final String pinSummary = "Авторизация по пину: %s";
        private int clickToVersion = 0;

        private Preference pVersion;
        private Preference pServerVersion;
        private SwitchPreference spDebug;
        private SwitchPreference spPin;
        private Preference pCreateError;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            onExceptionIntercept();
        }

        @Override
        public void onCreatePreferences(Bundle bundle, String s) {
            addPreferencesFromResource(R.xml.pref);

            pServerVersion = findPreference(PreferencesManager.SERVER_APP_VERSION);
            Objects.requireNonNull(pServerVersion).setOnPreferenceClickListener(this);


            Preference syncInterval = findPreference(PreferencesManager.MBL_BG_SYNC_INTERVAL);
            Objects.requireNonNull(syncInterval).setSummary(String.format("Интервал синхронизации фоновых данных: %s мин.", PreferencesManager.getInstance().getSyncInterval() / 60000));

            Preference trackingInterval = findPreference(PreferencesManager.MBL_TRACK_INTERVAL);
            Objects.requireNonNull(trackingInterval).setSummary(String.format("Интервал получения гео-данных: %s мин.", PreferencesManager.getInstance().getTrackingInterval() / 60000));

            Preference telemetryInterval = findPreference(PreferencesManager.MBL_TELEMETRY_INTERVAL);
            Objects.requireNonNull(telemetryInterval).setSummary(String.format("Интервал сбора показаний мобильного устройства: %s мин.", PreferencesManager.getInstance().getTelemetryInterval() / 60000));

            pVersion = findPreference(PreferencesManager.APP_VERSION);
            Objects.requireNonNull(pVersion).setOnPreferenceClickListener(this);

            spDebug = findPreference(PreferencesManager.DEBUG);
            Objects.requireNonNull(spDebug).setEnabled(PreferencesManager.getInstance().isDebug());
            spDebug.setOnPreferenceChangeListener(this);

            spPin = findPreference(PreferencesManager.PIN);
            Objects.requireNonNull(spPin).setOnPreferenceChangeListener(this);

            pCreateError = findPreference(PreferencesManager.GENERATED_ERROR);
            Objects.requireNonNull(pCreateError).setVisible(PreferencesManager.getInstance().isDebug());
            pCreateError.setOnPreferenceClickListener(this);
        }

        @Override
        public void onResume() {
            super.onResume();

            pVersion.setSummary(VersionUtil.getVersionName(requireActivity()));

            spDebug.setSummary(String.format(debugSummary, PreferencesManager.getInstance().isDebug() ? "включен" : "отключен"));
            spDebug.setChecked(PreferencesManager.getInstance().isDebug());

            spPin.setSummary(String.format(pinSummary, PreferencesManager.getInstance().isPinAuth() ? "включена" : "отключена"));
            spPin.setChecked(PreferencesManager.getInstance().isPinAuth());

            new ServerAppVersionAsyncTask().execute();
        }

        @Override
        public void onExceptionIntercept() {
            Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler(Thread.getDefaultUncaughtExceptionHandler(), getExceptionGroup(), getExceptionCode(), getContext()));
        }

        @Override
        public String getExceptionGroup() {
            return IExceptionGroup.SETTING;
        }

        @Override
        public int getExceptionCode() {
            return IExceptionCode.SETTING;
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            switch (preference.getKey()) {
                case PreferencesManager.APP_VERSION:
                    clickToVersion++;
                    if (clickToVersion >= 6) {
                        PreferencesManager.getInstance().getSharedPreferences().edit().putBoolean(PreferencesManager.DEBUG, true).apply();
                        spDebug.setChecked(true);
                        spDebug.setEnabled(true);
                        pCreateError.setVisible(true);

                        Toast.makeText(getActivity(), "Режим отладки активирован.", Toast.LENGTH_SHORT).show();
                        clickToVersion = 0;
                    }
                    break;

                case PreferencesManager.GENERATED_ERROR:
                    //noinspection ResultOfMethodCallIgnored
                    Integer.parseInt("Проверка обработки ошибок");
                    break;
            }
            return false;
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            switch (preference.getKey()) {
                case PreferencesManager.DEBUG:
                    boolean debugValue = Boolean.parseBoolean(String.valueOf(newValue));
                    spDebug.setSummary(String.format(debugSummary, debugValue ? "включен" : "отключен"));
                    spDebug.setEnabled(debugValue);
                    PreferencesManager.getInstance().getSharedPreferences().edit().putBoolean(PreferencesManager.DEBUG, debugValue).apply();
                    pCreateError.setVisible(debugValue);
                    break;

                case PreferencesManager.PIN:
                    boolean pinValue = Boolean.parseBoolean(String.valueOf(newValue));
                    AuditUtils.write(String.format(pinSummary, pinValue ? "включена" : "отключена"), AuditUtils.PREF_PIN, AuditUtils.Level.HIGH);
                    spPin.setSummary(String.format(pinSummary, pinValue ? "включена" : "отключена"));

                    if (pinValue) {
                        SettingActivity.setPinCodeFragment((AppCompatActivity)requireActivity(), null, "Установка пин-кода");
                    } else {
                        BasicUser user = Authorization.getInstance().getLastAuthUser();
                        new AuthorizationCache(requireActivity()).update(user.getCredentials().login, "", new Date());
                        PreferencesManager.getInstance().setPinAuth(false);
                    }
                    break;
            }
            return true;
        }

        @SuppressLint("StaticFieldLeak")
        private class ServerAppVersionAsyncTask extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    return RequestManager.version(MobniusApplication.getBaseUrl());
                } catch (IOException e) {
                    return "0.0.0.0";
                }
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (pServerVersion != null) {
                    if (!s.equals("0.0.0.0")) {

                        if (VersionUtil.isUpgradeVersion(requireActivity(), s)) {
                            pServerVersion.setVisible(true);
                            pServerVersion.setSummary("Доступна новая версия " + s);
                            pServerVersion.setIntent(new Intent().setAction(Intent.ACTION_VIEW).setData(
                                    Uri.parse(MobniusApplication.getBaseUrl() + Names.UPDATE_URL)));

                            if (pVersion != null) {
                                pVersion.setSummary(VersionUtil.getVersionName(requireActivity()));
                            }
                        } else {
                            pServerVersion.setVisible(false);
                            if (pVersion != null) {
                                pVersion.setSummary("Установлена последняя версия " + VersionUtil.getVersionName(requireActivity()));
                            }
                        }
                    } else {
                        pServerVersion.setVisible(false);
                        if (pVersion != null) {
                            pVersion.setSummary("Установлена последняя версия " + VersionUtil.getVersionName(requireActivity()));
                        }
                    }
                }
            }
        }
    }
}
