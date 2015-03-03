package com.unguided.andythio.brighttime;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 * Created by Andy Thio on 2/26/2015.
 */
public class BrightTime extends Activity {

    private static final String alarmNames = "alrmnam";

    private Set<String> pointNames;
    private List<int> pointTimes;

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
        setContentView(R.layout.bright_time);
        SharedPreferences settings = getPreferences(0);
        pointNames = settings.getStringSet(alarmNames, null);
        for(Object e : pointNames){
            pointTimes.add(settings.getInt(e,0));
        }
        ListView mPointList = (ListView) findViewById(R.id.pointlist);
        final TimeAdapter adapter = new TimeAdapter(this,
                android.R.layout.simple_list_item_1, pointTimes);
        mPointList.setAdapter(adapter);
    }

    @Override
    public void onResume(){
        //adds button for adding more points
        Button addPoint = (Button) findViewById(R.id.addbrighttimepoint);
        addPoint.setOnClickListener(new View.OnClickListener()){
            public void onClick(View v){
                Intent addPointIntent = new Intent(this, addBrightPoint.class);
                startActivity(addPointIntent);
            }
        }
    }
}


    private class TimeAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public TimeAdapter(Context context, int textViewResourceId,
                                  List<int> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }
}
