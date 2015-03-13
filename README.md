BrightTime `com.unguided.BrightTime`
=============================================================================
### Initial Development: Nexus 5 (hammerhead) on Android 5.0.2
### Description
`BrightTime` (aka `Timed Brightness`) is an open-source application that will allow users to adjust their screen brightness settings according to the time of day. It is tailored to users who have rigid schedules throughout the week and provide more control of display settings than the existing Adaptive Brightness option. This will hopefully increase battery life and provide more functionality to device displays (which consumes the majority of battery life). 

### Usage
`BrightTime` is a simple-to-use application. Installation onto the device is automatic.
* To create a "point" (a preset):
    - tap on the `+`, the circular pink button at the center bottom of the screen.
    - Select your desired time and brightness.
    - Once finished, click the `Confirm` button. **Point added!**
    - You will return to the main screen where your added point will appear on the list by time.
    - Your screen brightness will now change according to the brightness and times you have set.
* To edit/delete a "point":
    - tap on the point you have created previously.
    - delete the point altogether using the `Delete` button.
    - modify any settings for that point and press `Confirm` to save changes.
    - to discard and go back, tap your `Back` button at either the top or bottom of your screen.

Short Demo Below:

<a href="http://www.youtube.com/watch?feature=player_embedded&v=i8_A7HRHG_8
" target="_blank"><img src="http://img.youtube.com/vi/i8_A7HRHG_8/0.jpg" 
alt="Timed brightness option in Settings (In Development)" width="240" height="180" border="10" /></a>

### Future Implementations
* Add an `Edit Point` page, where time, days-of-week, and brightness can be edited.
	- **[NEW (03/13/15)]** Created `Edit Point` page by tapping on the point in the list. Allows users to delete or modify their settings for that point.
* Add `Delete Point` option
    - **[NEW (03/13/15)]** See above~
* Allow users to `Undo` changes or deletion of their points.
* Clean up UI, icon, and code to fit [Google's design guidelines](http://www.google.com/design/spec/material-design/introduction.html) and [Code style guidelines](https://source.android.com/source/code-style.html). 
* Allow users to `Undo` changes or deletion of their points.

### Sources / Links
[CyanogenMod Settings App](https://github.com/CyanogenMod/android_packages_apps_Settings)

[Google DeskClock App](https://android.googlesource.com/platform/packages/apps/DeskClock/+/kitkat-release)

[Using lists in Android](http://www.vogella.com/tutorials/AndroidListView/article.html#androidlists_overview)

[Android Development - Packages](http://developer.android.com/reference/packages.html)

[Android TimePicker Tutorial](https://www.youtube.com/watch?v=UpyzYwy_Tjw)

[Backlight! Open Source Project](http://www.phoeniix.com/android/backlight)

[Compile Time Error -Example of seekBar](http://www.compiletimeerror.com/2013/09/android-seekbar-example.html#.VPV6sPnF-ao)
