package com.unguided.andythio.brighttime;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 * Andy Thio & Shawn Lee. Last edit 03/05/15
 */

public class BrightTime extends Activity {

    private static final String alarmNames = "alrmnam";

    private Set<String> pointNames = Collections.emptySet();
    private List<String> pointTimes = Collections.emptyList();
    private Set<String> temp = Collections.emptySet();
    private static Context mContext;

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
        final ListView mPointList = (ListView) findViewById(R.id.pointlist);
        mContext = getApplicationContext();
        //Gets stored information to load into ListView mPointList
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());
        //Specifically getting time from storage
        //TODO: modify to array
        pointNames = settings.getStringSet(alarmNames, temp);
        String[] arrPointNames = pointNames.toArray(new String[pointNames.size()]);
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
            list.add(arrPointNames[i]);
        }
        // sets the adaptor to a modified array adaptor
        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        mPointList.setAdapter(adapter);
//        final TimeAdapter adapter = new TimeAdapter(this,
//                android.R.layout.simple_list_item_1, pointTimes);
//        mPointList.setAdapter(adapter);
        mPointList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //TODO:change the click action to bring you to an edit screen
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                view.animate().setDuration(2000).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                list.remove(item);
                                adapter.notifyDataSetChanged();
                                view.setAlpha(1);
                                //TODO: while it's delete to click, delete it in alarm manager
                            }
                        });
            }

        });
        Button addPoint = (Button) findViewById(R.id.addbrighttimepoint);
        //Listens for button to be clicked then moves to add point screen
        addPoint.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
//                Intent addPointIntent = new Intent(BrightTime.this, addBrightPoint.class);
//                startActivity(addPointIntent);
            Intent intent = new Intent(getApplicationContext(), addBrightPoint.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            }
        });
    }

//    @Override
//    public void onResume(){
//        //adds button for adding more points
//        super.onResume();
//
//    }
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
