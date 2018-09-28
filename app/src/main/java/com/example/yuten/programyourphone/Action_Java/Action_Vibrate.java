package com.example.yuten.programyourphone.Action_Java;
import android.content.Context;
import android.os.Vibrator;

/**
 * Created by Xt on 12/28/2017.
 */

public class Action_Vibrate {
    private Vibrator myVibrate;

    public Action_Vibrate(Context context){
        myVibrate = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void startVibrate(long ms){
        myVibrate.vibrate(ms);
    }

    public void stopVibrate (){
        myVibrate.cancel();
    }
}
