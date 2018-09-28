package com.example.yuten.programyourphone.DataStructure_Java;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class StateLab {
    private static StateLab sStateLab;
    private List<ProgramState> mProgramState;

    public static StateLab get(Context context){
        if(sStateLab == null){
            sStateLab = new StateLab(context);
        }
        return sStateLab;
    }

    private StateLab(Context context){
        mProgramState = new ArrayList<>();
    }

    public void addState(ProgramState programState){
        mProgramState.add(programState);
    }

    public List<ProgramState> getmTask() {
        return mProgramState;
    }

    public ProgramState getTask(UUID id){
        for(ProgramState programState : mProgramState){
            if (programState.getStateId().equals(id)){
                return programState;
            }
        }
        return null;
    }

}
