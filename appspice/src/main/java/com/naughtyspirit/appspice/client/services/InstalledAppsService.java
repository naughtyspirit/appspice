package com.naughtyspirit.appspice.client.services;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by n0_m0r3_pa1n on 04.09.14.
 */
public class InstalledAppsService extends IntentService {
    public InstalledAppsService() {
        super("InstalledAppsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Toast.makeText(getBaseContext(), "Started", Toast.LENGTH_SHORT).show();
    }
}
