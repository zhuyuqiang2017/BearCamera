package com.example.zhuyuqiang.bearcamera;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by zhuyuqiang on 2017/7/7.
 */

public class ModePickerView extends LinearLayout {

    private final static String TAG = "ModePickerView";
    private int mChildCount = 0;
    private int mCurrentMode = 0;
    private int mWidth = 0;
    private int mHeight = 0;
    private int mInterval = 12;
    private int mLeft = -100;
    public ModePickerView(Context context) {
        super(context);
    }

    public ModePickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ModePickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ModePickerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setCurrentMode(int mode){
        mCurrentMode = mode;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mChildCount = getChildCount();
        for (int i = 0;i<mChildCount;i++){
            View view = getChildAt(i);
            measureChild(view,widthMeasureSpec,heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        mWidth = getWidth();
        mHeight = getHeight();
        int Left = mLeft;
        for(int i = 0;i<mChildCount;i++){
            View view = getChildAt(i);
            LogUtil.I(TAG,"i = "+i+",Child width = "+view.getMeasuredWidth()+",Child height="+view.getMeasuredHeight()+",mLeft = "+mLeft);
            Left = Left+mInterval;
            view.layout(Left,top,Left+view.getMeasuredWidth(),view.getMeasuredHeight());
            Left = Left+view.getMeasuredWidth();
        }
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void setMovement(int move){
        mLeft += move;
        requestLayout();
    }
}
