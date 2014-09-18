package it.appspice.android.services;

import android.app.IntentService;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import it.appspice.android.client.events.Event;
import it.appspice.android.client.requests.UpdateUserInstalledApps;
import it.appspice.android.helpers.ConnectionManager;
import it.appspice.android.helpers.Constants;
import it.appspice.android.helpers.Log;
import it.appspice.android.helpers.MetaDataHelper;
import it.appspice.android.helpers.SharedPreferencesHelper;
import it.appspice.android.providers.InstalledPackagesProvider;

/**
 * Created by NaughtySpirit
 * Created on 04/Sep/2014
 */
public class InstalledAppsService extends IntentService {

    private static final String TAG = InstalledAppsService.class.getSimpleName();

    private String appSpiceId;
    private String appId;
    private String userId;

    private ConnectionManager connectionManager;

    private static List<String> installedApps = new ArrayList<String>();

    public InstalledAppsService() {
        super("InstalledAppsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.appSpiceId = MetaDataHelper.getMetaData(getApplication(), Constants.KEY_APP_SPICE_ID);
        this.appId = MetaDataHelper.getMetaData(getApplication(), Constants.KEY_APP_ID);
        this.userId = SharedPreferencesHelper.getStringPreference(getApplication(), Constants.KEY_USER_ID);

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        installedApps = InstalledPackagesProvider.installedPackages(getPackageManager());

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new GetPackages(), 500, Constants.INSTALLED_APPS_SERVICE_INTERVAL);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (connectionManager != null && connectionManager.isWebSocketOpen()) {
            connectionManager.close();
        }
    }

    private class GetPackages extends TimerTask {

        @Override
        public void run() {

            final List<String> newApps = new ArrayList<String>(),
                    removedApps = new ArrayList<String>();

            final List<String> currentApps = InstalledPackagesProvider.installedPackages(getPackageManager());

            for (String currentPkg : currentApps) {
                if (installedApps.contains(currentPkg)) {
                    installedApps.remove(currentPkg);
                } else {
                    newApps.add(currentPkg);
                }
            }
            removedApps.addAll(installedApps);
            installedApps = currentApps;

            if (removedApps.size() > 0 || newApps.size() > 0) {
                connectionManager = ConnectionManager.getInstance(getApplicationContext());
                connectionManager.init(Constants.API_ENDPOINT, Constants.API_PROTOCOL, new ConnectionManager.OnMsgReceiveListener() {
                    @Override
                    public void onReceive(String str) {
                        Log.e(TAG, str);
                    }
                });

                connectionManager.send(new Event(UpdateUserInstalledApps.EVENT_NAME,
                        new UpdateUserInstalledApps(appSpiceId, appId, userId, newApps, removedApps)).toJSON());
            }
        }
    }
}
