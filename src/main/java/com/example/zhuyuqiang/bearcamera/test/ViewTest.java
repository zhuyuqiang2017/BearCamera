package com.example.zhuyuqiang.bearcamera.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.example.zhuyuqiang.bearcamera.LogUtil;
import com.example.zhuyuqiang.bearcamera.ModePickerView;
import com.example.zhuyuqiang.bearcamera.R;

/**
 * Created by zhuyuqiang on 2017/7/7.
 */

public class ViewTest extends AppCompatActivity {

    private ModePickerView mModePicker;
    private G mG;
    private GestureDetector mDector;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        mModePicker = (ModePickerView) findViewById(R.id.mode_picker_container);
        mG = new G();
        mDector = new GestureDetector(this,mG);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mDector.onTouchEvent(event);
    }

    private class G extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            mModePicker.setMovement((int)(distanceX+0.5f));
            LogUtil.I("zyq","distanceX = "+distanceX);
            return true;
        }
    }
}
