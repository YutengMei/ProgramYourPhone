package com.example.yuten.programyourphone;


import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by yuten on 1/4/2018.
 */

public class StateListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment(){
        return new StateListFragment();
    }
}
