package ru.mobnius.vote.ui.fragment;

import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import java.util.Date;
import java.util.Objects;

import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.authorization.Authorization;
import ru.mobnius.vote.data.manager.authorization.AuthorizationCache;
import ru.mobnius.vote.data.manager.configuration.PreferencesManager;
import ru.mobnius.vote.data.manager.credentials.BasicUser;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.data.manager.exception.IExceptionGroup;
import ru.mobnius.vote.data.manager.exception.IExceptionIntercept;
import ru.mobnius.vote.data.manager.exception.MyUncaughtExceptionHandler;
import ru.mobnius.vote.utils.VersionUtil;

public class PrefFragment extends PreferenceFragmentCompat implements IExceptionIntercept, Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {
    private String debugSummary = "Режим отладки: %s";
    private String pinSummary = "Авторизация по пину: %s";
    private int clickToVersion = 0;

    private Preference pVersion;
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
        //getPreferenceManager().setSharedPreferencesName(PreferencesManager.getInstance().getPreferenceName());
        addPreferencesFromResource(R.xml.pref);

        pVersion = findPreference(PreferencesManager.APP_VERSION);
        if (pVersion != null) {
            pVersion.setOnPreferenceClickListener(this);
        }

        spDebug = findPreference(PreferencesManager.DEBUG);
        if (spDebug != null) {
            spDebug.setEnabled(PreferencesManager.getInstance().isDebug());
            spDebug.setOnPreferenceChangeListener(this);
        }
        spPin = findPreference(PreferencesManager.PIN);
        if (spPin != null) {
            spPin.setOnPreferenceChangeListener(this);
        }

        pCreateError = findPreference(PreferencesManager.GENERATED_ERROR);
        if (pCreateError != null) {
            pCreateError.setVisible(PreferencesManager.getInstance().isDebug());
            pCreateError.setOnPreferenceClickListener(this);
        }


    }

    @Override
    public void onResume() {
        super.onResume();

        if (pVersion != null) {
            pVersion.setSummary(VersionUtil.getVersionName(Objects.requireNonNull(getActivity())));
        }

        if (spDebug != null) {
            spDebug.setSummary(String.format(debugSummary, PreferencesManager.getInstance().isDebug() ? "включен" : "отключен"));
            spDebug.setChecked(PreferencesManager.getInstance().isDebug());
        }
        if (spPin != null) {
            spPin.setSummary(String.format(pinSummary, PreferencesManager.getInstance().isPinAuth() ? "включена" : "отключена"));
            spPin.setChecked(PreferencesManager.getInstance().isPinAuth());
        }

    }


    public void onExceptionIntercept() {
        Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler(Thread.getDefaultUncaughtExceptionHandler(), getExceptionGroup(), getExceptionCode(), getContext()));
    }

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

                    Toast.makeText(getActivity(), String.format("Режим отладки активирован."), Toast.LENGTH_SHORT).show();
                    clickToVersion = 0;
                }
                break;

            case PreferencesManager.GENERATED_ERROR:
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
                spPin.setSummary(String.format(pinSummary, pinValue ? "включена" : "отключена"));
                PreferencesManager.getInstance().getSharedPreferences().edit().putBoolean(PreferencesManager.PIN, pinValue).apply();
                AuthorizationCache cache = new AuthorizationCache(getContext());
                BasicUser user = Authorization.getInstance().getLastAuthUser();
                if (pinValue) {
                    PinCodeFragment fragment = PinCodeFragment.newInstance(null, user.getCredentials().login);
                    Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.single_fragment_container, fragment).commit();
                } else {
                    cache.update(user.getCredentials().login, "", new Date());
                }
                break;
        }
        return true;
    }

    protected int getPosition(String[] array, String value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(value))
                return i;
        }

        return -1;
    }

    protected String getValueFromPosition(String[] names, String[] values, String value) {
        int position = getPosition(values, value);
        return names[position];
    }

    protected String getValueFromPosition(int names, int values, String value) {
        String[] Values = getResources().getStringArray(values);
        String[] Names = getResources().getStringArray(names);
        int position = getPosition(Values, value);
        return Names[position];
    }
}
