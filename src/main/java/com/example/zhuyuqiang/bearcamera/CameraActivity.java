package com.example.zhuyuqiang.bearcamera;

import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

public class CameraActivity extends BaseActivity {

    private SurfaceView mCameraPreview;
    private PreviewCallBack mPreviewCallBack;
    private Camera mCamera;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCameraPreview = (SurfaceView) findViewById(R.id.camera_preview);
        mPreviewCallBack = new PreviewCallBack();
        mCameraPreview.getHolder().addCallback(mPreviewCallBack);
    }

    private class PreviewCallBack implements SurfaceHolder.Callback{
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            try {
                mCamera = Camera.open();
                mCamera.setPreviewDisplay(surfaceHolder);
                mCamera.setDisplayOrientation(90);
                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            mCamera.stopPreview();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mCamera != null){
            mCamera.release();
        }
    }
}
