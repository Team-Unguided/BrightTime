/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.unguided.andythio.brighttime;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextClock;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;


public class Utils {
    private final static String PARAM_LANGUAGE_CODE = "hl";

    /**
     * Help URL query parameter key for the app version.
     */
    private final static String PARAM_VERSION = "version";

    /**
     * Cached version code to prevent repeated calls to the package manager.
     */
    private static String sCachedVersionCode = null;

    /**
     * Array of single-character day of week symbols {'S', 'M', 'T', 'W', 'T', 'F', 'S'}
     */
    private static String[] sShortWeekdays = null;

    /** Types that may be used for clock displays. **/
    public static final String CLOCK_TYPE_DIGITAL = "digital";
    public static final String CLOCK_TYPE_ANALOG = "analog";

    /** The background colors of the app, it changes thru out the day to mimic the sky. **/
    public static final String[] BACKGROUND_SPECTRUM = { "#212121", "#27232e", "#2d253a",
            "#332847", "#382a53", "#3e2c5f", "#442e6c", "#393a7a", "#2e4687", "#235395", "#185fa2",
            "#0d6baf", "#0277bd", "#0d6cb1", "#1861a6", "#23569b", "#2d4a8f", "#383f84", "#433478",
            "#3d3169", "#382e5b", "#322b4d", "#2c273e", "#272430" };

    /**
     * Returns whether the SDK is KitKat or later
     */
    public static boolean isKitKatOrLater() {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2;
    }


    public static void prepareHelpMenuItem(Context context, MenuItem helpMenuItem) {
        String helpUrlString = context.getResources().getString(R.string.desk_clock_help_url);
        if (TextUtils.isEmpty(helpUrlString)) {
            // The help url string is empty or null, so set the help menu item to be invisible.
            helpMenuItem.setVisible(false);
            return;
        }
        // The help url string exists, so first add in some extra query parameters.  87
        final Uri fullUri = uriWithAddedParameters(context, Uri.parse(helpUrlString));

        // Then, create an intent that will be fired when the user
        // selects this help menu item.
        Intent intent = new Intent(Intent.ACTION_VIEW, fullUri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

        // Set the intent to the help menu item, show the help menu item in the overflow
        // menu, and make it visible.
        helpMenuItem.setIntent(intent);
        helpMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        helpMenuItem.setVisible(true);
    }

    /**
     * Adds two query parameters into the Uri, namely the language code and the version code
     * of the application's package as gotten via the context.
     * @return the uri with added query parameters
     */
    private static Uri uriWithAddedParameters(Context context, Uri baseUri) {
        Uri.Builder builder = baseUri.buildUpon();

        // Add in the preferred language
        builder.appendQueryParameter(PARAM_LANGUAGE_CODE, Locale.getDefault().toString());

        // Add in the package version code
        if (sCachedVersionCode == null) {
            // There is no cached version code, so try to get it from the package manager.
            try {
                // cache the version code
                PackageInfo info = context.getPackageManager().getPackageInfo(
                        context.getPackageName(), 0);
                sCachedVersionCode = Integer.toString(info.versionCode);

                // append the version code to the uri
                builder.appendQueryParameter(PARAM_VERSION, sCachedVersionCode);
            } catch (NameNotFoundException e) {
                // Cannot find the package name, so don't add in the version parameter
                // This shouldn't happen.
                LogUtils.wtf("Invalid package name for context " + e);
            }
        } else {
            builder.appendQueryParameter(PARAM_VERSION, sCachedVersionCode);
        }

        // Build the full uri and return it
        return builder.build();
    }

    public static long getTimeNow() {
        return SystemClock.elapsedRealtime();
    }

    /**
     * Calculate the amount by which the radius of a CircleTimerView should be offset by the any
     * of the extra painted objects.
     */
    public static float calculateRadiusOffset(
            float strokeSize, float dotStrokeSize, float markerStrokeSize) {
        return Math.max(strokeSize, Math.max(dotStrokeSize, markerStrokeSize));
    }

    /**
     * Uses {@link Utils#calculateRadiusOffset(float, float, float)} after fetching the values
     * from the resources just as {@link CircleTimerView#init(android.content.Context)} does.
     */
    public static float calculateRadiusOffset(Resources resources) {
        if (resources != null) {
            float strokeSize = resources.getDimension(R.dimen.circletimer_circle_size);
            float dotStrokeSize = resources.getDimension(R.dimen.circletimer_dot_size);
            float markerStrokeSize = resources.getDimension(R.dimen.circletimer_marker_size);
            return calculateRadiusOffset(strokeSize, dotStrokeSize, markerStrokeSize);
        } else {
            return 0f;
        }
    }

    /**  The pressed color used throughout the app. If this method is changed, it will not have
     *   any effect on the button press states, and those must be changed separately.
    **/
    public static int getPressedColorId() {
        return R.color.hot_pink;
    }

    /**  The un-pressed color used throughout the app. If this method is changed, it will not have
     *   any effect on the button press states, and those must be changed separately.
    **/
    public static int getGrayColorId() {
        return R.color.clock_gray;
    }

//    /** Runnable for use with screensaver and dream, to move the clock every minute.
//     *  registerViews() must be called prior to posting.
//     */
//    public static class ScreensaverMoveSaverRunnable implements Runnable {
//        static final long MOVE_DELAY = 60000; // DeskClock.SCREEN_SAVER_MOVE_DELAY;
//        static final long SLIDE_TIME = 10000;
//        static final long FADE_TIME = 3000;
//
//        static final boolean SLIDE = false;
//
//        private View mContentView, mSaverView;
//        private final Handler mHandler;
//
//        private static TimeInterpolator mSlowStartWithBrakes;
//
//
//        public ScreensaverMoveSaverRunnable(Handler handler) {
//            mHandler = handler;
//            mSlowStartWithBrakes = new TimeInterpolator() {
//                @Override
//                public float getInterpolation(float x) {
//                    return (float)(Math.cos((Math.pow(x,3) + 1) * Math.PI) / 2.0f) + 0.5f;
//                }
//            };
//        }
//
//        public void registerViews(View contentView, View saverView) {
//            mContentView = contentView;
//            mSaverView = saverView;
//        }
//
//        @Override
//        public void run() {
//            long delay = MOVE_DELAY;
//            if (mContentView == null || mSaverView == null) {
//                mHandler.removeCallbacks(this);
//                mHandler.postDelayed(this, delay);
//                return;
//            }
//
//            final float xrange = mContentView.getWidth() - mSaverView.getWidth();
//            final float yrange = mContentView.getHeight() - mSaverView.getHeight();
//
//            if (xrange == 0 && yrange == 0) {
//                delay = 500; // back in a split second
//            } else {
//                final int nextx = (int) (Math.random() * xrange);
//                final int nexty = (int) (Math.random() * yrange);
//
//                if (mSaverView.getAlpha() == 0f) {
//                    // jump right there
//                    mSaverView.setX(nextx);
//                    mSaverView.setY(nexty);
//                    ObjectAnimator.ofFloat(mSaverView, "alpha", 0f, 1f)
//                        .setDuration(FADE_TIME)
//                        .start();
//                } else {
//                    AnimatorSet s = new AnimatorSet();
//                    Animator xMove   = ObjectAnimator.ofFloat(mSaverView,
//                                         "x", mSaverView.getX(), nextx);
//                    Animator yMove   = ObjectAnimator.ofFloat(mSaverView,
//                                         "y", mSaverView.getY(), nexty);
//
//                    Animator xShrink = ObjectAnimator.ofFloat(mSaverView, "scaleX", 1f, 0.85f);
//                    Animator xGrow   = ObjectAnimator.ofFloat(mSaverView, "scaleX", 0.85f, 1f);
//
//                    Animator yShrink = ObjectAnimator.ofFloat(mSaverView, "scaleY", 1f, 0.85f);
//                    Animator yGrow   = ObjectAnimator.ofFloat(mSaverView, "scaleY", 0.85f, 1f);
//                    AnimatorSet shrink = new AnimatorSet(); shrink.play(xShrink).with(yShrink);
//                    AnimatorSet grow = new AnimatorSet(); grow.play(xGrow).with(yGrow);
//
//                    Animator fadeout = ObjectAnimator.ofFloat(mSaverView, "alpha", 1f, 0f);
//                    Animator fadein = ObjectAnimator.ofFloat(mSaverView, "alpha", 0f, 1f);
//
//
//                    if (SLIDE) {
//                        s.play(xMove).with(yMove);
//                        s.setDuration(SLIDE_TIME);
//
//                        s.play(shrink.setDuration(SLIDE_TIME/2));
//                        s.play(grow.setDuration(SLIDE_TIME/2)).after(shrink);
//                        s.setInterpolator(mSlowStartWithBrakes);
//                    } else {
//                        AccelerateInterpolator accel = new AccelerateInterpolator();
//                        DecelerateInterpolator decel = new DecelerateInterpolator();
//
//                        shrink.setDuration(FADE_TIME).setInterpolator(accel);
//                        fadeout.setDuration(FADE_TIME).setInterpolator(accel);
//                        grow.setDuration(FADE_TIME).setInterpolator(decel);
//                        fadein.setDuration(FADE_TIME).setInterpolator(decel);
//                        s.play(shrink);
//                        s.play(fadeout);
//                        s.play(xMove.setDuration(0)).after(FADE_TIME);
//                        s.play(yMove.setDuration(0)).after(FADE_TIME);
//                        s.play(fadein).after(FADE_TIME);
//                        s.play(grow).after(FADE_TIME);
//                    }
//                    s.start();
//                }
//
//                long now = System.currentTimeMillis();
//                long adjust = (now % 60000);
//                delay = delay
//                        + (MOVE_DELAY - adjust) // minute aligned
//                        - (SLIDE ? 0 : FADE_TIME) // start moving before the fade
//                        ;
//            }
//
//            mHandler.removeCallbacks(this);
//            mHandler.postDelayed(this, delay);
//        }
//    }

//    /** Setup to find out when the quarter-hour changes (e.g. Kathmandu is GMT+5:45) **/
//    public static long getAlarmOnQuarterHour() {
//        Calendar nextQuarter = Calendar.getInstance();
//        //  Set 1 second to ensure quarter-hour threshold passed.
//        nextQuarter.set(Calendar.SECOND, 1);
//        nextQuarter.set(Calendar.MILLISECOND, 0);
//        int minute = nextQuarter.get(Calendar.MINUTE);
//        nextQuarter.add(Calendar.MINUTE, 15 - (minute % 15));
//        long alarmOnQuarterHour = nextQuarter.getTimeInMillis();
//        long now = System.currentTimeMillis();
//        long delta = alarmOnQuarterHour - now;
//        if (0 >= delta || delta > 901000) {
//            // Something went wrong in the calculation, schedule something that is
//            // about 15 minutes. Next time , it will align with the 15 minutes border.
//            alarmOnQuarterHour = now + 901000;
//        }
//        return alarmOnQuarterHour;
//    }

//    // Setup a thread that starts at midnight plus one second. The extra second is added to ensure
//    // the date has changed.
//    public static void setMidnightUpdater(Handler handler, Runnable runnable) {
//        String timezone = TimeZone.getDefault().getID();
//        if (handler == null || runnable == null || timezone == null) {
//            return;
//        }
//        long now = System.currentTimeMillis();
//        Time time = new Time(timezone);
//        time.set(now);
//        long runInMillis = ((24 - time.hour) * 3600 - time.minute * 60 - time.second + 1) * 1000;
//        handler.removeCallbacks(runnable);
//        handler.postDelayed(runnable, runInMillis);
//    }

//    // Stop the midnight update thread
//    public static void cancelMidnightUpdater(Handler handler, Runnable runnable) {
//        if (handler == null || runnable == null) {
//            return;
//        }
//        handler.removeCallbacks(runnable);
//    }

//    // Setup a thread that starts at the quarter-hour plus one second. The extra second is added to
//    // ensure dates have changed.
//    public static void setQuarterHourUpdater(Handler handler, Runnable runnable) {
//        String timezone = TimeZone.getDefault().getID();
//        if (handler == null || runnable == null || timezone == null) {
//            return;
//        }
//        long runInMillis = getAlarmOnQuarterHour() - System.currentTimeMillis();
//        // Ensure the delay is at least one second.
//        if (runInMillis < 1000) {
//            runInMillis = 1000;
//        }
//        handler.removeCallbacks(runnable);
//        handler.postDelayed(runnable, runInMillis);
//    }

//    // Stop the quarter-hour update thread
//    public static void cancelQuarterHourUpdater(Handler handler, Runnable runnable) {
//        if (handler == null || runnable == null) {
//            return;
//        }
//        handler.removeCallbacks(runnable);
//    }

//    /**
//     * For screensavers to set whether the digital or analog clock should be displayed.
//     * Returns the view to be displayed.
//     */
//    public static View setClockStyle(Context context, View digitalClock, View analogClock,
//            String clockStyleKey) {
//        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
//        String defaultClockStyle = context.getResources().getString(R.string.default_clock_style);
//        String style = sharedPref.getString(clockStyleKey, defaultClockStyle);
//        View returnView;
//        if (style.equals(CLOCK_TYPE_ANALOG)) {
//            digitalClock.setVisibility(View.GONE);
//            analogClock.setVisibility(View.VISIBLE);
//            returnView = analogClock;
//        } else {
//            digitalClock.setVisibility(View.VISIBLE);
//            analogClock.setVisibility(View.GONE);
//            returnView = digitalClock;
//        }
//
//        return returnView;
//    }

//    /**
//     * For screensavers to dim the lights if necessary.
//     */
//    public static void dimClockView(boolean dim, View clockView) {
//        Paint paint = new Paint();
//        paint.setColor(Color.WHITE);
//        paint.setColorFilter(new PorterDuffColorFilter(
//                        (dim ? 0x40FFFFFF : 0xC0FFFFFF),
//                PorterDuff.Mode.MULTIPLY));
//        clockView.setLayerType(View.LAYER_TYPE_HARDWARE, paint);
//    }


    /** Clock views can call this to refresh their date. **/
    public static void updateDate(
            String dateFormat, String dateFormatForAccessibility, View clock) {

        Date now = new Date();
        TextView dateDisplay;
        dateDisplay = (TextView) clock.findViewById(R.id.date);
        if (dateDisplay != null) {
            final Locale l = Locale.getDefault();
            String fmt = DateFormat.getBestDateTimePattern(l, dateFormat);
            SimpleDateFormat sdf = new SimpleDateFormat(fmt, l);
            dateDisplay.setText(sdf.format(now));
            dateDisplay.setVisibility(View.VISIBLE);
            fmt = DateFormat.getBestDateTimePattern(l, dateFormatForAccessibility);
            sdf = new SimpleDateFormat(fmt, l);
            dateDisplay.setContentDescription(sdf.format(now));
        }
    }

    /***
     * Formats the time in the TextClock according to the Locale with a special
     * formatting treatment for the am/pm label.
     * @param clock - TextClock to format
     * @param amPmFontSize - size of the am/pm label since it is usually smaller
     *        than the clock time size.
     */
    public static void setTimeFormat(TextClock clock, int amPmFontSize) {
        if (clock != null) {
            // Get the best format for 12 hours mode according to the locale
            clock.setFormat12Hour(get12ModeFormat(amPmFontSize));
            // Get the best format for 24 hours mode according to the locale
            clock.setFormat24Hour(get24ModeFormat());
        }
    }
    /***
     * @param amPmFontSize - size of am/pm label (label removed is size is 0).
     * @return format string for 12 hours mode time
     */
    public static CharSequence get12ModeFormat(int amPmFontSize) {
        String skeleton = "hma";
        String pattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), skeleton);
        // Remove the am/pm
        if (amPmFontSize <= 0) {
            pattern.replaceAll("a", "").trim();
        }
        // Replace spaces with "Hair Space"
        pattern = pattern.replaceAll(" ", "\u200A");
        // Build a spannable so that the am/pm will be formatted
        int amPmPos = pattern.indexOf('a');
        if (amPmPos == -1) {
            return pattern;
        }
        Spannable sp = new SpannableString(pattern);
        sp.setSpan(new StyleSpan(Typeface.NORMAL), amPmPos, amPmPos + 1,
                Spannable.SPAN_POINT_MARK);
        sp.setSpan(new AbsoluteSizeSpan(amPmFontSize), amPmPos, amPmPos + 1,
                Spannable.SPAN_POINT_MARK);
        sp.setSpan(new TypefaceSpan("sans-serif"), amPmPos, amPmPos + 1,
                Spannable.SPAN_POINT_MARK);
        return sp;
    }

    public static CharSequence get24ModeFormat() {
        String skeleton = "Hm";
        return DateFormat.getBestDateTimePattern(Locale.getDefault(), skeleton);
    }

    /**
     * Returns string denoting the timezone hour offset (e.g. GMT-8:00)
     */
    public static String getGMTHourOffset(TimeZone timezone, boolean showMinutes) {
        StringBuilder sb = new StringBuilder();
        sb.append("GMT  ");
        int gmtOffset = timezone.getRawOffset();
        if (gmtOffset < 0) {
            sb.append('-');
        } else {
            sb.append('+');
        }
        sb.append(Math.abs(gmtOffset) / DateUtils.HOUR_IN_MILLIS); // Hour

        if (showMinutes) {
            final int min = (Math.abs(gmtOffset) / (int) DateUtils.MINUTE_IN_MILLIS) % 60;
            sb.append(':');
            if (min < 10) {
                sb.append('0');
            }
            sb.append(min);
        }

        return sb.toString();
    }

    public static int getCurrentHourColor() {
        final int hourOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        return Color.parseColor(BACKGROUND_SPECTRUM[hourOfDay]);
    }

    public static int getNextHourColor() {
        final int currHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        return Color.parseColor(BACKGROUND_SPECTRUM[currHour < 24 ? currHour + 1 : 1]);
    }

    /**
     * To get an array of single-character day of week symbols {'S', 'M', 'T', 'W', 'T', 'F', 'S'}
     * @return the array of symbols
     */
    public static String[] getShortWeekdays() {
        if (sShortWeekdays == null) {
            final String[] shortWeekdays = new String[7];
            final SimpleDateFormat format = new SimpleDateFormat("EEEEE");
            // Create a date (2014/07/20) that is a Sunday
            long aSunday = new GregorianCalendar(2014, Calendar.JULY, 20).getTimeInMillis();
            for (int day = 0; day < 7; day++) {
                shortWeekdays[day] = format.format(new Date(aSunday + day * DateUtils.DAY_IN_MILLIS));
            }
            sShortWeekdays = shortWeekdays;
        }
        return sShortWeekdays;
    }
}
