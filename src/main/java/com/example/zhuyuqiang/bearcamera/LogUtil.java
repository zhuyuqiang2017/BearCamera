package com.example.zhuyuqiang.bearcamera;

/**
 * Created by zhuyuqiang on 2017/7/6.
 */

public class LogUtil {
    private static final boolean INFO = true;
    private static final boolean DEBUG = false;
    public static void I(String TAG,String message){
        android.util.Log.i(TAG,message);
    }
}
