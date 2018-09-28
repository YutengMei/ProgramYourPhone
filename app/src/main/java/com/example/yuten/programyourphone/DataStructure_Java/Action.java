package com.example.yuten.programyourphone.DataStructure_Java;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;


public class Action {

    private List<Integer> mAction;


    public Action(){
        mAction = new ArrayList<>();
    }

    public void addAction(int a){
        mAction.add(a);
    }

    public List<Integer> getmAction(){
        return mAction;
    }


}
