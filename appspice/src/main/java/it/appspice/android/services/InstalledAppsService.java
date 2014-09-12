package it.appspice.android.services;

import android.app.IntentService;
import android.content.Intent;

import it.appspice.android.helpers.Log;

/**
 * Created by NaughtySpirit
 * Created on 04/Sep/2014
 */
public class InstalledAppsService extends IntentService {

    private static final String TAG = "services.InstalledAppsService";

    public InstalledAppsService() {
        super("InstalledAppsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.e(TAG, "onCreate()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.e(TAG, "onDestroy()");
    }
}
