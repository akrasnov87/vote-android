package ru.mobnius.vote.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import ru.mobnius.vote.Names;
import ru.mobnius.vote.R;
import ru.mobnius.vote.data.ICallback;
import ru.mobnius.vote.data.Meta;
import ru.mobnius.vote.data.manager.BaseFragment;
import ru.mobnius.vote.data.manager.INetworkChange;
import ru.mobnius.vote.data.manager.authorization.Authorization;
import ru.mobnius.vote.data.manager.Version;

import ru.mobnius.vote.data.manager.authorization.AuthorizationCache;
import ru.mobnius.vote.data.manager.credentials.BasicUser;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.ui.activity.MainActivity;
import ru.mobnius.vote.data.manager.authorization.AuthorizationMeta;
import ru.mobnius.vote.ui.model.LoginModel;
import ru.mobnius.vote.ui.viewModel.LoginViewModel;
import ru.mobnius.vote.utils.NetworkUtil;
import ru.mobnius.vote.utils.VersionUtil;


public class LoginFragment extends BaseFragment implements View.OnClickListener, TextWatcher, View.OnFocusChangeListener, INetworkChange {
    private final String LOGIN = "login";
    private final String PASSWORD = "password";

    private LoginViewModel mLoginViewModel;
    private Authorization mAuthorization;

    private String login = "";
    private String password = "";

    private TextView tvVersion;
    private TextView tvNetwork;
    private TextView tvServer;
    private EditText etLogin;
    private ImageButton ibLoginClear;
    private ImageButton ibPasswordClear;
    private ImageButton ibShowPassword;
    private TextInputLayout tilLogin;
    private TextInputLayout tilPassword;
    private EditText etPassword;
    private Button btnSignIn;
    private BasicUser mBasicUser;
    private ProgressBar mProgressBar;

    public static LoginFragment newInstance(boolean isPinForgotten) {
        LoginFragment loginFragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putBoolean(Names.PIN, isPinForgotten);
        loginFragment.setArguments(args);
        return loginFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuthorization = Authorization.getInstance();
        mBasicUser = mAuthorization.getLastAuthUser();
        mLoginViewModel = new LoginViewModel();

        if (savedInstanceState != null) {
            login = savedInstanceState.getString(LOGIN);
            password = savedInstanceState.getString(PASSWORD);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!Objects.requireNonNull(getArguments()).getBoolean(Names.PIN) && mBasicUser != null) {
            AuthorizationCache cache = new AuthorizationCache(getContext());
            String pin = cache.readPin(mBasicUser.getCredentials().login);
            if (!pin.isEmpty()) {
                PinCodeFragment fragment = PinCodeFragment.newInstance(pin, mBasicUser.getCredentials().login);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.single_fragment_container, fragment).commit();
            } else {
                if (mAuthorization.isAutoSignIn()) {
                    singIn(mBasicUser.getCredentials().login, mBasicUser.getCredentials().password);
                }
            }
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_authorization, container, false);

        etLogin = v.findViewById(R.id.fAuthorization_etLogin);
        tilLogin = v.findViewById(R.id.fAuthorization_tilLogin);
        etPassword = v.findViewById(R.id.fAuthorization_etPassword);
        tilPassword = v.findViewById(R.id.fAuthorization_tilPassword);
        tvNetwork = v.findViewById(R.id.fAuthorization_tvNoIntetnet);
        tvServer = v.findViewById(R.id.fAuthorization_tvNoServer);

        ibLoginClear = v.findViewById(R.id.fAuthorization_ibClearLogin);
        ibPasswordClear = v.findViewById(R.id.fAuthorization_ibClearPassword);
        ibShowPassword = v.findViewById(R.id.fAuthorization_ibShowPassword);
        btnSignIn = v.findViewById(R.id.fAuthorization_btnSignIn);

        tvVersion = v.findViewById(R.id.fAuthorization_tvVersion);
        tvVersion.setText(getString(R.string.versionShort, getVersion()));
        TextView tvBackToPin = v.findViewById(R.id.fAuthorization_tvBackToPin);
        mLoginViewModel.setModel(LoginModel.getInstance(login, password, getVersion()));
        mProgressBar = v.findViewById(R.id.fAuthorization_pbLoading);

        etLogin.addTextChangedListener(this);
        etPassword.addTextChangedListener(this);
        etLogin.setOnFocusChangeListener(this);
        etPassword.setOnFocusChangeListener(this);
        ibLoginClear.setOnClickListener(this);
        ibPasswordClear.setOnClickListener(this);
        ibShowPassword.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
        tvVersion.setOnClickListener(this);

        if (Objects.requireNonNull(getArguments()).getBoolean(Names.PIN)) {
            tvBackToPin.setVisibility(View.VISIBLE);
            tvBackToPin.setOnClickListener(this);
            btnSignIn.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    btnSignIn.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    int[] locations = new int[2];
                    btnSignIn.getLocationOnScreen(locations);
                    int btnLocation = locations[1];
                    Display display = getActivity().getWindowManager().getDefaultDisplay();
                    Point size = new Point();
                    display.getSize(size);
                    int screenHeight = size.y;
                    int toastPosition = (screenHeight - btnLocation - btnSignIn.getHeight()) / 2;
                    Toast toast = Toast.makeText(getContext(), "Введите логин и пароль. Пин-код будет сброшен", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, toastPosition);
                    toast.show();
                }
            });

        }

        mLoginViewModel.setNoSpaces(new EditText[]{etLogin, etPassword});

        return v;
    }

    public void onVersionClick() {
        String versionName = VersionUtil.getVersionName(Objects.requireNonNull(getContext()));
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
        toast(mLoginViewModel.getVersionToast(getString(R.string.versionToast), versionName, status));
    }

    /**
     * Получение версии приложения
     *
     * @return версия
     */
    private String getVersion() {
        String versionName = VersionUtil.getVersionName(Objects.requireNonNull(getContext()));
        if (new Version().getVersionParts(versionName)[2] == Version.PRODUCTION) {
            return VersionUtil.getShortVersionName(getContext());
        } else {
            return VersionUtil.getVersionName(getContext());
        }
    }

    private void toast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    private void onAuthorized() {
        getApplication().onAuthorized();

        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
        Objects.requireNonNull(getActivity()).finish();
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

    public void singIn(String login, String password) {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
        if (NetworkUtil.isNetworkAvailable(Objects.requireNonNull(getContext()))) {
            onSignOnline(login, password);
        } else {
            onSignOffline(login, password);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(LOGIN, etLogin.getText().toString());
        outState.putString(PASSWORD, etPassword.getText().toString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fAuthorization_tvVersion:
                onVersionClick();
                break;
            case R.id.fAuthorization_btnSignIn:
                singIn(etLogin.getText().toString(), etPassword.getText().toString());
                break;
            case R.id.fAuthorization_ibClearLogin:
                etLogin.setText("");
                break;
            case R.id.fAuthorization_ibClearPassword:
                etPassword.setText("");
                break;
            case R.id.fAuthorization_ibShowPassword:
                if (etPassword.getInputType() == (InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT)) {
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                    ibShowPassword.setImageDrawable(getResources().getDrawable(R.drawable.ic_visibility_off_outlined_24dp, null));
                } else {
                    etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    ibShowPassword.setImageDrawable(getResources().getDrawable(R.drawable.ic_visibility_outlined_24dp, null));
                }
                etPassword.setSelection(etPassword.getText().length());

                break;
            case R.id.fAuthorization_tvBackToPin:
                String pin = new AuthorizationCache(getContext()).readPin(mBasicUser.getCredentials().login);
                if (!pin.isEmpty()) {
                    PinCodeFragment fragment = PinCodeFragment.newInstance(pin, mBasicUser.getCredentials().login);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.single_fragment_container, fragment).commit();
                }
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
            tilLogin.setError(String.format(mLoginViewModel.minLength(login), getString(R.string.login)));
            if (s.toString().isEmpty()) {
                ibLoginClear.setVisibility(View.GONE);
            } else {
                if (etLogin.hasFocus()) {
                    ibLoginClear.setVisibility(View.VISIBLE);
                }
            }
        }

        if (etPassword.isFocused()) {
            tilPassword.setError(String.format(mLoginViewModel.minLength(password), getString(R.string.password)));
            if (s.toString().isEmpty()) {
                ibPasswordClear.setVisibility(View.GONE);
                ibShowPassword.setVisibility(View.GONE);
            } else {
                if (etPassword.hasFocus()) {
                    ibPasswordClear.setVisibility(View.VISIBLE);
                    ibShowPassword.setVisibility(View.VISIBLE);
                }
            }
        }

        btnSignIn.setEnabled(mLoginViewModel.isButtonEnable(login, password));
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.LOGIN;
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v == etLogin) {
            changeVisibility(ibLoginClear, hasFocus, etLogin.getText().toString());
        }
        if (v == etPassword) {
            changeVisibility(ibPasswordClear, hasFocus, etPassword.getText().toString());
            changeVisibility(ibShowPassword, hasFocus, etPassword.getText().toString());
        }

    }

    private void changeVisibility(ImageButton cross, boolean visible, String empty) {
        if (visible && !empty.equals("")) {
            cross.setVisibility(View.VISIBLE);
        }
        if (!visible) {
            cross.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNetworkChange(boolean online, boolean serverExists) {
        tvNetwork.setVisibility(online ? View.GONE : View.VISIBLE);
        tvServer.setVisibility(online ? View.GONE : View.VISIBLE);
    }
}
