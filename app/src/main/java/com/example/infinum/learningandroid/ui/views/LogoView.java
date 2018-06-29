package com.example.infinum.learningandroid.ui.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

import com.example.infinum.learningandroid.R;

import timber.log.Timber;

/**
 * Created by infinum on 29/07/2017.
 */

public class LogoView extends View {

    private BitmapDrawable logoImage;
    private int backgroundColor;

    private int logoPositionY;

    public LogoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LogoView, 0, 0);

        try {
            logoImage = (BitmapDrawable)a.getDrawable(R.styleable.LogoView_logoImage);
            backgroundColor = a.getColor(R.styleable.LogoView_backgroundColor, Color.TRANSPARENT);
        }
        finally {
            a.recycle();
        }
    }

    public void playAnimation() {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        final int height = displayMetrics.heightPixels;
        updateHeight(height);

        int centerY = (height - logoImage.getMinimumHeight())/2;
        ValueAnimator positionAnimator = ValueAnimator.ofInt(centerY, 0);
        positionAnimator.setDuration(getResources().getInteger(R.integer.logo_anim_duration));
        positionAnimator.setInterpolator(new DecelerateInterpolator());
        positionAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                logoPositionY = (int)valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        positionAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animator) {
                updateHeight(logoImage.getMinimumHeight());
            }
        });
        positionAnimator.start();

        if(Build.VERSION.SDK_INT >= 21) {
            ValueAnimator colorAnimator = ValueAnimator.ofArgb(backgroundColor, Color.TRANSPARENT);
            colorAnimator.setDuration(getResources().getInteger(R.integer.logo_anim_duration));
            colorAnimator.setInterpolator(new DecelerateInterpolator());
            colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    backgroundColor = (int)valueAnimator.getAnimatedValue();
                    invalidate();
                }
            });
            colorAnimator.start();
        }
    }

    private void updateHeight(int height) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(getLayoutParams());
        params.height = height;
        int marginTop = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                (int)getResources().getDimension(R.dimen.spacing_2x), getResources().getDisplayMetrics());
        params.topMargin = marginTop;
        setLayoutParams(params);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = logoImage.getMinimumWidth();
        int desiredHeight = logoImage.getMinimumHeight();

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if(widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        }
        else if(widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(desiredWidth, widthSize);
        }
        else {
            width = desiredWidth;
        }

        if(heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        }
        else if(heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(desiredHeight, heightSize);
        }
        else {
            height = desiredHeight;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float x = (canvas.getWidth() - logoImage.getMinimumWidth())/2;

        canvas.drawColor(backgroundColor);
        canvas.drawBitmap(logoImage.getBitmap(), x, logoPositionY, null);
    }
}
