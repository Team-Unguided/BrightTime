BrightTime
=============================================================================
BrightTime will allow users to adjust their screen brightness settings according to the time of day. It is tailored to users who have rigid schedules throughout the week and provide more control of display settings than the existing Adaptive Brightness option. This will hopefully increase battery life and provide more functionality to device displays (which consumes the majority of battery life). 

![BrightTime Header](http://shawnjzlee.me/img/brighttime.gif)

### Usage
`BrightTime` is a simple-to-use application. Installation onto the device is automatic. Feel free to download it on **Android 5.0.2+** [here](http://shawnjzlee.me/BrightTime-alpha001r1-5.0.1.apk)

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

![Screenshot 1](http://shawnjzlee.me/img/bt4.jpg)

Short Demo Below:

<a href="http://www.youtube.com/watch?feature=player_embedded&v=qFep5CybKuw
" target="_blank"><img src="http://img.youtube.com/vi/qFep5CybKuw/0.jpg" 
alt="Timed brightness option in Settings (In Development)" width="240" height="180" border="5" /></a>

### What's New
* Added an `Edit Preset` activity, where time and brightness can be edited or deleted.
* Included default presets when application is first installed on phone for ease-of-use.

### Future Implementations
* Allow users to `Undo` changes or deletion of their points.
* Clean up UI, icon, and code to fit [Google's design guidelines](http://www.google.com/design/spec/material-design/introduction.html) and [Code style guidelines](https://source.android.com/source/code-style.html). 

### Links
[Does Decreased Brightness Increase Your Phone's Battery Life | WIRED](http://www.wired.com/2013/04/does-decreased-brightness-increase-your-phones-battery-life/) - Increased brightness settings does decrease battery life, as seen through many tests within the article.

[Things Draining Phone Battery - Business Insider](http://www.businessinsider.com/sc/things-draining-phone-battery-2014-5) - One of the biggest reasons - according to this article - that battery life is drained is due to increased brightness settings.

[Google DeskClock App](https://android.googlesource.com/platform/packages/apps/DeskClock/+/android-5.1.0_r1) - A good reference for starting timer-based applications by using their alarm clock activity.

[Backlight! Open Source Project](http://www.phoeniix.com/android/backlight) - An application that attempted to save power by making it easy for users to cycle through different brightness levels by clicking an icon. 
