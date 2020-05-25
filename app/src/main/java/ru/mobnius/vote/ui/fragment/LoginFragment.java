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

import com.google.android.material.textfield.TextInputLayout;

import ru.mobnius.vote.R;
import ru.mobnius.vote.data.ICallback;
import ru.mobnius.vote.data.Meta;
import ru.mobnius.vote.data.manager.BaseFragment;
import ru.mobnius.vote.data.manager.OnNetworkChangeListener;
import ru.mobnius.vote.data.manager.authorization.Authorization;
import ru.mobnius.vote.data.manager.Version;

import ru.mobnius.vote.data.manager.authorization.AuthorizationCache;
import ru.mobnius.vote.data.manager.credentials.BasicUser;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.ui.activity.RouteListActivity;
import ru.mobnius.vote.data.manager.authorization.AuthorizationMeta;
import ru.mobnius.vote.ui.data.ServerExistsAsyncTask;
import ru.mobnius.vote.utils.AuthUtil;
import ru.mobnius.vote.utils.NetworkUtil;
import ru.mobnius.vote.utils.UiUtil;
import ru.mobnius.vote.utils.VersionUtil;


public class LoginFragment extends BaseFragment
        implements View.OnClickListener, TextWatcher, View.OnFocusChangeListener, OnNetworkChangeListener {

    private Authorization mAuthorization;

    private TextView tvNetwork;
    private TextView tvServer;
    private EditText etLogin;
    private ImageButton ibLoginClear;
    private ImageButton ibPasswordClear;
    private ImageButton ibShowPassword;
    private EditText etPassword;
    private Button btnSignIn;
    private BasicUser mBasicUser;
    private ProgressBar mProgressBar;

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
                .execute(NetworkUtil.isNetworkAvailable(requireContext()));
        String pin = "";
        if(mBasicUser!=null) {
            AuthorizationCache cache = new AuthorizationCache(getContext());
            pin = cache.readPin(mBasicUser.getCredentials().login);
        }
        if (!pin.isEmpty()) {
            PinCodeFragment fragment = PinCodeFragment.newInstance(pin, mBasicUser.getCredentials().login);
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.single_fragment_container, fragment).commit();
        } else {
            if (mAuthorization.isAutoSignIn()) {
                singIn(mBasicUser.getCredentials().login, mBasicUser.getCredentials().password);
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

        UiUtil.setNoSpaces(new EditText[]{etLogin, etPassword});

        return v;
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
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    private void onAuthorized() {
        getApplication().onAuthorized();

        Intent intent = new Intent(getContext(), RouteListActivity.class);
        startActivity(intent);
        requireActivity().finish();
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
        if (NetworkUtil.isNetworkAvailable(requireContext())) {
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
                singIn(etLogin.getText().toString(), etPassword.getText().toString());
                break;

            case R.id.auth_login_clear:
                etLogin.setText("");
                break;

            case R.id.auth_password_clear:
                etPassword.setText("");
                break;

            case R.id.auth_password_show:
                if (etPassword.getInputType() == (InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT)) {
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                    ibShowPassword.setImageDrawable(getResources().getDrawable(R.drawable.ic_visibility_off_outlined_24dp));
                } else {
                    etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    ibShowPassword.setImageDrawable(getResources().getDrawable(R.drawable.ic_visibility_outlined_24dp));
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
    public void onNetworkChange(boolean online, boolean serverExists) {
        tvNetwork.setVisibility(online ? View.GONE : View.VISIBLE);
        tvServer.setVisibility(serverExists ? View.GONE : View.VISIBLE);
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.LOGIN;
    }
}
