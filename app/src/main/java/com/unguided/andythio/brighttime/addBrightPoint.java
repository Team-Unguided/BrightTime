package com.unguided.andythio.brighttime;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TimePicker;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * Created by Andy Thio on 3/2/2015.
 */
public class addBrightPoint extends Activity{

    private static final String alarmNames = "alrmnam";
    private Set<String> pointNames;

    private AlarmManager alarmgr;
    private int randomID;
    private String stringID;


    //seekbar max
    final int intmax = 255;
    int brightnessToBeSet = 0;


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        //Loads the layout
        setContentView(R.layout.add_brighttime_point);
    }

    public void onResume(){
        super.onResume();
        //May need to move these things back to onCreate!!!

        //gets the seekbar
        SeekBar brightnessSeeker = (SeekBar) findViewById(R.id.seekBrightness);
        brightnessSeeker.setMax(intmax);

        //gets TimePicker then sets it to 12 hours view
        final TimePicker brightnessTime = (TimePicker) findViewById(R.id.timePickerBrightness);
        brightnessTime.setIs24HourView(false);
        final Button confirmAdd = (Button) findViewById(R.id.confirmAdd);

        //FUTURE: change to current brightness
        brightnessSeeker.setProgress(0);
        brightnessSeeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int currprogress = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currprogress = progress;
                //FUTURE: make it so that as they change they will see the corresponding brightness
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //FUTURE: could get rid of currprogress and just use brightnessToBeSet in onProgress
                brightnessToBeSet = currprogress;
            }
        });

        //NOTE: might need to fix the second part. not sure what goes in there exactly
        confirmAdd.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //set up the service from the other file that we made.
                int selectedTime = (brightnessTime.getCurrentHour() * 60
                                        + brightnessTime.getCurrentMinute()) *60*1000;
                //store info in preference
                //Need alarmNames, which can be stored as alarm id
                //Time they are set to
                //Brightness they are set to
                SharedPreferences settings = getPreferences(0);
                pointNames = settings.getStringSet(alarmNames, null);

                //generate a random id so as not to overwrite links
                String currID = null;
                Random rand = new Random();
                do {
                    randomID = rand.nextInt((999999 - 1) + 1) + 1;
                    stringID = Integer.toString(randomID);
                    for(Iterator<String> e = pointNames.iterator(); e.hasNext();) {
                        currID = e.next();
                        if(currID.equals(stringID)){
                            break;
                        }
                    }
                } while (stringID.equals(currID));

                //create a PendingIntent to perform service
                setBrightnessTimer(brightnessToBeSet, selectedTime, randomID);
                //storing all the things
                pointNames.add(stringID);
                SharedPreferences.Editor editStorage = settings.edit();
                editStorage.remove(alarmNames);
                editStorage.putStringSet(alarmNames, pointNames);
                editStorage.putString(stringID, Integer.toString(selectedTime));
                editStorage.putInt(stringID, brightnessToBeSet);
                //FUTURE: check if .apply() is better than commit
                editStorage.commit();
            }
        });


        finish();
    }

    public void setBrightnessTimer(int userinputBrightness, int userinputTimeset, int alarmID){
        Intent brightnessIntent = new Intent(this, BrightTimeService.class);
        String temp = Integer.toString(userinputBrightness);
        brightnessIntent.setData(Uri.parse(temp));
        PendingIntent setBrightness = PendingIntent.getService(this,alarmID,
                brightnessIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmgr.setRepeating(AlarmManager.RTC, (userinputTimeset * 1000), AlarmManager.INTERVAL_DAY, setBrightness);
    }

}
