package teamunguided.brighttime;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BrightTime extends Activity {
    static final String SETTINGS_HOUR = "hour";
    static final String SETTINGS_MINUTES = "minute";

    private static final String alarmNames = "alrmnam";

    private Set<String> pointNames = Collections.emptySet();
    private Set<String> temp = Collections.emptySet();
    private static Context mContext;

    private AlarmManager alarmgr;

    private ImageButton addPoint;
    private ImageButton mLeftButton;
    private ImageButton mRightButton;

    private static final float FAB_DEPTH = 20f;
    private static final int UNKNOWN_COLOR_ID = 0;

    private boolean doubleBackToExitPressedOnce = false;

    StableArrayAdapter adapter;

    public static Context getContext() {
        return mContext;
    }

    /* TODO:
        * Listen for addbrightimepoint button
        * create a page where bright time points can be created
        * call the pending request function
        * figure out what needs to be in onResume() and not in onCreate()
        * Clean up and
     */

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //loads Overlay
        setContentView(R.layout.bright_time);
        //SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if(settings.getBoolean("isFirstRun",true)){
            setDefaultPoints();
            SharedPreferences.Editor editInitial = settings.edit();
            editInitial.putBoolean("isFirstRun", false);
            editInitial.commit();
        }

        addPoint = (ImageButton) findViewById(R.id.addbrighttimepoint);
        addPoint.setTranslationZ(FAB_DEPTH);
        mLeftButton = (ImageButton) findViewById(R.id.left_button);
        mRightButton = (ImageButton) findViewById(R.id.right_button);

        final ListView mPointList = (ListView) findViewById(R.id.pointlist);
        mContext = getApplicationContext();
        //Gets stored information to load into ListView mPointList

        //Specifically getting time from storage
        //TODO: modify to array
        pointNames = settings.getStringSet(alarmNames, temp);
        final String[] arrPointNames = pointNames.toArray(new String[pointNames.size()]);
//        if(pointNames.size() != 0) {
//            for (Iterator<String> e = pointNames.iterator(); e.hasNext(); ) {
//                String temp = e.next();
//                //possibly fix this one, use parse that is
//                pointTimes.add(settings.getString(temp, "-1"));
//            }
//        }

        //creates the adaptor...still not sure what it does
        //FUTURE: need to change adaptor to support clicking? that way they can edit points

        final ArrayList<String> list = new ArrayList<String>();
        for(int i = 0; i < arrPointNames.length; ++i){
            String displayTime;
            boolean isPM = false;
            int displayHour = settings.getInt((arrPointNames[i] + SETTINGS_HOUR), -1);
            int displayMin = settings.getInt((arrPointNames[i] + SETTINGS_MINUTES), -1);
            if(displayHour == -1 || displayMin == -1)
                list.add("Error: Unable to Retrieve Point");
            else{
                if(displayHour % 12 == 0){
                    if(displayHour != 0)
                        isPM = true;
                    displayTime = "12:";
                }
                else if(displayHour < 12)
                    displayTime = displayHour + ":";
                else{
                    displayTime = ((displayHour % 12))+":";
                    isPM = true;
                }
                if(isPM){
                    if(displayMin < 10)
                        list.add(displayTime + "0" + displayMin + " pm");
                    else
                        list.add(displayTime + displayMin + " pm");
                }
                else{
                    if(displayMin < 10)
                        list.add(displayTime + "0" + displayMin + " am");
                    else
                        list.add(displayTime + displayMin + " am");
                }
            }

            //list.add(settings.getString(arrPointNames[i] + SETTINGS_HOUR,
            //"Error: Unable to Retrieve Point"));
        }
        // sets the adaptor to a modified array adaptor
        adapter = new StableArrayAdapter(this,
                R.layout.settinglist, list);
        mPointList.setAdapter(adapter);

        //final TimeAdapter adapter = new TimeAdapter(this,
        //android.R.layout.simple_list_item_1, pointTimes);
        //mPointList.setAdapter(adapter);

        mPointList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //TODO:change the click action to bring you to an edit screen
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final int tisPosition = position;
                final String item = (String) parent.getItemAtPosition(position);
                view.animate().setDuration(500).alpha(1)
                        .withEndAction(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent editIntent = new Intent(getApplicationContext(), editPoint.class);
                                        editIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        editIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                        editIntent.putExtra("stringID", arrPointNames[tisPosition]);
                                        startActivity(editIntent);
                                        adapter.notifyDataSetChanged();
                                        view.setAlpha(1);
                                        //TODO: while it's delete to click, delete it in alarm manager
                                    }
                                });
            }
        });

        //ImageButton addPoint = (ImageButton) findViewById(R.id.addbrighttimepoint);
        //Listens for button to be clicked then moves to add point screen
        addPoint.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
//                Intent addPointIntent = new Intent(BrightTime.this, addBrightPoint.class);
//                startActivity(addPointIntent);
                Intent intent = new Intent(getApplicationContext(), addBrightPoint.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume(){
        //adds button for adding more points
        super.onResume();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());
        pointNames = settings.getStringSet(alarmNames, temp);


//        String[] arrPointNames = pointNames.toArray(new String[pointNames.size()]);
//        adapter.clear();
//        adapter.addAll(arrPointNames);
//        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            this.finish();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

    //Creates default points for when app first is installed
    private void setDefaultPoints(){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Set<String> initialPoints = new HashSet<String>(Arrays.asList( "0", "1", "2" ,"3" , "4"));
        String[] initPoints = {"0","1","2","3","4"};
        int[] initialTimeHOUR = {6,8,12,14,19};
        int[] initialsetBrightness = {64,153,255,128,51};

        SharedPreferences.Editor editInitial = settings.edit();

        for(int i = 0; i < 5 ; ++i){
            Calendar setTime = Calendar.getInstance();
            setTime.set(Calendar.HOUR_OF_DAY, initialTimeHOUR[i]);
            setTime.set(Calendar.MINUTE, 0);
            setTime.set(Calendar.SECOND, 0);

            setBrightnessTimer(initialsetBrightness[i], setTime, Integer.parseInt(initPoints[i]));

            editInitial.putInt(initPoints[i], initialsetBrightness[i]);
            editInitial.putInt(initPoints[i] + SETTINGS_HOUR, initialTimeHOUR[i]);
            editInitial.putInt(initPoints[i] + SETTINGS_MINUTES, 0);
        }
        editInitial.putStringSet(alarmNames, initialPoints);
        editInitial.commit();
    }

    public void setBrightnessTimer(int userinputBrightness, Calendar userinputTimeset, int alarmID){
        alarmgr = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent brightnessIntent = new Intent(BrightTime.this, BrightTimeService.class);
        String temp = Integer.toString(userinputBrightness);
        brightnessIntent.setData(Uri.parse(temp));
        PendingIntent setBrightness = PendingIntent.getService(BrightTime.this,alarmID,
                brightnessIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmgr.setRepeating(AlarmManager.RTC, userinputTimeset.getTimeInMillis(), AlarmManager.INTERVAL_DAY, setBrightness);
    }
}

class TimeAdapter extends ArrayAdapter<String> {

    HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

    public TimeAdapter(Context context, int textViewResourceId,
                       List<String> objects) {
        super(context, textViewResourceId,objects);
        //for (int i = 0; i < objects.size(); ++i) {
        mIdMap.put("testing!!", 1);
        //}
    }

}
