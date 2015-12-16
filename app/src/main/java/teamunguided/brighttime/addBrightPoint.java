package teamunguided.brighttime;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

//import android.R;

public class addBrightPoint extends Activity{
    static final String SETTINGS_HOUR = "hour";
    static final String SETTINGS_MINUTES = "minute";

    private static final String alarmNames = "alrmnam";
    private static final String TAG = "addBrightPoint";
    private Set<String> pointNames = Collections.emptySet();
    private Set<String> _pointNames = Collections.emptySet();
    private Set<String> temp = Collections.emptySet();

    private AlarmManager alarmgr;
    private int randomID;
    private String stringID;

    private static Context mContext;

    //Seekbar objects
    final int intmax = 255;
    int brightnessToBeSet = 0;

    @Override
    protected void onStop() {
        Log.w(TAG, "App stopped");

        super.onStop();
    }

    //Putting bread in the toaster
    public static Context getContext() {
        return mContext;
    }

    //Toast text declaration
    CharSequence text = "Point added!";
    int duration = Toast.LENGTH_SHORT;

    //TODO FIX: Needs to be fixed, causes app to crash after onCreate finishes
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        //Loads the layout
        setContentView(R.layout.add_brighttime_point);

        //Loads the layout
        SeekBar brightnessSeeker = (SeekBar) findViewById(R.id.seekBrightness);

        //gets TimePicker then sets it to 12 hours view
        final TimePicker brightnessTime = (TimePicker) findViewById(R.id.timePickerBrightness);
        final Button confirmAdd = (Button) findViewById(R.id.confirmAdd);

        //temporary check
        brightnessSeeker.setMax(intmax);
        brightnessTime.setIs24HourView(false);

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
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //FUTURE: could get rid of currprogress and just use brightnessToBeSet in onProgress
                brightnessToBeSet = currprogress;
            }
        });

        confirmAdd.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //set up the service from the other file that we made.
//                int selectedTime = (brightnessTime.getCurrentHour() * 60
//                        + brightnessTime.getCurrentMinute()) *60;
                Calendar selectedTime = Calendar.getInstance();

                selectedTime.set(Calendar.MINUTE, brightnessTime.getCurrentMinute());
                selectedTime.set(Calendar.HOUR_OF_DAY, brightnessTime.getCurrentHour());
                selectedTime.set(Calendar.SECOND,0);
                //store info in preference
                //Need alarmNames, which can be stored as alarm id
                //Time they are set to
                //Brightness they are set to
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());
                pointNames = settings.getStringSet(alarmNames, temp);

                //generate a random id so as not to overwrite links
                String currID = null;
                Random rand = new Random();
                do {
                    randomID = rand.nextInt((999999 - 1) + 1) + 1;
                    stringID = Integer.toString(randomID);
                    if (pointNames.size() != 0) {
                        for (Iterator<String> e = pointNames.iterator(); e.hasNext(); ) {
                            currID = e.next();
                            if (currID.equals(stringID)) {
                                break;
                            }
                        }
                    }
                } while (stringID.equals(currID));

                //create a PendingIntent to perform service
                //TODO: setBrightnessTimer breaks app
                setBrightnessTimer(brightnessToBeSet, selectedTime, randomID);
                //storing all the things

                _pointNames = new HashSet<String>();
                for (String currWord: pointNames){
                    _pointNames.add(currWord);
                }
                _pointNames.add(stringID);

                //pointNames.add(stringID); //broken add
                SharedPreferences.Editor editStorage = settings.edit();
                editStorage.remove(alarmNames);
                editStorage.putStringSet(alarmNames, _pointNames);
                int hourSet = brightnessTime.getCurrentHour();
                int minuteSet = brightnessTime.getCurrentMinute();
                editStorage.putInt(stringID + SETTINGS_HOUR,hourSet);
                editStorage.putInt(stringID + SETTINGS_MINUTES, minuteSet);
//                if(hourSet == 0)
//                    editStorage.putString(stringID + SETTINGS_HOUR,"12:"+minuteSet+" am");
//                else if(hourSet < 12) {
//                    editStorage.putString(stringID + SETTINGS_HOUR,
//                            Integer.toString(hourSet) + ":" + minuteSet + " am");
//                }
//                else{
//                    editStorage.putString(stringID + SETTINGS_HOUR,
//                            Integer.toString((hourSet % 12)+1)+":"+minuteSet+" pm");
//                }
                editStorage.putInt(stringID, brightnessToBeSet);
                //FUTURE: check if .apply() is better than commit
                editStorage.commit();

                //Toast is ready to use!
                Toast toast = Toast.makeText(mContext, text, duration);
                toast.show();

                //TODO: check whether to use finish or the following
                Intent intent = new Intent(getApplicationContext(), BrightTime.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                //finish(); here?
            }
        });
        //or finish(); here?
    }

    @Override
    protected void onDestroy() {
        Log.w(TAG, "App destroyed");

        super.onDestroy();
    }

//    @Override
//    public void onResume(){
//        super.onResume();
//        //May need to move these things back to onCreate!!!
//
//        finish();
//    }


    public void setBrightnessTimer(int userinputBrightness, Calendar userinputTimeset, int alarmID){
        alarmgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent brightnessIntent = new Intent(addBrightPoint.this, BrightTimeService.class);
        String temp = Integer.toString(userinputBrightness);
        brightnessIntent.setData(Uri.parse(temp));
        PendingIntent setBrightness = PendingIntent.getService(addBrightPoint.this,alarmID,
                brightnessIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmgr.setRepeating(AlarmManager.RTC, userinputTimeset.getTimeInMillis(), AlarmManager.INTERVAL_DAY, setBrightness);
    }

    //move a final string to a mutable string from a Set<String>
//    public Set<String> stringSetCpy(Set<String> source) {
//        Set<String> _temp = Collections.emptySet();
//
//        if (source.size() != 0) {
//            for (Iterator<String> e = source.iterator(); e.hasNext(); ) {
//                String currID = e.next();
//                _temp.add(currID);
//            }
//        }
//        return _temp;
//    }


}
