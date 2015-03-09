package com.unguided.andythio.brighttime;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.view.WindowManager;


/**
 * Created by Andy Thio on 3/9/2015.
 */
public class DummyBrightnessActivity extends Activity {

    private static final int DELAYED_MESSAGE = 1;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == DELAYED_MESSAGE) {
                    DummyBrightnessActivity.this.finish();
                }
                super.handleMessage(msg);
            }
        };
        Intent brightnessIntent = this.getIntent();
        float brightness = brightnessIntent.getFloatExtra("brightness value", 0);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = brightness;
        getWindow().setAttributes(lp);

        Message message = handler.obtainMessage(DELAYED_MESSAGE);
        //this next line is very important, you need to finish your activity with slight delay
        handler.sendMessageDelayed(message,500);
    }

}