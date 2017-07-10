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
    float origin = 0.0f;
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
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                origin = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                if((event.getX()-origin)>10.0f){
                    mModePicker.setCurrentMode(true);
                }
                if((event.getX()-origin)<-10.0f){
                    mModePicker.setCurrentMode(false);
                }
                break;
        }
        return true;
//        return mDector.onTouchEvent(event);
    }

    private class G extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if(distanceX>10.0f){
                mModePicker.setCurrentMode(true);
            }
            if(distanceX<-10.0f){
                mModePicker.setCurrentMode(false);
            }
            LogUtil.I("zyq","distanceX = "+distanceX);
            return true;
        }

    }
}
