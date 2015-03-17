package com.unguided.andythio.brighttime;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andy Thio on 3/13/2015.
 */
public class editPoint extends Activity {
    static final String SETTINGS_HOUR = "hour";
    static final String SETTINGS_MINUTES = "minute";
    private static final String alarmNames = "alrmnam";
    private Set<String> temp = Collections.emptySet();

    private AlarmManager alarmgr;
    private static Context mContext;

    private int brightnessToBeSet;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_point);
        final String alarmID = getIntent().getStringExtra("stringID");
        mContext = getApplicationContext();

        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int currBrightness = settings.getInt(alarmID,-1);

        final TimePicker brightnessTime = (TimePicker) findViewById(R.id.timePickerEdit);
        brightnessTime.setIs24HourView(false);

        brightnessTime.setCurrentHour(settings.getInt(alarmID + SETTINGS_HOUR, -1));
        brightnessTime.setCurrentMinute(settings.getInt(alarmID + SETTINGS_MINUTES, -1));

        Button confirmEdit = (Button) findViewById(R.id.comfirmEdit);
        Button removeEdit = (Button) findViewById(R.id.rmPoint);

        SeekBar brightnessSeeker = (SeekBar) findViewById(R.id.brightnessEdit);
        brightnessSeeker.setMax(255);
        brightnessSeeker.setProgress(currBrightness);


        brightnessSeeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int currprogress = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currprogress = progress;
                //FUTURE: make it so that as they change they will see the corresponding brightness
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //FUTURE: could get rid of currprogress and just use brightnessToBeSet in onProgress
                brightnessToBeSet = currprogress;
            }
        });

        confirmEdit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Calendar selectedTime = Calendar.getInstance();
                selectedTime.set(Calendar.MINUTE, brightnessTime.getCurrentMinute());
                selectedTime.set(Calendar.HOUR_OF_DAY, brightnessTime.getCurrentHour());
                selectedTime.set(Calendar.SECOND,0);

                removeBrightnessTimer(brightnessToBeSet, Integer.parseInt(alarmID));
                setBrightnessTimer(brightnessToBeSet, selectedTime, Integer.parseInt(alarmID));

                SharedPreferences.Editor editStorage = settings.edit();
                editStorage.putInt(alarmID, brightnessToBeSet);
                editStorage.putInt(alarmID + SETTINGS_HOUR, brightnessTime.getCurrentHour());
                editStorage.putInt(alarmID + SETTINGS_MINUTES, brightnessTime.getCurrentMinute());
                editStorage.commit();

                Intent intent = new Intent(getApplicationContext(), BrightTime.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        removeEdit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Calendar selectedTime = Calendar.getInstance();
                selectedTime.set(Calendar.MINUTE, brightnessTime.getCurrentMinute());
                selectedTime.set(Calendar.HOUR_OF_DAY, brightnessTime.getCurrentHour());
                selectedTime.set(Calendar.SECOND,0);

                removeBrightnessTimer(brightnessToBeSet, Integer.parseInt(alarmID));

                Set<String> pointNames = settings.getStringSet(alarmNames,temp );


                Set<String> _pointNames = pointNames;
                _pointNames.remove(alarmID);
//                for (String currWord: pointNames){
//                    if(!currWord.equals(alarmID))
//                        _pointNames.add(currWord);
//                }//


                SharedPreferences.Editor editStorage = settings.edit();
                editStorage.remove(alarmNames);
                editStorage.putStringSet(alarmNames, _pointNames);
                editStorage.remove(alarmID);
                editStorage.remove(alarmID + SETTINGS_HOUR);
                editStorage.remove(alarmID + SETTINGS_MINUTES);
                editStorage.commit();

                Intent intent = new Intent(getApplicationContext(), BrightTime.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }

    public void removeBrightnessTimer(int userinputBrightness, int alarmID){
        alarmgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent brightnessIntent = new Intent(editPoint.this, BrightTimeService.class);
        String temp = Integer.toString(userinputBrightness);
        brightnessIntent.setData(Uri.parse(temp));
        PendingIntent setBrightness = PendingIntent.getService(editPoint.this,alarmID,
                brightnessIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmgr.cancel(setBrightness);
    }

    public void setBrightnessTimer(int userinputBrightness, Calendar userinputTimeset, int alarmID){
        alarmgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent brightnessIntent = new Intent(editPoint.this, BrightTimeService.class);
        String temp = Integer.toString(userinputBrightness);
        brightnessIntent.setData(Uri.parse(temp));
        PendingIntent setBrightness = PendingIntent.getService(editPoint.this,alarmID,
                brightnessIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmgr.setRepeating(AlarmManager.RTC, userinputTimeset.getTimeInMillis(), AlarmManager.INTERVAL_DAY, setBrightness);
    }
}
