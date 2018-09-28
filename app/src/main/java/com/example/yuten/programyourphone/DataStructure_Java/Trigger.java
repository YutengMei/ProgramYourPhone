package com.example.yuten.programyourphone.DataStructure_Java;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuten on 1/4/2018.
 */

public class Trigger {

    private List<Integer> mTrigger ;

    public Trigger (){
        mTrigger  = new ArrayList<>();
    }

    public void addAction(int t){
        mTrigger.add(t);
    }

    public List<Integer> getmTrigger(){
        return mTrigger ;
    }
}
