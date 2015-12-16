BrightTime
=============================================================================
BrightTime adjusts your Android screen's brightness based on the time of day.
This [improves your phone's battery life](http://www.wired.com/2013/04/does-decreased-brightness-increase-your-phones-battery-life/).
Other projects like [Backlight!](http://www.phoeniix.com/android/backlight) make it easier to manually adjust your brightness settings to save power, but BrightTime completely automates this process.
Your phone will know the optimal brightness setting and adjust it for you.
The default install is suitable for most users, but advanced users can thoroughly customize the schedule. 

![BrightTime Header](http://shawnjzlee.me/img/brighttime.gif)

## Installation

BrightTime is easy to use. Currently, BrightTime requires **Android 5.0.2+**.

#### From your desktop:

1. <a href="http://goo.gl/EmKhmO" target="_blank">Click here</a> and click the `Begin Download` button.
2. Connect your phone to your computer and open its internal storage or SD card.
3. Locate the downloaded `BrightTime-xx.apk` file and move it to your phone directory.
4. Locate `BrightTime-xx.apk` on your phone and click it to begin installation.
5. Click `Install` and either `Done` or `Open` on the following screen.

![BrightTime Screen 1](http://shawnjzlee.me/img/bt_in0.png)

#### From your phone:

1. <a href="http://goo.gl/EmKhmO" target="_blank">Click here</a> and click the `Begin Download` button.
2. Open your notification bar and click the downloaded `BrightTime-xx.apk` file.
3. Click `Install` and either `Done` or `Open` on the following screen.

![BrightTime Screen 2](http://shawnjzlee.me/img/bt_in2.png)

## Adjusting the schedule

BrightTime comes with default settings that should be good enough for most users.
Advanced users can follow these directions to change your settings.

BrightTime uses points that allow users to set times at which the brightness changes.
* To create a point:
    1. Tap on the `+`, the circular pink button at the center bottom of the screen.
    2. Select your desired time and brightness.
    3. Once finished, click the `Confirm` button. **Point added!**
    4. You will return to the main screen where your added point will appear on the list by time.
    5. Your screen brightness will now change according to the brightness and times you have set.
* To edit/delete a point:
    1. Tap on the point you created previously.
    2. Delete the point altogether using the `Delete` button.
    3. Modify any settings for that point and press `Confirm` to save changes.
    4. To discard and go back, tap your `Back` button at either the top or bottom of your screen.

![Screenshot 1](http://shawnjzlee.me/img/bt4.jpg)

Short Demo Below:

<a href="http://www.youtube.com/watch?feature=player_embedded&v=qFep5CybKuw
" target="_blank"><img src="http://img.youtube.com/vi/qFep5CybKuw/0.jpg" 
alt="Timed brightness option in Settings (In Development)" width="240" height="180" border="5" /></a>

## Future Goals
* Allow users to `Undo` changes or deletion of their points.
* Clean up UI, icon, and code to fit [Google's design guidelines](http://www.google.com/design/spec/material-design/introduction.html) and [Code style guidelines](https://source.android.com/source/code-style.html). 
* Support for Android Jelly Bean (4.1 - 4.3.1) and KitKat (4.4)

## Building & Developing
The easiest way to build is to install [Android Studio](http://developer.android.com/sdk/index.html) with [Gradle](http://www.gradle.org/). After installation, you can import the project into Android Studio:

1. Download the project into a working directory.
2. Open Android Studio, click `Import Project`
3. Locate your working directory to import.
4. Click `OK`
5. Importing and building will be automatic via Gradle.

## Contact Us
Feel free to leave an issue or email us at [teamunguided@gmail.com](mailto:teamunguided@gmail.com).