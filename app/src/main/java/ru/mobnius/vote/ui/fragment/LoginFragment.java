package ru.mobnius.vote.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ru.mobnius.vote.R;
import ru.mobnius.vote.data.ICallback;
import ru.mobnius.vote.data.Meta;
import ru.mobnius.vote.data.manager.BaseFragment;
import ru.mobnius.vote.data.manager.MobniusApplication;
import ru.mobnius.vote.data.manager.OnNetworkChangeListener;
import ru.mobnius.vote.data.manager.Version;
import ru.mobnius.vote.data.manager.authorization.Authorization;
import ru.mobnius.vote.data.manager.authorization.AuthorizationMeta;
import ru.mobnius.vote.data.manager.configuration.PreferencesManager;
import ru.mobnius.vote.data.manager.credentials.BasicUser;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.ui.activity.LoginActivity;
import ru.mobnius.vote.ui.activity.RouteListActivity;
import ru.mobnius.vote.ui.data.ServerExistsAsyncTask;
import ru.mobnius.vote.utils.AuthUtil;
import ru.mobnius.vote.utils.NetworkUtil;
import ru.mobnius.vote.utils.VersionUtil;


public class LoginFragment extends BaseFragment
        implements View.OnClickListener, TextWatcher, View.OnFocusChangeListener, OnNetworkChangeListener {

    private Authorization mAuthorization;

    private TextView tvNetwork;
    private TextView tvServer;
    private TextView tvSlowInternet;
    private EditText etLogin;
    private ImageButton ibLoginClear;
    private ImageButton ibPasswordClear;
    private ImageButton ibShowPassword;
    private EditText etPassword;
    private Button btnSignIn;
    private BasicUser mBasicUser;
    private ProgressBar mProgressBar;

    private MobniusApplication.ConfigurationAsyncTask mConfigurationAsyncTask;

    public static LoginFragment newInstance() {
        LoginFragment loginFragment = new LoginFragment();
        Bundle args = new Bundle();
        loginFragment.setArguments(args);
        return loginFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuthorization = Authorization.getInstance();
        mBasicUser = mAuthorization.getLastAuthUser();
    }

    @Override
    public void onResume() {
        super.onResume();

        new ServerExistsAsyncTask(this)
                .execute(NetworkUtil.isNetworkAvailable(requireContext()), NetworkUtil.isConnectionFast(requireContext()));

        if (mAuthorization.isAutoSignIn()) {
            String login = mBasicUser.getCredentials().login;
            PreferencesManager.createInstance(getApplication().getApplicationContext(), login);
            if (PreferencesManager.getInstance().isDebug()) {
                singIn(mBasicUser.getCredentials().login, mBasicUser.getCredentials().password);
            } else {
                etLogin.setText(mBasicUser.getCredentials().login);
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        etLogin = v.findViewById(R.id.auth_login);
        etPassword = v.findViewById(R.id.auth_password);
        tvNetwork = v.findViewById(R.id.auth_no_internet);
        tvServer = v.findViewById(R.id.auth_no_server);
        tvSlowInternet = v.findViewById(R.id.auth_slow_internet);

        ibLoginClear = v.findViewById(R.id.auth_login_clear);
        ibPasswordClear = v.findViewById(R.id.auth_password_clear);
        ibShowPassword = v.findViewById(R.id.auth_password_show);
        btnSignIn = v.findViewById(R.id.auth_sign_in);

        TextView tvVersion = v.findViewById(R.id.auth_version);
        tvVersion.setText(getString(R.string.versionShort, getVersion()));

        mProgressBar = v.findViewById(R.id.auth_progress);

        etLogin.addTextChangedListener(this);
        etPassword.addTextChangedListener(this);
        etLogin.setOnFocusChangeListener(this);
        etPassword.setOnFocusChangeListener(this);
        ibLoginClear.setOnClickListener(this);
        ibPasswordClear.setOnClickListener(this);
        ibShowPassword.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
        tvVersion.setOnClickListener(this);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        getApplication().addNetworkChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getApplication().removeNetworkChangeListener(this);
    }

    private void onVersionClick() {
        String versionName = VersionUtil.getVersionName(requireContext());
        String status = "неизвестен";
        switch (new Version().getVersionParts(versionName)[2]) {
            case 0:
                status = getString(R.string.alphaText);
                break;
            case 1:
                status = getString(R.string.betaText);
                break;
            case 2:
                status = getString(R.string.releaseCandidateText);
                break;
            case 3:
                status = getString(R.string.productionText);
                break;
        }
        toast(AuthUtil.getVersionToast(getString(R.string.versionToast), versionName, status));
    }

    /**
     * Получение версии приложения
     *
     * @return версия
     */
    private String getVersion() {
        String versionName = VersionUtil.getVersionName(requireContext());
        if (new Version().getVersionParts(versionName)[2] == Version.PRODUCTION) {
            return VersionUtil.getShortVersionName(getContext());
        } else {
            return VersionUtil.getVersionName(requireContext());
        }
    }

    private void toast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void onAuthorized() {
        mConfigurationAsyncTask = new MobniusApplication.ConfigurationAsyncTask(new MobniusApplication.OnConfigurationLoadedListener() {
            @Override
            public void onConfigurationLoaded(boolean configRefreshed) {
                getApplication().onAuthorized(LoginActivity.LOGIN);

                Intent intent = new Intent(getContext(), RouteListActivity.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });
        mConfigurationAsyncTask.execute();
    }

    private void failAuthorized(String message) {
        if (!message.isEmpty()) {
            toast(message);
        }
        mProgressBar.setVisibility(View.GONE);
    }

    private void onSignOnline(final String login, final String password) {
        mAuthorization.onSignIn(login, password, Authorization.ONLINE, new ICallback() {
            @Override
            public void onResult(Meta meta) {
                AuthorizationMeta authorizationMeta = (AuthorizationMeta) meta;

                switch (authorizationMeta.getStatus()) {
                    case Meta.NOT_AUTHORIZATION:
                        failAuthorized(meta.getMessage());
                        break;
                    case Meta.OK:
                        if (mAuthorization.isInspector()) {
                            toast(authorizationMeta.getMessage());
                            onAuthorized();
                        } else {
                            failAuthorized(getString(R.string.accessDenied));
                        }
                        break;

                    default:
                        failAuthorized(getString(R.string.serverNotAvailable));
                        break;
                }
            }
        });
    }

    private void onSignOffline(String login, String password) {
        mBasicUser = mAuthorization.getLastAuthUser();
        if (mBasicUser == null) {
            failAuthorized(getString(R.string.offlineDenied));
            return;
        }
        mAuthorization.onSignIn(login, password, Authorization.OFFLINE, new ICallback() {
            @Override
            public void onResult(Meta meta) {

                AuthorizationMeta authorizationMeta = (AuthorizationMeta) meta;
                switch (authorizationMeta.getStatus()) {
                    case Meta.NOT_AUTHORIZATION:
                        failAuthorized(meta.getMessage());
                        break;
                    case Meta.OK:
                        if (mAuthorization.isInspector()) {
                            toast(authorizationMeta.getMessage());
                            onAuthorized();
                        } else {
                            failAuthorized(getString(R.string.accessDenied));
                        }
                        break;

                    default:
                        failAuthorized(getString(R.string.serverNotAvailable));
                        break;
                }
            }
        });
    }

    private void singIn(String login, String password) {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        if (NetworkUtil.isNetworkAvailable(requireContext()) && NetworkUtil.isConnectionFast(requireContext())) {
            onSignOnline(login, password);
        } else {
            onSignOffline(login, password);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.auth_version:
                onVersionClick();
                break;

            case R.id.auth_sign_in:
                singIn(etLogin.getText().toString().trim(), etPassword.getText().toString().trim());
                break;

            case R.id.auth_login_clear:
                etLogin.setText("");
                break;

            case R.id.auth_password_clear:
                etPassword.setText("");
                break;

            case R.id.auth_password_show:
                if (etPassword.getInputType() == (InputType.TYPE_NUMBER_VARIATION_PASSWORD | InputType.TYPE_CLASS_NUMBER)) {
                    etPassword.setInputType(InputType.TYPE_CLASS_NUMBER);
                    ibShowPassword.setBackground(getResources().getDrawable(R.drawable.ic_visibility_outlined_24dp));
                } else {
                    etPassword.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD | InputType.TYPE_CLASS_NUMBER);
                    ibShowPassword.setBackground(getResources().getDrawable(R.drawable.ic_visibility_off_outlined_24dp));
                }
                etPassword.setSelection(etPassword.getText().length());
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        String login = etLogin.getText().toString();
        String password = etPassword.getText().toString();

        if (etLogin.isFocused()) {
            String msg = AuthUtil.minLength(login);
            etLogin.setError(msg.isEmpty() ? null : msg);
            ibLoginClear.setVisibility(login.isEmpty() ? View.GONE : View.VISIBLE);
        }

        if (etPassword.isFocused()) {
            String msg = AuthUtil.minLength(password);
            etPassword.setError(msg.isEmpty() ? null : msg);
            ibPasswordClear.setVisibility(password.isEmpty() ? View.GONE : View.VISIBLE);
            ibShowPassword.setVisibility(password.isEmpty() ? View.GONE : View.VISIBLE);
        }

        btnSignIn.setEnabled(AuthUtil.isButtonEnable(login, password));
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v instanceof EditText) {
            String str = ((EditText) v).getText().toString();

            switch (v.getId()) {
                case R.id.auth_login:
                    changeVisibility(ibLoginClear, hasFocus, str);
                    break;

                case R.id.auth_password:
                    changeVisibility(ibPasswordClear, hasFocus, str);
                    changeVisibility(ibShowPassword, hasFocus, str);
                    break;
            }
        }
    }

    private void changeVisibility(ImageButton img, boolean visible, String empty) {
        if (visible && !empty.equals("")) {
            img.setVisibility(View.VISIBLE);
        }
        if (!visible) {
            img.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNetworkChange(boolean online, boolean serverExists, boolean fasted) {
        tvNetwork.setVisibility(online ? View.GONE : View.VISIBLE);
        tvServer.setVisibility(serverExists ? View.GONE : View.VISIBLE);
        tvSlowInternet.setVisibility(fasted ? View.GONE : View.VISIBLE);
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.LOGIN;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mConfigurationAsyncTask != null) {
            mConfigurationAsyncTask.cancel(true);
            mConfigurationAsyncTask = null;
        }
    }
}
