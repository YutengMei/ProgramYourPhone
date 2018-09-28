package com.example.yuten.programyourphone.Action_Java;

import android.hardware.Camera;
import android.hardware.Camera.Parameters;


/**
 * Created by Xt on 12/28/2017.
 */

public class Action_Flashlight {
    private Camera camera;
    private Parameters params;

    public Action_Flashlight(){
        if (camera == null) {
            camera = Camera.open();
            params = camera.getParameters();
        }
    }

    public void startFlashlight(){
        params = camera.getParameters();
        params.setFlashMode(Parameters.FLASH_MODE_TORCH);
        camera.setParameters(params);
        camera.startPreview();
    }

    public void stopFlashlight() {
        params = camera.getParameters();
        params.setFlashMode(Parameters.FLASH_MODE_OFF);
        camera.setParameters(params);
        camera.startPreview();
    }

    public void stopCamera(){
        if (camera != null) {
            camera.release();
            camera = null;}
    }
}
