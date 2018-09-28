package com.example.yuten.programyourphone.DataStructure_Java;

import com.example.yuten.programyourphone.DataStructure_Java.Action;
import com.example.yuten.programyourphone.DataStructure_Java.Trigger;

import java.util.UUID;


public class ProgramState {
    private UUID stateId;
    private Action action;
    private Trigger trigger;


    public ProgramState(){
        stateId = UUID.randomUUID();
        action = new Action();
        trigger = new Trigger();
    }

    public UUID getStateId(){
        return stateId;
    }

    public Action getAction() {
        return action;
    }

    public Trigger getTrigger() {
        return trigger;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public void setTrigger(Trigger trigger) {
        this.trigger = trigger;
    }
}
