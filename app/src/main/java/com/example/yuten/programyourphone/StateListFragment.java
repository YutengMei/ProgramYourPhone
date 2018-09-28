package com.example.yuten.programyourphone;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yuten.programyourphone.Action_Java.*;
import com.example.yuten.programyourphone.DataStructure_Java.*;

import java.util.ArrayList;
import java.util.List;



public class StateListFragment extends Fragment implements SensorEventListener {
    private RecyclerView RecyclerView;
    private ArrayList<ProgramState> mProgramStateList = new ArrayList<>();
    private StateAdapter mAdapter;
    private LinearLayout androidDropDownMenuIconItem,androidDropDownMenuIconItem1;
    private ImageButton ledOn,ledOff,vibrateOn,vibrateOff,ACTION1,ACTION2,ACTION3,ACTION4,ACTION5,ACTION6, ACTION7;
    private ImageButton timer,TRIGGER1,TRIGGER2,TRIGGER3,TRIGGER4;
    private ImageButton doge;
    private Button program;
    private HorizontalScrollView s1,s2;
    SwipeController swipeController = null;

    //Actions
    private Action_Flashlight myFlashlight;
    private Action_Vibrate myVibrate;
    private Action_Sound mySound;
    private Action_Image myImage;
    private Intent popUp;
    private View myView;
    //Triggers
    private SensorManager mySensorManager;
    private Sensor accelerometerSensor;
    private boolean isAccelerometerSensorPresent, isShakeReady = true;
    private long lastShake_ms;
    private float x,y,z,last_x,last_y,last_z, deltaX, deltaY, deltaZ, shakeThreshold = 5f;

    private boolean isMoveReady = true;
    private long lastMove_ms;
    private float moveThreshold = 1f;

    private Sensor distanceSensor;
    private boolean isDistanceSensorPresent, isDistanceReady = true;
    private long lastDistance_ms;

    private Sensor lightSensor;
    private boolean isLightSensorPresent;

    private Sensor gyroscopeSensor;
    private boolean isGyroscopeSensorPresent;
    //Threads
    private Code_Thread myThread;
    private boolean threadFlag = true;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        //Initialize actions for background thread
        myFlashlight = new Action_Flashlight();
        myVibrate = new Action_Vibrate(getActivity().getApplicationContext());
        mySound = new Action_Sound(getActivity().getApplicationContext());
        myView = getActivity().getWindow().getDecorView().getRootView();

        //Initialize triggers for background thread
        mySensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        if(mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null){
            accelerometerSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            isAccelerometerSensorPresent = true;
        }
        if(mySensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) != null){
            distanceSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            isDistanceSensorPresent = true;
        }
        if(mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null){
            lightSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            isLightSensorPresent = true;
        }
        if(mySensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null){
            gyroscopeSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            isGyroscopeSensorPresent = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.state_list_recycler, container, false);
        RecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        RecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        androidDropDownMenuIconItem = (LinearLayout) view.findViewById(R.id.horizontal_dropdown_icon_menu_items);
        androidDropDownMenuIconItem1 = (LinearLayout) view.findViewById(R.id.horizontal_dropdown_icon_menu_items1);
        s1 = (HorizontalScrollView) view.findViewById(R.id.scroll1);
        s2 = (HorizontalScrollView) view.findViewById(R.id.scroll2);
        program = (Button) view.findViewById(R.id.program);
        ledOn = (ImageButton) view.findViewById(R.id.imageLEDON);
        ledOff = (ImageButton) view.findViewById(R.id.imageLEDOFF);
        vibrateOn = (ImageButton) view.findViewById(R.id.imageVIRBATEON);
        vibrateOff = (ImageButton) view.findViewById(R.id.imageVIRBATEOFF);
        ACTION1 = (ImageButton) view.findViewById(R.id.imageSound);
        ACTION2 = (ImageButton) view.findViewById(R.id.imageLoop);
        ACTION3 = (ImageButton) view.findViewById(R.id.imageSoundOff);
        ACTION4 = (ImageButton) view.findViewById(R.id.imageImageSelect_1);
        ACTION5 = (ImageButton) view.findViewById(R.id.imageImageSelect_2);
        ACTION6 = (ImageButton) view.findViewById(R.id.imageImageSelect_3);
        ACTION7 = (ImageButton) view.findViewById(R.id.imageImageSelect_4);

        timer = (ImageButton) view.findViewById(R.id.imageTimer);
        TRIGGER1 = (ImageButton) view.findViewById(R.id.imageShake);
        TRIGGER2 = (ImageButton) view.findViewById(R.id.imageDistance);
        TRIGGER3 = (ImageButton) view.findViewById(R.id.imageMove);
        TRIGGER4 = (ImageButton) view.findViewById(R.id.imageTRIGGER4);

        ledOn.setOnLongClickListener(longClickListener);
        ledOff.setOnLongClickListener(longClickListener);
        vibrateOn.setOnLongClickListener(longClickListener);
        vibrateOff.setOnLongClickListener(longClickListener);
        ACTION1.setOnLongClickListener(longClickListener);
        ACTION2.setOnLongClickListener(longClickListener);
        ACTION3.setOnLongClickListener(longClickListener);
        ACTION4.setOnLongClickListener(longClickListener);
        ACTION5.setOnLongClickListener(longClickListener);
        ACTION6.setOnLongClickListener(longClickListener);
        ACTION7.setOnLongClickListener(longClickListener);


        timer.setOnLongClickListener(longClickListener);
        TRIGGER1.setOnLongClickListener(longClickListener);
        TRIGGER2.setOnLongClickListener(longClickListener);
        TRIGGER3.setOnLongClickListener(longClickListener);
        TRIGGER4.setOnLongClickListener(longClickListener);


        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                if (!myThread.runTheThread){
                    mAdapter.mStates.remove(position);
                    RecyclerView.findViewHolderForAdapterPosition(position).setIsRecyclable(false);
                    mAdapter.notifyItemRemoved(position);
                    mAdapter.notifyItemRangeChanged(position, mAdapter.getItemCount());
                    updateUI();
                }
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(RecyclerView);


        RecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });

        //Control Panel Button Click
        program.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (androidDropDownMenuIconItem.getVisibility() == View.VISIBLE && androidDropDownMenuIconItem1.getVisibility() == View.VISIBLE  ) {
                    androidDropDownMenuIconItem.setVisibility(View.INVISIBLE);
                    androidDropDownMenuIconItem.setClickable(false);
                    s1.setVisibility(View.GONE);
                    androidDropDownMenuIconItem1.setVisibility(View.INVISIBLE);
                    androidDropDownMenuIconItem1.setClickable(false);
                    s2.setVisibility(View.GONE);
                    //Action_Image.getInstance().finish();
                }
                else {
                    androidDropDownMenuIconItem.setVisibility(View.VISIBLE);
                    androidDropDownMenuIconItem.setClickable(true);
                    s1.setVisibility(View.VISIBLE);
                    androidDropDownMenuIconItem1.setVisibility(View.VISIBLE);
                    androidDropDownMenuIconItem1.setClickable(true);
                    s2.setVisibility(View.VISIBLE);

                    //popUp = new Intent (getActivity(), Action_Image.class);
                    //getActivity().startActivity(popUp);
                }


            }
        });
        updateUI();

        return view;

    }

    View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            ClipData data = ClipData.newPlainText("","");
            View.DragShadowBuilder myShadowBuilder = new View.DragShadowBuilder(v);
            v.startDrag(data,myShadowBuilder,v,0);
            return true;
        }
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.fragment_menu,menu);
        MenuItem menuItem = menu.findItem(R.id.add_state);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){
            case R.id.add_state:
                ProgramState programState = new ProgramState();
                mProgramStateList.add(programState);

                updateUI();
                break;
            case R.id.run:
                if (threadFlag && (mProgramStateList.size() != 0)) {
                    myThread = new Code_Thread(myFlashlight, myVibrate, mySound, mProgramStateList, myView, getContext(), getActivity());
                    myThread.start();
                    threadFlag = false;
                }
                break;
            case R.id.stop:
                if (!threadFlag) {
                    myThread.runTheThread = false;
                    threadFlag = true;
                }

            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    public void print(ArrayList<ProgramState> array){
        for (int i=0; i<array.size();i++){
            System.out.println(array.get(i)+"   ");
        }
    }

    private void updateUI(){
        if(mAdapter == null){
            mAdapter = new StateAdapter(mProgramStateList);

            RecyclerView.setAdapter(mAdapter);
        }
        else{
            mAdapter.notifyDataSetChanged();
        }
    }

    // implements View.OnClickListener
    private class StateHolder extends RecyclerView.ViewHolder {
        private LinearLayout actionTarget, triggerTarget,droppingArea;
        private TextView currentState;
        private ProgramState mState;

        public StateHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.state_view, parent, false));
            actionTarget = (LinearLayout) itemView.findViewById(R.id.action_target);
            triggerTarget = (LinearLayout) itemView.findViewById(R.id.trigger_target);
            droppingArea = (LinearLayout) itemView.findViewById(R.id.droppingArea);
            currentState = (TextView) itemView.findViewById(R.id.currentSate);
            droppingArea.setOnDragListener(actionDragListener);
        }

        public void bind(ProgramState state) {
           mState = state;
           currentState.setText("State #"+(mProgramStateList.indexOf(mState)+1));
        }

        View.OnDragListener actionDragListener = new View.OnDragListener(){
            @Override
            public boolean onDrag(View v, DragEvent event) {
                int dragEvent =  event.getAction();
                final View view = (View) event.getLocalState();

                switch(dragEvent){
                    case DragEvent.ACTION_DRAG_ENTERED:
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        break;
                    case DragEvent.ACTION_DROP:
                        printState(mProgramStateList);

                          if (view.getTag().toString().equals("action")){

                            if (view.getId() == R.id.imageLEDON){
                                Drawable temp = getActivity().getResources().getDrawable(R.drawable.flashon);
                                addImageButton(itemView,"action",R.id.action_target,temp);
                                mState.getAction().getmAction().add(Variables.Action_Flashlight_On);
                            }
                            else if(view.getId() == R.id.imageLEDOFF){
                                Drawable temp = getActivity().getResources().getDrawable(R.drawable.flashoff);
                                addImageButton(itemView,"action",R.id.action_target,temp);
                                mState.getAction().getmAction().add(Variables.Action_Flashlight_Off);
                            }
                            else if(view.getId() == R.id.imageVIRBATEON){
                                Drawable temp = getActivity().getResources().getDrawable(R.drawable.vibrateon);
                                addImageButton(itemView,"action",R.id.action_target,temp);
                                mState.getAction().getmAction().add(Variables.Action_Vibration_On);
                            }
                            else if(view.getId() == R.id.imageVIRBATEOFF){
                                Drawable temp = getActivity().getResources().getDrawable(R.drawable.vibrateoff);
                                addImageButton(itemView,"action",R.id.action_target,temp);
                                mState.getAction().getmAction().add(Variables.Action_Vibration_off);
                            }
                            //新加的2个actions
                            else if(view.getId() == R.id.imageSound){
                                Drawable temp = getActivity().getResources().getDrawable(R.drawable.music);
                                addImageButton(itemView,"action",R.id.action_target,temp);
                                mState.getAction().getmAction().add(Variables.Action_Sound_On);
                            }
                            else if(view.getId() == R.id.imageLoop){
                                Drawable temp = getActivity().getResources().getDrawable(R.drawable.loop);
                                addImageButton(itemView,"action",R.id.action_target,temp);
                                mState.getAction().getmAction().add(Variables.Action_Loop);
                            }
                            else if(view.getId() == R.id.imageSoundOff){
                                Drawable temp = getActivity().getResources().getDrawable(R.drawable.musicoff);
                                addImageButton(itemView,"action",R.id.action_target,temp);
                                mState.getAction().getmAction().add(Variables.Action_Sound_Off);
                            }
                            else if(view.getId() == R.id.imageImageSelect_1){
                                Drawable temp = getActivity().getResources().getDrawable(R.drawable.image1);
                                addImageButton(itemView,"action",R.id.action_target,temp);
                                mState.getAction().getmAction().add(Variables.Action_Image1);
                            }
                            else if(view.getId() == R.id.imageImageSelect_2){
                                Drawable temp = getActivity().getResources().getDrawable(R.drawable.image2);
                                addImageButton(itemView,"action",R.id.action_target,temp);
                                mState.getAction().getmAction().add(Variables.Action_Image2);
                            }
                            else if(view.getId() == R.id.imageImageSelect_3){
                                Drawable temp = getActivity().getResources().getDrawable(R.drawable.image3);
                                addImageButton(itemView,"action",R.id.action_target,temp);
                                mState.getAction().getmAction().add(Variables.Action_Image3);
                            }
                            else if(view.getId() == R.id.imageImageSelect_4){
                                Drawable temp = getActivity().getResources().getDrawable(R.drawable.thankyou);
                                addImageButton(itemView,"action",R.id.action_target,temp);
                                mState.getAction().getmAction().add(Variables.Action_Image4);
                            }
                          }

                          else{
                              if(view.getId() == R.id.imageTimer) {
                                  Drawable temp = getActivity().getResources().getDrawable(R.drawable.timer);
                                  addImageButton(itemView, "trigger", R.id.trigger_target, temp);
                                  mState.getTrigger().getmTrigger().add(Variables.Trigger_Timer);
                              }
                              //新加的4个triggers
                              else if (view.getId() == R.id.imageShake){
                                  Drawable temp = getActivity().getResources().getDrawable(R.drawable.shake);
                                  addImageButton(itemView, "trigger", R.id.trigger_target, temp);
                                  mState.getTrigger().getmTrigger().add(Variables.Trigger_Shake);
                              }
                              else if (view.getId() == R.id.imageDistance){
                                  Drawable temp = getActivity().getResources().getDrawable(R.drawable.distance);
                                  addImageButton(itemView, "trigger", R.id.trigger_target, temp);
                                  mState.getTrigger().getmTrigger().add(Variables.Trigger_Distance);
                              }
                              else if (view.getId() == R.id.imageMove){
                                  Drawable temp = getActivity().getResources().getDrawable(R.drawable.move);
                                  addImageButton(itemView, "trigger", R.id.trigger_target, temp);
                                  mState.getTrigger().getmTrigger().add(Variables.Trigger_Move);
                              }
                              else if (view.getId() == R.id.imageTRIGGER4){
                                  Drawable temp = getActivity().getResources().getDrawable(R.drawable.dog);
                                  addImageButton(itemView, "trigger", R.id.trigger_target, temp);
                                  mState.getTrigger().getmTrigger().add(Variables.Trigger_Timer);
                              }
                          }

                        break;
                }

                return true;
            }
        };
    }

    private class StateAdapter extends RecyclerView.Adapter<StateHolder> {
        private List<ProgramState> mStates;


        public StateAdapter(List<ProgramState> states) {
            mStates = states;
        }

        @Override
        public StateHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new StateHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(StateHolder holder, int position) {
            ProgramState state = mStates.get(position);
            holder.bind(state);
        }

        @Override
        public int getItemCount() {
            return mStates.size();
        }
    }


    public void  printState(List<ProgramState> input){
        for (int i = 0 ; i < input.size(); i++){
            System.out.println("State "+ i + " :");
            System.out.println("Action id:");
            for (int j = 0 ; j< input.get(i).getAction().getmAction().size();j++){
                System.out.print(input.get(i).getAction().getmAction().get(j) + " ");
            }
            System.out.println(" ");
            System.out.println("Trigger id:");
            for (int k = 0 ; k< input.get(i).getTrigger().getmTrigger().size();k++){
                System.out.print(input.get(i).getTrigger().getmTrigger().get(k) + " ");
            }
            System.out.println(" ");
            System.out.println("-----------------------");
        }
    }


    public void addImageButton(View v,String tag, int id, Drawable image){
        ImageButton myButton = new ImageButton(getActivity());
        myButton.setImageDrawable(image);
        myButton.setScaleType(ImageView.ScaleType.FIT_CENTER);

        LinearLayout ll;
        if (tag.equals("action")) {
            ll = (LinearLayout) v.findViewById(id);
            myButton.setBackgroundResource(R.drawable.action_single_background);
        }
        else{
            ll = (LinearLayout) v.findViewById(id);
            myButton.setBackgroundResource(R.drawable.trigger_single_background);
        }
        float width_height = getResources().getDimension(R.dimen.icon_size); //Convert dp to pixel
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int)width_height, (int)width_height);
        ll.addView(myButton,lp);
    }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
        //SENSOR_DELAY_FASTEST (0 microsecond delay)
        //SENSOR_DELAY_GAME (20,000 us delay)    or 20ms or 50Hz
        //SENSOR_DELAY_UI (60,000 us delay)      or 60ms or 17Hz
        //SENSOR_DELAY_NORMAL (200,000 us delay) or 200ms or 5Hz

        if (isAccelerometerSensorPresent)
            mySensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_UI);
        if (isDistanceSensorPresent)
            mySensorManager.registerListener(this, distanceSensor, SensorManager.SENSOR_DELAY_NORMAL);
        if (isLightSensorPresent)
            mySensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        if (isGyroscopeSensorPresent)
            mySensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mySensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (!threadFlag) {
            int sensorType = event.sensor.getType();
            //Accelerometer
            if (sensorType == Sensor.TYPE_ACCELEROMETER) {
                x = event.values[0];
                y = event.values[1];
                z = event.values[2];
                deltaX = Math.abs(last_x - x);
                deltaY = Math.abs(last_y - y);
                deltaZ = Math.abs(last_z - z);
                last_x = x;
                last_y = y;
                last_z = z;
                if (myThread.checkShake) {
                    if (isShakeReady) {
                        if ((deltaX > shakeThreshold && deltaY > shakeThreshold)
                                || (deltaX > shakeThreshold && deltaZ > shakeThreshold)
                                || (deltaY > shakeThreshold && deltaZ > shakeThreshold)) {
                            myThread.isShakeTriggered = true;
                            isShakeReady = false;
                            lastShake_ms = SystemClock.elapsedRealtime();
                        }

                    } else {
                        if ((SystemClock.elapsedRealtime() - lastShake_ms) > 1000)
                            isShakeReady = true;
                    }
                }
                if (myThread.checkMove){
                    if (isMoveReady) {
                        if ((deltaX >= moveThreshold) || (deltaY >= moveThreshold)
                                || (deltaZ >= moveThreshold)) {
                            myThread.isMoveTriggered = true;
                            isMoveReady = false;
                            lastMove_ms = SystemClock.elapsedRealtime();
                        }

                    } else {
                        if ((SystemClock.elapsedRealtime() - lastMove_ms) > 1000)
                            isMoveReady = true;
                    }
                }
            }
            else if (sensorType == Sensor.TYPE_PROXIMITY && myThread.checkDistance) {
                if (isDistanceReady) {
                    if (event.values[0] == 0) {
                        myThread.isDistanceTriggered = true;
                        isDistanceReady = false;
                        lastDistance_ms = SystemClock.elapsedRealtime();
                    }
                }
                else{
                    if ((SystemClock.elapsedRealtime()-lastDistance_ms) > 1000)
                        isDistanceReady = true;
                }
            }
            else if (sensorType == Sensor.TYPE_LIGHT) {

            }
            else if (sensorType == Sensor.TYPE_GYROSCOPE){

            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
