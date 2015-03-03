package com.unguided.andythio.brighttime;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TimePicker;

/**
 * Created by Andy Thio on 3/2/2015.
 */
public class addBrightPoint extends Activity{
    //seekbar max
    final int intmax = 255;
    int brightnessToBeSet = 0;


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.add_brighttime_point);
        SeekBar brightnessSeeker = (SeekBar) findViewById(R.id.seekBrightness);
        brightnessSeeker.setMax(intmax);

        final TimePicker brightnessTime = (TimePicker) findViewById(R.id.timePickerBrightness);
        brightnessTime.setIs24HourView(false);
        final Button confirmAdd = (Button) findViewById(R.id.confirmAdd);

        //change to current brightness
        brightnessSeeker.setProgress(0);
        brightnessSeeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int currprogress = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currprogress = progress;
                //make it so that as they change they will see the corresponding brightness
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //could get rid of currprogress and just use brightnessToBeSet in onProgress
                brightnessToBeSet = currprogress;
            }
        });

        //might need to fix the second part. not sure what goes in there exactly
        confirmAdd.setOnClickListener(new View.OnClickListener()){
            public void onClick(View v){
                //set up the service from the other file that we made.

            }
        }



    }
}
