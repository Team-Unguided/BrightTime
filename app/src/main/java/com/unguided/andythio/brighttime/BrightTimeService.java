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
    private Window window;

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
        //Get the current window attributes.
        WindowManager.LayoutParams params = window.getAttributes();
        //Set the brightness of this window.
        float newBrightness = (brightness / (float) 255);
        params.screenBrightness = newBrightness < 0.1 ? 0.1f : newBrightness;
        //Apply attribute changes to this window.
        window.setAttributes(params);

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