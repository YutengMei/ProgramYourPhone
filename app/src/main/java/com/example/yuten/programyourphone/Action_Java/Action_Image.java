package com.example.yuten.programyourphone.Action_Java;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import com.example.yuten.programyourphone.R;

/**
 * Created by Xt on 1/12/2018.
 */

public class Action_Image extends Activity {
    private ImageView myImage;
    public static Activity pop;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pop = this;
        setContentView(R.layout.action_image_layout);
        Intent myIntent = getIntent();

        myImage = (ImageView) findViewById(R.id.actionImageLayout);
        myImage.setImageResource(myIntent.getIntExtra("action_image_src", R.raw.lightspeed));

        DisplayMetrics DM = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(DM);

        int width = DM.widthPixels;
//        //int height = DM.heightPixels;
//        //getWindow().setLayout(width, (int) (height * 0.5));
        getWindow().setLayout(width, width);


        myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop = null;
                finish();
            }
        });
    }

    public static Activity getInstance(){
        return pop;
    }
}
