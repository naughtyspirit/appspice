package it.appspice.android.services;

import android.app.IntentService;
import android.content.Intent;

import com.koushikdutta.async.http.WebSocket;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import it.appspice.android.AppSpice;
import it.appspice.android.client.AppSpiceClient;
import it.appspice.android.helpers.Constants;
import it.appspice.android.helpers.Log;
import it.appspice.android.providers.InstalledPackagesProvider;

/**
 * Created by NaughtySpirit
 * Created on 04/Sep/2014
 */
public class InstalledAppsService extends IntentService {

    private static final String TAG = "services.InstalledAppsService";

    private static String appSpiceId;
    private static String appId;

    private static List<String> installedApps = new ArrayList<String>();

    public InstalledAppsService() {
        super("InstalledAppsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate()");

        installedApps = InstalledPackagesProvider.installedPackages(getPackageManager());

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new GetPackages(), 500, 2000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy()");
    }

    private class GetPackages extends TimerTask {

        private WebSocket webSocket;

        @Override
        public void run() {

            List<String> newApps = new ArrayList<String>(),
                    removedApps = new ArrayList<String>();

            List<String> currentApps = InstalledPackagesProvider.installedPackages(getPackageManager());


            for (String currentPkg : currentApps) {
                if (installedApps.contains(currentPkg)) {
                    installedApps.remove(currentPkg);
                } else {
                    newApps.add(currentPkg);
                }
            }
            removedApps.addAll(installedApps);
            installedApps = currentApps;
            Log.e(TAG, "REMOVED: " + removedApps.toString());
            Log.e(TAG, "NEW: " + newApps.toString());

            AppSpiceClient.sendUpdateUserInstalledAppsEvent(newApps, removedApps);
        }
    }
}
