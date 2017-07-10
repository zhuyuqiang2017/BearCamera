package com.example.zhuyuqiang.bearcamera;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends BaseActivity {

    private final String TAG = "CameraActivity";
    private SurfaceView mCameraPreview;
    private PreviewCallBack mPreviewCallBack;
    private Camera mCamera;
    private Camera.Parameters mParameters;
    private ImageButton mShutter;
    private boolean startRecording = false;
    private MediaRecorder mMediaRecorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.I(TAG,"onCreate...");
        setContentView(R.layout.activity_main);
        mCameraPreview = (SurfaceView) findViewById(R.id.camera_preview);
        mViewManager.setModePickerView((ModePickerView) findViewById(R.id.mode_picker_container));
        mShutter = (ImageButton) findViewById(R.id.camera_action);
        mShutter.setOnClickListener(new ClickListener());
        mViewManager.setCameraModeChangeListener(new ModeChangeListener());
        mPreviewCallBack = new PreviewCallBack();
        mCameraPreview.getHolder().addCallback(mPreviewCallBack);
        initCameraShutter();
    }

    private void initCameraShutter(){
        int mode = mViewManager.getCurrentMode();
        LogUtil.I(TAG,"initCameraShutter..."+"mode="+mode);
        switch (mode){
            case ModePickerView.MODE_PHOTO:
                mShutter.setImageDrawable(getDrawable(R.drawable.ic_camera_shutter_button_48dp));
                break;
            case ModePickerView.MODE_VIDEO:
                mShutter.setImageDrawable(getDrawable(R.drawable.video_button));
                break;
            default:
                mShutter.setImageDrawable(getDrawable(R.drawable.ic_camera_shutter_button_48dp));
                break;
        }
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
            switch (currentMode){
                case ModePickerView.MODE_PHOTO:
                    mShutter.setImageDrawable(getDrawable(R.drawable.ic_camera_shutter_button_48dp));
                    break;
                case ModePickerView.MODE_VIDEO:
                    mShutter.setImageDrawable(getDrawable(R.drawable.video_button));
                    break;
            }
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

//    public void test(View view){
//        LogUtil.I(TAG,"test...");
//        mCamera.takePicture(new ShutterListener(),null,null,new PictureListener());
//    }

    private class ClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            int mode = mViewManager.getCurrentMode();
            switch (mode){
                case ModePickerView.MODE_PHOTO:
//                    mCamera.unlock();
                    mCamera.takePicture(new ShutterListener(),null,null,new PictureListener());
//                    mCamera.lock();
                    break;
                case ModePickerView.MODE_VIDEO:
                    startRecordVideo();
                    break;
            }
        }
    }

    private void startRecordVideo() {
        if(!startRecording){
            mShutter.setImageDrawable(getDrawable(R.drawable.ic_camera_video_stop_48dp));
            mCamera.unlock();
            mMediaRecorder = new MediaRecorder();
            mMediaRecorder.reset();
            mMediaRecorder.setCamera(mCamera);
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            CamcorderProfile mCamcorderProfile = CamcorderProfile.get(Camera.CameraInfo.CAMERA_FACING_BACK,
                    CamcorderProfile.QUALITY_HIGH);
            mMediaRecorder.setProfile(mCamcorderProfile);
            mMediaRecorder.setOutputFile(getOutputMediaFile(0));
            mMediaRecorder.setPreviewDisplay(mCameraPreview.getHolder().getSurface());
            try {
                mMediaRecorder.prepare();
            } catch (IOException e) {
                startRecording = false;
                Toast.makeText(this, "fail", Toast.LENGTH_LONG).show();
                e.printStackTrace();
                mCamera.lock();
            }
//            mMediaRecorder.start();
            startRecording = true;
        }else{
            mShutter.setImageDrawable(getDrawable(R.drawable.ic_camera_video_button_48dp));
//            mMediaRecorder.stop();
            mMediaRecorder.release();
//            mCamera.lock();
            try {
                mCamera.reconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            startRecording = false;
        }
    }

    private String getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Camera App");
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
//        if (type == MEDIA_TYPE_IMAGE){
//            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
//                    "IMG_"+ timeStamp + ".jpg");
//        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
//        } else {
//            return null;
//        }

        return mediaStorageDir.getPath() + File.separator +
                "VID_"+ timeStamp + ".mp4";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.I(TAG,"onDestroy...");
        if(mMediaRecorder != null){
            mMediaRecorder.release();
        }
        if(mCamera != null){
            mCamera.release();
        }
    }


}
