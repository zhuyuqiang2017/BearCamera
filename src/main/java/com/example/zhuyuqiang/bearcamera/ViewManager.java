package com.example.zhuyuqiang.bearcamera;

/**
 * Created by zhuyuqiang on 2017/7/10.
 */

public class ViewManager {
    private ModePickerView mModePicker;
    public ViewManager(){

    }

    public void setModePickerView(ModePickerView modePicker){
        this.mModePicker = modePicker;
    }

    public void setCurrentMode(boolean b) {
        this.mModePicker.setCurrentMode(b);
    }

    public int getCurrentMode(){
        return this.mModePicker.getCurrentMode();
    }

    public void setCameraModeChangeListener(ModePickerView.CameraModeChangeListener listener){
        this.mModePicker.setCameraModeChangerListener(listener);
    }
}
