package com.example.zhuyuqiang.bearcamera;

import android.hardware.Camera;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.IOException;

public class CameraActivity extends BaseActivity {

    private final String TAG = "CameraActivity";
    private SurfaceView mCameraPreview;
    private PreviewCallBack mPreviewCallBack;
    private Camera mCamera;
    private Camera.Parameters mParameters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.I(TAG,"onCreate...");
        setContentView(R.layout.activity_main);
        mCameraPreview = (SurfaceView) findViewById(R.id.camera_preview);
        mViewManager.setModePickerView((ModePickerView) findViewById(R.id.mode_picker_container));
        mViewManager.setCameraModeChangeListener(new ModeChangeListener());
        mPreviewCallBack = new PreviewCallBack();
        mCameraPreview.getHolder().addCallback(mPreviewCallBack);
    }

    private class PreviewCallBack implements SurfaceHolder.Callback{
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            LogUtil.I(TAG,"PreviewCallBack::surfaceCreated...");
            try {
                mCamera = Camera.open();
                mCamera.setPreviewDisplay(surfaceHolder);
                mCamera.setDisplayOrientation(90);
                mParameters = mCamera.getParameters();
                initCameraParameters();
                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
            LogUtil.I(TAG,"PreviewCallBack::surfaceChanged...");
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            LogUtil.I(TAG,"PreviewCallBack::surfaceDestroyed...");
            mCamera.stopPreview();
        }
    }

    private void initCameraParameters(){
        if(mParameters != null){
            mParameters.setAutoWhiteBalanceLock(true);
            mParameters.setAutoExposureLock(true);
            mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
            mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            mCamera.setParameters(mParameters);
            setCameraListener();
        }
    }

    private void setCameraListener(){
        if(mCamera != null){
            mCamera.setErrorCallback(new ErrorListener());
            mCamera.setOneShotPreviewCallback(new PreviewListener());
            mCamera.setZoomChangeListener(new ZoomListener());
            mCamera.autoFocus(new FocusListener());
        }
    }

    private class PictureListener implements Camera.PictureCallback{
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
            LogUtil.I(TAG,"PictureListener::onPictureTaken..."+" data lengths="+bytes.length);
            camera.startPreview();
        }
    }

    private class ShutterListener implements Camera.ShutterCallback{
        @Override
        public void onShutter() {
            LogUtil.I(TAG,"ShutterListener::onShutter...");
        }
    }

    private class FocusListener implements Camera.AutoFocusCallback{
        @Override
        public void onAutoFocus(boolean b, Camera camera) {
            LogUtil.I(TAG,"FocusListener::onAutoFocus..."+"result="+(b?"success":"fail"));
        }
    }

    private class ZoomListener implements Camera.OnZoomChangeListener{
        @Override
        public void onZoomChange(int i, boolean b, Camera camera) {
            LogUtil.I(TAG,"ZoomListener::onZoomChange...");
        }
    }

    private class ModeChangeListener implements ModePickerView.CameraModeChangeListener{
        @Override
        public void onCameraModeChange(int currentMode) {
            LogUtil.I(TAG,"ModeChangeListener::onCameraModeChange...");
        }
    }

    private class FaceDetectionListener implements Camera.FaceDetectionListener{
        @Override
        public void onFaceDetection(Camera.Face[] faces, Camera camera) {
            LogUtil.I(TAG,"FaceDetectionListener::onFaceDetection...");
        }
    }

    private class PreviewListener implements Camera.PreviewCallback{
        @Override
        public void onPreviewFrame(byte[] bytes, Camera camera) {
            LogUtil.I(TAG,"PreviewListener::onPreviewFrame..."+" data lengths="+bytes.length);
        }
    }

    private class ErrorListener implements Camera.ErrorCallback{
        @Override
        public void onError(int i, Camera camera) {
            LogUtil.I(TAG,"ErrorListener::onError...");
        }
    }

    public void test(View view){
        LogUtil.I(TAG,"test...");
//        mCamera.autoFocus(new FocusListener());
//        mCamera.stopPreview();
        mCamera.takePicture(new ShutterListener(),null,null,new PictureListener());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.I(TAG,"onDestroy...");
        if(mCamera != null){
            mCamera.release();
        }
    }


}
