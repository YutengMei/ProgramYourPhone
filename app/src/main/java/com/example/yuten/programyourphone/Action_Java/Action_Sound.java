package com.example.yuten.programyourphone.Action_Java;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by Xt on 12/28/2017.
 */

public class Action_Sound {
    private MediaPlayer media;
    private Context mediaContext;

    public Action_Sound(Context context){
        stopSound();
        mediaContext = context;
    }

    public boolean isPlaying() {return  media.isPlaying();}

    public void startSound(int soundNum){
        if (media == null){
            media = MediaPlayer.create(mediaContext, soundNum);
        }
        media.start();
    }

    public void pauseSound(){
        media.pause();
    }

    public void stopSound(){
        if (media != null) {
            media.stop();
            media.release();
            media = null;
        }
    }
}
