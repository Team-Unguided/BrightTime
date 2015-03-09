package com.unguided.andythio.brighttime;

import android.app.IntentService;
import android.content.Intent;
import android.content.ContentResolver;
import android.provider.Settings;
import android.provider.Settings.System;
import android.view.Window;
import android.view.WindowManager;

/**
 * Andy Thio & Shawn Lee. Last edit 03/05/15
 */

public class BrightTimeService extends IntentService
{

    //Content resolver used as a handle to the system's settings
    private ContentResolver cResolver;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public BrightTimeService() {
        super("BrightTimeService");
    }

    @Override
    protected void onHandleIntent(Intent workIntent){
        int brightness= Integer.parseInt(workIntent.getDataString());
        //Get the content resolver
        cResolver = getContentResolver();

        System.putInt(cResolver, System.SCREEN_BRIGHTNESS_MODE,
                System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        //Set the system brightness using the brightness variable value
        System.putInt(cResolver, System.SCREEN_BRIGHTNESS, brightness);
        //Set the brightness of this windows
        float newBrightness = (brightness / (float) 255);
        // Apply brightness by creating a dummy activity
        Intent intent = new Intent(getBaseContext(), DummyBrightnessActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("brightness value", newBrightness);
        getApplication().startActivity(intent);

//        try {
//            IHardwareService hardware = IHardwareService.Stub.asInterface(
//                    ServiceManager.getService("hardware"));
//            if (hardware != null) {
//                // set current brightness
//                hardware.setScreenBacklight(brightness);
//
//                // set system brightness
//                Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightness);
//            }
//        } catch (RemoteException doe) {
//            Log.d(TAG,">>>failed to call HardwareService: " + doe);
//            //TODO: add dialog box to ask user to send exception to developer.
//            //showDialog(DIALOG_CONTACT_DEV);
//        }
    }

}