package com.example.zhuyuqiang.bearcamera;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by zhuyuqiang on 2017/7/6.
 */

public class BaseActivity extends AppCompatActivity {

    protected PermissionManager mPermissionManager;
    protected ViewManager mViewManager;
    private float modeChangeDistance = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPermissionManager = new PermissionManager(BaseActivity.this);
        mViewManager = new ViewManager();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPermissionManager.checkLaunchCameraPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                modeChangeDistance = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                if((event.getX()-modeChangeDistance)>10.0f){
                    mViewManager.setCurrentMode(true);
                }
                if((event.getX()-modeChangeDistance)<-10.0f){
                    mViewManager.setCurrentMode(false);
                }
                break;
        }
        return true;
    }
}
