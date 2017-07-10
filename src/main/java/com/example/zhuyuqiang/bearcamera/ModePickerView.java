package com.example.zhuyuqiang.bearcamera;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.hardware.Camera;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by zhuyuqiang on 2017/7/7.
 */

public class ModePickerView extends LinearLayout {

    private final static String TAG = "ModePickerView";
    private int mChildCount = 0;
    private int mCurrentMode = 0;
    private int mWidth = 0;
    private int mHeight = 0;
    private int mLeft = -100;
    private int mChildWidth = 0;
    private int mNormalHeight = 0;
    private CameraModeChangeListener mListener;

    public interface CameraModeChangeListener{
        public void onCameraModeChange(int currentMode);
    }

    public void setCameraModeChangerListener(CameraModeChangeListener listener){
        this.mListener = listener;
    }

    public ModePickerView(Context context) {
        this(context, null);
    }

    public ModePickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public int getCurrentMode(){
        if (mCurrentMode>0 && mCurrentMode<=(mChildCount-1)){
            return mCurrentMode;
        }else{
            return -1;
        }

    }

    public void setCurrentMode(boolean add) {
        if (add) {
            mCurrentMode = mCurrentMode >= mChildCount-1 ? mCurrentMode : mCurrentMode + 1;
        } else {
            mCurrentMode = mCurrentMode - 1 < 0 ? mCurrentMode : (mCurrentMode - 1);
        }
        LogUtil.I(TAG, "mCurrentMode=" + mCurrentMode + ",mChildCount=" + mChildCount);
        if(mListener != null){
            mListener.onCameraModeChange(mCurrentMode);
        }
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mChildWidth = 0;
        mChildCount = getChildCount();
        int width = 0;
        for (int i = 0; i < mChildCount; i++) {
            View view = getChildAt(i);
            ((TextView) view).setTextSize(12);
            ((TextView) view).setTextColor(Color.WHITE);
            if (mCurrentMode == i) {
                ((TextView) view).setTextSize(18);
                ((TextView) view).setTextColor(Color.RED);
                measureChild(view, widthMeasureSpec, heightMeasureSpec);
                mChildWidth = width + view.getMeasuredWidth() / 2;
            } else {
                measureChild(view, widthMeasureSpec, heightMeasureSpec);
                mNormalHeight = (view.getMeasuredHeight() + this.getPaddingBottom() + this.getPaddingTop());
            }
            width += view.getMeasuredWidth();
            mHeight = mHeight >= (view.getMeasuredHeight() + this.getPaddingBottom() + this.getPaddingTop()) ? mHeight :
                    (view.getMeasuredHeight() + this.getPaddingBottom() + this.getPaddingTop());
        }
        LogUtil.I(TAG, "mWidth=" + mWidth + ",mHeight=" + mHeight);
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        mWidth = getWidth();
        mHeight = getHeight();
        int Left = (mWidth / 2 - mChildWidth);
        for (int i = 0; i < mChildCount; i++) {
            View view = getChildAt(i);
            LogUtil.I(TAG, "i = " + i + ",Child width = " + view.getMeasuredWidth() + ",Child height=" + view.getMeasuredHeight() + ",mLeft = " + mLeft);
            if (i == mCurrentMode) {
                view.layout(Left, top, Left + view.getMeasuredWidth(), view.getMeasuredHeight());
            } else {
                view.layout(Left, top + (mHeight - mNormalHeight) / 2, Left + view.getMeasuredWidth(), view.getMeasuredHeight());
            }
            Left = Left + view.getMeasuredWidth();
        }
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

}
