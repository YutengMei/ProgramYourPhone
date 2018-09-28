package com.example.yuten.programyourphone;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.yuten.programyourphone.Action_Java.*;
import com.example.yuten.programyourphone.DataStructure_Java.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xt on 1/5/2018.
 */

public class Code_Thread extends Thread {
    //Part 1. Data Structure
    private ArrayList<ProgramState> stateList;
    private ProgramState currentState;
    private int currentStateIdx = 0;
    private Action actionList;
    private Trigger triggerList;
    public boolean runTheThread = true;
    private View myView;
    private Context myContext;
    private Activity myActivity;
    //Part 2. Actions
    private Action_Flashlight myFlashlight;
    private Action_Vibrate myVibrate;
    private Action_Sound mySound;
    private int loopIdx;
    private boolean isActionLoop;
    //Part 3. Triggers
    private long startTime, targetEndTime;  //Trigger_Timer
    public boolean isShakeTriggered, isMoveTriggered;
    public boolean isTimerTriggered, isDistanceTriggered;
    public boolean checkShake = false, checkDistance = false, checkMove = false;


    Code_Thread(Action_Flashlight Flashlight, Action_Vibrate Vibrate, Action_Sound Sound, ArrayList<ProgramState> list, View V, Context C, Activity A) {
        myFlashlight = Flashlight;
        myVibrate = Vibrate;
        mySound = Sound;
        stateList = list;
        myView = V;
        myContext = C;
        myActivity = A;
    }

    @Override
    public void run() {
        currentState = stateList.get(currentStateIdx);
        actionList = currentState.getAction();
        triggerList = currentState.getTrigger();
        updateStateBackground(currentStateIdx,true);

        while (runTheThread) {
            extractActionFromState();   //1. Extract Actions from current state and Execute
            resetTrigger();             //2. Reset Triggers
            extractTriggerFromState();  //3. Extract Triggers from current state
            checkTriggerInLoop();       //4. Wait for Triggers to occur in a loop
            moveToNextState();          //5. Move to next state
            yield();

//            if (Thread.interrupted()) {
//                return;
//            }
            //use wait and notify
        }
    }


    //Step 1:
    private void extractActionFromState(){
        int numberOfActions = actionList.getmAction().size();
        if (numberOfActions == 0){
            //If no action, do nothing.
        }
        else {
            for (int i = 0; i < numberOfActions; i++) {
                int actionNum = actionList.getmAction().get(i);
                if (actionNum == Variables.Action_Loop){
                    loopIdx = 0;
                    isActionLoop = true;
                    break;
                }
                performAction(actionNum);
                //performUIAction();
            }
        }
    }

    private void performAction(int actionNumber) {
        //System.out.println(actionNumber + " ");
        switch (actionNumber) {
            case Variables.Action_Flashlight_On:
                myFlashlight.startFlashlight();
                break;
            case Variables.Action_Flashlight_Off:
                myFlashlight.stopFlashlight();
                break;
            case Variables.Action_Vibration_On:
                myVibrate.startVibrate(120000);
                break;
            case Variables.Action_Vibration_off:
                myVibrate.stopVibrate();
                break;
            case Variables.Action_Sound_On:
                mySound.startSound(R.raw.alarm);
                break;
            case Variables.Action_Sound_Off:
                 mySound.stopSound();
                 break;
            case Variables.Action_Image1:
                startImage(R.raw.image1);
                break;
            case Variables.Action_Image2:
                //startImage(R.raw.lightspeed);
                startImage(R.raw.image2);
                break;
            case Variables.Action_Image3:
                //startImage(R.raw.lightspeed_edit);
                startImage(R.raw.image3);
                break;
            case Variables.Action_Image4:
                startImage(R.raw.thankyou);
                break;
        }
    }

    //Step 2:
    private void resetTrigger() {
        isTimerTriggered = false;
        isShakeTriggered = false; checkShake = false;
        isMoveTriggered = false; checkMove = false;
        isDistanceTriggered = false; checkDistance = false;
        startTime = SystemClock.elapsedRealtime();
        targetEndTime = Long.MAX_VALUE;
    }

    //Step 3:
    private void extractTriggerFromState() {
        if (!isActionLoop) { //Skip Step 3, if going in loop
            int triggerNum = triggerList.getmTrigger().size();
            //If no trigger, stuck in an infinite loop
            if (triggerNum == 0) {
                while (runTheThread) yield();
            }
            //Otherwise, extract triggers
            else {
                for (int i = 0; i < triggerNum; i++) {
                    performTrigger(triggerList.getmTrigger().get(i));
                }
            }
        }
    }

    //Step 4:
    private void checkTriggerInLoop() {
        while (true) {
            yield();
            //Timer Trigger
            if (SystemClock.elapsedRealtime() >= targetEndTime) break;
            //shake Trigger
            if (isShakeTriggered) break;
            //move Trigger
            if (isMoveTriggered) break;
            //distance Trigger
            if (isDistanceTriggered) break;
            //Thread Control Trigger
            if (!runTheThread) break;
            //Is Loop Trigger
            if (isActionLoop) break;
        }
    }

    private void performTrigger(int triggerNumber) {
        switch (triggerNumber) {
            case Variables.Trigger_Timer:
                targetEndTime = startTime + 3000;
                break;
            case Variables.Trigger_Distance:
                checkDistance = true;
                break;
            case Variables.Trigger_Shake:
                checkShake = true;
                break;
            case Variables.Trigger_Move:
                checkMove = true;
        }
    }

    private void performUIAction() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                //textview_debug.setText(""+ SystemClock.elapsedRealtime());
            }
        });
    }

    //Step 5
    private void moveToNextState(){
        updateStateBackground(currentStateIdx, false);
        if (runTheThread){
            //Get new currentStateIdx
            if (isActionLoop) {
                currentStateIdx = loopIdx;
                isActionLoop = false;
            }
            else {
                currentStateIdx++;
            }

            //If illegal state idx
            if (currentStateIdx >= stateList.size()){
                while (runTheThread) {yield();}
                stopAllActions();
            }
            else{
                updateStateBackground(currentStateIdx, true);
                currentState = stateList.get(currentStateIdx);
                actionList = currentState.getAction();
                triggerList = currentState.getTrigger();
            }

            //Check if state is empty
            if (triggerList.getmTrigger().size() == 0 && actionList.getmAction().size() == 0){
                while (runTheThread){yield();};
                stopAllActions();
                updateStateBackground(currentStateIdx, false);
            }
        }
        //If thread no long runs, stop all actions
        else{
            stopAllActions();
        }
    }

    public void stopAllActions(){
        myVibrate.stopVibrate();
        myFlashlight.stopFlashlight();
        mySound.stopSound();
        stopImage();
    }

    private void delay(long ms) {
        long end = ms + SystemClock.elapsedRealtime();
        while (SystemClock.elapsedRealtime() < end) ;
    }

    public void printState(List<ProgramState> input) {
        for (int i = 0; i < input.size(); i++) {
            System.out.println("ProgramState " + i + " :");
            System.out.println("Action id:");
            for (int j = 0; j < input.get(i).getAction().getmAction().size(); j++) {
                System.out.print(input.get(i).getAction().getmAction().get(j) + " ");
            }
            System.out.println(" ");
            System.out.println("Trigger id:");
            for (int k = 0; k < input.get(i).getTrigger().getmTrigger().size(); k++) {
                System.out.print(input.get(i).getTrigger().getmTrigger().get(k) + " ");
            }
            System.out.println(" ");
            System.out.println("-----------------------");
        }
    }

    private void updateStateBackground(final int Idx, final boolean On){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                RecyclerView RecyclerView = (RecyclerView) myView.findViewById(R.id.recycler_view);
                TextView text = (TextView) RecyclerView.findViewHolderForAdapterPosition(Idx).itemView.findViewById(R.id.currentSate);
                if (On){
                    //RecyclerView.findViewHolderForAdapterPosition(Idx).itemView.setBackground(myContext.getResources().getDrawable(R.drawable.button_background));
                    text.setTextColor(Color.parseColor("#57F2CC"));
                }
                else{
                    //RecyclerView.findViewHolderForAdapterPosition(Idx).itemView.setBackgroundColor(0xFFFFFF);
                    text.setTextColor(Color.parseColor("#808080"));
                }
            }
        });
    }

    private void startImage(int image_src){
        stopImage();
        Intent popUp = new Intent (myActivity, Action_Image.class);
        popUp.putExtra("action_image_src",image_src);
        myActivity.startActivity(popUp);
    }
    private void stopImage(){
        if (Action_Image.getInstance()!= null) {
            Action_Image.getInstance().finish();
        }
    }
}
