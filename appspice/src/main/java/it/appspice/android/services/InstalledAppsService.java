package it.appspice.android.services;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by n0_m0r3_pa1n on 04.09.14.
 */
public class InstalledAppsService extends IntentService {
    public InstalledAppsService() {
        super("InstalledAppsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
