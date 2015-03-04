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
    private List<String> pointTimes;

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

        //Gets stored information to load into ListView mPointList
        SharedPreferences settings = getPreferences(0);
        //Specifially getting time from storage
        pointNames = settings.getStringSet(alarmNames, null);
        for(Iterator<String> e = pointNames.iterator(); e.hasNext();){
            String temp = e.next();
            //possibly fix this one, use parse that is
            pointTimes.add(settings.getString(temp, "-1"));
        }

        //creates the adaptor...still not sure what it does
        //FUTURE: need to change adaptor to support clicking? that way they can edit points
        ListView mPointList = (ListView) findViewById(R.id.pointlist);
        final TimeAdapter adapter = new TimeAdapter(this,
                android.R.layout.simple_list_item_1, pointTimes);
        mPointList.setAdapter(adapter);
    }

    @Override
    public void onResume(){
        //adds button for adding more points
        Button addPoint = (Button) findViewById(R.id.addbrighttimepoint);
        //Listens for button to be clicked then moves to add point screen
        addPoint.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent addPointIntent = new Intent(BrightTime.this, addBrightPoint.class);
                startActivity(addPointIntent);
            }
        });
    }
}


    class TimeAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public TimeAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId,objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }
}
