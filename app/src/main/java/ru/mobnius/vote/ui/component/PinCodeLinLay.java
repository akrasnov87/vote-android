package ru.mobnius.vote.ui.component;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import ru.mobnius.vote.R;

public class PinCodeLinLay extends LinearLayout {
    private PinChangeListener mPinChangeListener;
    private CheckPin mCheckPin;
    private FocusChange mFocusChange;

    private ImageView firstImage;
    private ImageView secondImage;
    private ImageView thirdImage;
    private ImageView fourthImage;
    private final Drawable filledPinPointImage;
    private final Drawable emptyPinPointImage;
    private int pinnedPoints;

    public static final int PIN_CODE_LENGTH = 4;

    public enum PinDotStatus {
        /**
         * Первая точка не закрашена
         */
        FIRST_CLEAR,
        /**
         * Первая точка закрашена
         */
        FIRST_FILLED
    }

    private PinDotStatus pinDotStatus = PinDotStatus.FIRST_CLEAR;
    public void setPinDotStatus(PinDotStatus dotStatus) {
        this.pinDotStatus = dotStatus;
    }

    public PinDotStatus getPinDotStatus() {
        return this.pinDotStatus;
    }

    public PinCodeLinLay(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        pinnedPoints = 0;
        filledPinPointImage = getResources().getDrawable(R.drawable.ic_pin_circle_filled_24dp);
        emptyPinPointImage = getResources().getDrawable(R.drawable.ic_pin_circle_empty_24dp);

        setOrientation(LinearLayout.HORIZONTAL);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View rootView = inflater.inflate(R.layout.pin_code_lin, this, false);
        this.addView(rootView);
        firstImage = findViewById(R.id.pinCodeLin_first);
        secondImage = findViewById(R.id.pinCodeLin_second);
        thirdImage = findViewById(R.id.pinCodeLin_third);
        fourthImage = findViewById(R.id.pinCodeLin_fourth);
    }

    public void setPinnedPoints() {
        pinnedPoints++;
        if (pinnedPoints==1){
            setPinDotStatus(PinDotStatus.FIRST_FILLED);
        }
        setPinPoint(pinnedPoints, filledPinPointImage);
        if (pinnedPoints == PIN_CODE_LENGTH) {

            pinnedPoints = 0;
            setPinDotStatus(PinDotStatus.FIRST_CLEAR);
            mCheckPin.onPinComplete();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    clearPinPoint(emptyPinPointImage);

                    mFocusChange.onRunnableComplete();
                }
            }, 400);

        }
    }
    public void setUnPinnedPoint() {
        if (pinnedPoints==0){
            return;
        }
        setPinPoint(pinnedPoints, emptyPinPointImage);
        pinnedPoints-- ;
        if (pinnedPoints==0){
            setPinDotStatus(PinDotStatus.FIRST_CLEAR);
        }
    }

    private void setPinPoint(int pinPointNumber, Drawable drawable) {
        switch (pinPointNumber) {
            case 1:
                firstImage.setImageDrawable(drawable);
                break;
            case 2:
                secondImage.setImageDrawable(drawable);
                break;
            case 3:
                thirdImage.setImageDrawable(drawable);
                break;
            case 4:
                fourthImage.setImageDrawable(drawable);
                break;
        }
    }

    private void clearPinPoint(Drawable drawable) {
        setPinDotStatus(PinDotStatus.FIRST_CLEAR);
        firstImage.setImageDrawable(drawable);
        secondImage.setImageDrawable(drawable);
        thirdImage.setImageDrawable(drawable);
        fourthImage.setImageDrawable(drawable);
    }

    public void onPinClear() {
        mPinChangeListener.onClear();
    }

    public void onPinEnter() {
        mPinChangeListener.onEnter();
    }


    public interface PinChangeListener {
        void onEnter();

        void onClear();
    }

    public interface CheckPin {
        void onPinComplete();
    }

    public interface FocusChange {
        void onRunnableComplete();
    }

    public void setPinChangeListener(PinChangeListener pinChangeListener) {
        mPinChangeListener = pinChangeListener;
    }

    public void setCheckPinListener(CheckPin checkPin) {
        mCheckPin = checkPin;
    }

    public void setFocusChangeListener(FocusChange focusChange){
        mFocusChange = focusChange;
    }
}
