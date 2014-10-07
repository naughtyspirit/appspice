# AppSpice Android SDK

Use AppSpice in your Android apps to promote and find engaged users for free.
Fast and Easy!

## Getting started

AppSpice supports both Eclipse and Android Studio (via gradle), so you are in
for a treat!

### Android Studio

Add the following dependency to your `build.gradle

```
compile 'it.appspice:android:+'
```


### Eclipse

### Update Android Manifest

Add the Internet permission

``` xml
<uses-permission android:name="android.permission.INTERNET" />
```

Add your AppSpice ID

``` xml
<meta-data android:name="APP_SPICE_ID" android:value="YOUR APP SPICE ID" />
```

Add your App ID

``` xml
<meta-data android:name="APP_SPICE_APP_ID" android:value="YOUR APP ID" />
```

Add the AppSpice service

``` xml
<service
    android:name="it.appspice.android.services.InstalledAppsService"
    android:exported="true"
    android:process="it.appspice.InstalledAppsService" />
```

### Init AppSpice

Add this code to your main Activity

```java
@Override
public void onCreate(Bundle savedInstance) {
    super.onCreate(savedInstance);
    setContentView(R.layout.main_activity);

    AppSpice.init(this);
    AppSpice.showAd(this);
}

@Override
protected void onDestroy() {
    super.onDestroy();
    AppSpice.onDestroy();
```

### Manage your App

Add awesomeness to your app [here](http://www.appspice.it/manage/apps/)

## License

Copyright (c) 2014 NaughtySpirit. Released under [MIT license](http://www.opensource.org/licenses/mit-license.php)
