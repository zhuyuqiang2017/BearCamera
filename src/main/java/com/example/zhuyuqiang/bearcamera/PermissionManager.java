package com.example.zhuyuqiang.bearcamera;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuyuqiang on 2017/7/6.
 */

public class PermissionManager {
    private final String TAG = "PermissionManager";
    private final int PERMISSION_CAMERA_LAUNCH_CODE = 99;
    private final String PERMISSION_CAMERA = "android.permission.CAMERA";
    private final String PERMISSION_WRITE_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";
    private final String PERMISSION_READ_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
    private String[] mPermissions;
    private Activity mContext;

    public PermissionManager(Activity activity){
        mContext = activity;
        mPermissions = initDefaultPermission();
    }

    private String[] initDefaultPermission(){
        final String METHOD_TAG = "initDefaultPermission";
        LogUtil.I(TAG,METHOD_TAG);
        List<String> mPermissionList = new ArrayList<String>();
        if(mContext.checkSelfPermission(PERMISSION_CAMERA)!= PackageManager.PERMISSION_GRANTED){
            mPermissionList.add(PERMISSION_CAMERA);
        }
        if(mContext.checkSelfPermission(PERMISSION_WRITE_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            mPermissionList.add(PERMISSION_WRITE_STORAGE);
        }
        if(mContext.checkSelfPermission(PERMISSION_READ_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            mPermissionList.add(PERMISSION_READ_STORAGE);
        }
        String[] ss = new String[mPermissionList.size()];
        for (int i = 0 ; i<mPermissionList.size() ; i++){
            ss[i] = mPermissionList.get(i);
        }
        return ss;
    }

    public void checkLaunchCameraPermission(){
        final String METHOD_TAG = "checkLaunchCameraPermission";
        LogUtil.I(TAG,METHOD_TAG);
        if(mPermissions != null && mPermissions.length>0){
            mContext.requestPermissions(mPermissions,PERMISSION_CAMERA_LAUNCH_CODE);
        }
    }

    public void checkOtherPermission(String permission,final int permissionCode){
        if(mContext.checkSelfPermission(permission)!= PackageManager.PERMISSION_GRANTED){
            mContext.requestPermissions(new String[]{permission},permissionCode);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        boolean permissionFlag = true;
        if((requestCode==PERMISSION_CAMERA_LAUNCH_CODE)){
            for(int i : grantResults){
                if (i != PackageManager.PERMISSION_GRANTED){
                    permissionFlag = false;
                }
            }
        }
        if(!permissionFlag){
            Toast.makeText(mContext,"Permission Denied",Toast.LENGTH_LONG).show();
            mContext.finish();
        }
    }
}
