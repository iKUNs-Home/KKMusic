package com.ikunkun.kunmusic.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ikunkun.kunmusic.R;

public class MusicRoundProgressView extends View {
    private Paint mPaint;
    private int mPaintColor;
    private float mRadius;
    private float mRingRadius;
    private float mStrokeWidth;
    private int mCenterX;
    private int mCenterY;
    private static int mTotalProgress;
    private static int mProgress;
    private static View mView;

    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            Bundle bundle = msg.getData();
            int duration = bundle.getInt("duration");
            int currentPosition = bundle.getInt("currentPosition");
            setProgress(currentPosition, duration);
        }
    };

    public MusicRoundProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressView);
        mRadius = typedArray.getDimension(R.styleable.RoundProgressView_radius, 40);
        mStrokeWidth = typedArray.getDimension(R.styleable.RoundProgressView_strokeWidth, 5);
        mPaintColor = typedArray.getColor(R.styleable.RoundProgressView_strokeColor, 0xFFFFFFFF);
        mRingRadius = mRadius + mStrokeWidth / 2;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mPaintColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);

        mView = getRootView();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mCenterX = getWidth() / 2;
        mCenterY = getHeight() / 2;
        if (mProgress > 0) {
            RectF rectF = new RectF();
            rectF.left = mCenterX - mRingRadius;
            rectF.top = mCenterY - mRingRadius;
            rectF.right = mRingRadius * 2 + (mCenterX - mRingRadius);
            rectF.bottom = mRingRadius * 2 + (mCenterY - mRingRadius);
            canvas.drawArc(rectF, -90, ((float) mProgress / mTotalProgress) * 360, false, mPaint);
        }
    }

    public static void setProgress(int progress, int totalProgress) {
        mProgress = progress;
        mTotalProgress = totalProgress;
        mView.postInvalidate();
    }
}
