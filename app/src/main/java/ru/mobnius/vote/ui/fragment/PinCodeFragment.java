package ru.mobnius.vote.ui.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

import ru.mobnius.vote.Names;
import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.BaseFragment;
import ru.mobnius.vote.data.manager.authorization.Authorization;
import ru.mobnius.vote.data.manager.authorization.AuthorizationCache;
import ru.mobnius.vote.data.manager.configuration.PreferencesManager;
import ru.mobnius.vote.data.manager.credentials.BasicUser;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.ui.activity.LoginActivity;
import ru.mobnius.vote.ui.activity.RouteListActivity;
import ru.mobnius.vote.ui.component.PinCodeLinLay;

import static ru.mobnius.vote.ui.component.PinCodeLinLay.PIN_CODE_LENGTH;

public class PinCodeFragment extends BaseFragment
        implements View.OnClickListener, PinCodeLinLay.PinChangeListener, PinCodeLinLay.CheckPin, PinCodeLinLay.FocusChange {

    private PinCodeLinLay pclPinPoints;

    private String pinDigits;
    private String savedPinDigits;
    private String tempPin;
    private String login;

    private boolean isCreateMode;
    private int confirmPin;

    private List<Button> mButtonList;
    private ImageButton ibClear;

    private AuthorizationCache cache;
    private Authorization mAuthorization;
    private OnPinEnableComplete mOnPinEnableComplete;

    public static PinCodeFragment newInstance(String pin, String login) {
        PinCodeFragment pinCodeFragment;
        if (pin != null) {
            pinCodeFragment = new PinCodeFragment();
            Bundle args = new Bundle();
            args.putString(Names.PIN, pin);
            args.putString(Names.LOGIN, login);
            pinCodeFragment.setArguments(args);
        } else {
            pinCodeFragment = new PinCodeFragment();
            Bundle args = new Bundle();
            args.putString(Names.LOGIN, login);
            pinCodeFragment.setArguments(args);
        }
        return pinCodeFragment;
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.PIN_CODE;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnPinEnableComplete) {
            mOnPinEnableComplete = (OnPinEnableComplete) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().containsKey(Names.PIN)) {
                savedPinDigits = getArguments().getString(Names.PIN);
                isCreateMode = false;
            } else {
                isCreateMode = true;
                confirmPin = 0;
            }
            login = getArguments().getString(Names.LOGIN);
        }
        mAuthorization = Authorization.getInstance();
        cache = new AuthorizationCache(getContext());
        pinDigits = "";
        mButtonList = new ArrayList<>();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pin_code, container, false);
        pclPinPoints = v.findViewById(R.id.pinFragment_pclTop);
        pclPinPoints.setPinChangeListener(this);
        pclPinPoints.setCheckPinListener(this);
        pclPinPoints.setFocusChangeListener(this);

        TextView tvForgotPin = v.findViewById(R.id.pinFragment_tvForgotPin);

        ibClear = v.findViewById(R.id.pinFragment_ibClear);
        ImageButton ibFingerPrint = v.findViewById(R.id.pinFragment_ibFingerPrint);

        Button btnOne = v.findViewById(R.id.pinFragment_btnOne);
        Button btnTwo = v.findViewById(R.id.pinFragment_btnTwo);
        Button btnThree = v.findViewById(R.id.pinFragment_btnThree);
        Button btnFour = v.findViewById(R.id.pinFragment_btnFour);
        Button btnFive = v.findViewById(R.id.pinFragment_btnFive);
        Button btnSix = v.findViewById(R.id.pinFragment_btnSix);
        Button btnSeven = v.findViewById(R.id.pinFragment_btnSeven);
        Button btnEight = v.findViewById(R.id.pinFragment_btnEight);
        Button btnNine = v.findViewById(R.id.pinFragment_btnNine);
        Button btnZero = v.findViewById(R.id.pinFragment_btnZero);

        mButtonList.add(btnOne);
        mButtonList.add(btnTwo);
        mButtonList.add(btnThree);
        mButtonList.add(btnFour);
        mButtonList.add(btnFive);
        mButtonList.add(btnSix);
        mButtonList.add(btnSeven);
        mButtonList.add(btnEight);
        mButtonList.add(btnNine);
        mButtonList.add(btnZero);
        mButtonList.add(btnOne);
        ibClear.setOnClickListener(this);
        if (isCreateMode) {
            tvForgotPin.setVisibility(View.GONE);
        }
        tvForgotPin.setOnClickListener(this);
        for (Button button : mButtonList) {
            button.setOnClickListener(this);
        }

        if (mAuthorization.isAutoSignIn() && !isCreateMode && Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            fingerPrintActivate(ibFingerPrint);

        }
        return v;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.pinFragment_tvForgotPin) {
            new AlertDialog.Builder(getContext()).setTitle("Сброс пин-кода")
                    .setMessage("Сбросить пин-код и авторизоаться через логин и пароль?")
                    .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            PreferencesManager.createInstance(getApplication().getApplicationContext(), login);
                            PreferencesManager.getInstance().getSharedPreferences().edit().
                                    putBoolean(PreferencesManager.PIN, false).apply();
                            cache.update(login, null, new Date());
                            mAuthorization.destroy();
                            FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
                            int resId = requireActivity().getWindow().getDecorView().findViewById(android.R.id.content).getId();
                            ft.replace(resId, LoginFragment.newInstance()).commit();
                        }
                    })
                    .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            if (v.getId() == R.id.pinFragment_ibClear) {
                pclPinPoints.onPinClear();
            } else {
                Button btn = (Button) v;
                pclPinPoints.onPinEnter();
                if (pinDigits.length() < PIN_CODE_LENGTH) {
                    pinDigits = pinDigits + btn.getText().toString();
                }
                pclPinPoints.setPinnedPoints();
            }
        }
    }

    @Override
    public void onEnter() {
    }

    @Override
    public void onClear() {
        pclPinPoints.setUnPinnedPoint();
        if (!pinDigits.isEmpty()) {
            pinDigits = pinDigits.substring(0, pinDigits.length() - 1);
        }
    }

    @Override
    public void onPinComplete() {
        for (Button button : mButtonList) {
            button.setClickable(false);
        }
        ibClear.setClickable(false);

        if (!isCreateMode) {
            if (savedPinDigits.equals(pinDigits)) {
                onAuthorize();
                return;
            } else {
                Toast.makeText(getContext(), "Неправильный пин", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (confirmPin == 0) {
                confirmPin++;
                tempPin = pinDigits;
                Toast.makeText(getContext(), "Подтвердите пин-код", Toast.LENGTH_SHORT).show();
            } else {
                if (pinDigits.equals(tempPin)) {
                    if (mOnPinEnableComplete != null) {
                        mOnPinEnableComplete.onPinActivated();
                    }
                    cache.update(login, pinDigits, new Date());
                    requireActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
                    Intent i = new Intent(getContext(), LoginActivity.class);
                    startActivity(i);
                    requireActivity().finish();
                    Toast.makeText(getContext(), "Вход по пин-коду активирован", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getContext(), "Пин-коды не совпадают, порпобуйте еще раз", Toast.LENGTH_SHORT).show();
                    confirmPin = 0;
                }
            }
        }
        pinDigits = "";
    }

    @Override
    public void onRunnableComplete() {
        for (Button button : mButtonList) {
            button.setClickable(true);
            ibClear.setClickable(true);
        }
    }

    private void onAuthorize() {
        String[] names = cache.getNames();
        for (String log : names) {
            if (log.equals(login)) {
                BasicUser user = cache.read(log);
                Authorization.getInstance().setUser(user);
                cache.update(log, savedPinDigits, new Date());
            }
        }
        getApplication().onAuthorized();
        Intent intent = new Intent(getContext(), RouteListActivity.class);
        startActivity(intent);

    }

    private void fingerPrintActivate(ImageButton imageButton) {
        BiometricManager manager = BiometricManager.from(requireActivity());
        if (manager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS) {
            imageButton.setVisibility(View.VISIBLE);
            Executor executor = ContextCompat.getMainExecutor(requireContext());
            final BiometricPrompt biometricPrompt = new BiometricPrompt(requireActivity(),
                    executor, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode,
                                                  @NonNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                }

                @Override
                public void onAuthenticationSucceeded(
                        @NonNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    onAuthorize();
                    Toast.makeText(getContext(),
                            "Вы авторизовались", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    Toast.makeText(getContext(), "Несовпадение отпечатка",
                            Toast.LENGTH_SHORT)
                            .show();
                }
            });
            final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Авторизация по отпечатку пальца")
                    .setSubtitle("Приложите палец к сенсору")
                    .setNegativeButtonText("Отмена")
                    .build();


            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    biometricPrompt.authenticate(promptInfo);
                }
            });
        }
    }

    public interface OnPinEnableComplete {
        void onPinActivated();
    }
}
