package it.appspice.android.client;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.util.List;

import it.appspice.android.helpers.Constants;
import it.appspice.android.helpers.Log;
import it.appspice.android.helpers.SharedPreferencesHelper;
import it.appspice.android.models.Ads;
import it.appspice.android.providers.InstalledPackagesProvider;
import it.appspice.android.providers.UniqueIdProvider;
import it.appspice.android.providers.ads.AdProvider;
import it.appspice.android.providers.ads.AppSpiceAdProvider;
import it.appspice.android.services.InstalledAppsService;

/**
 * Created by NaughtySpirit
 * Created on 20/Aug/2014
 */
public class AppSpiceClient {
    public static final String TAG = AppSpiceClient.class.getSimpleName();

    private Context context;

    private static String appSpiceId;
    private static String appId;
    private static String userId;
    private String tempAdvertisingId;

    private static AppSpiceClient instance;

    private final AppSpiceAdProvider adProvider = new AppSpiceAdProvider();

    public AppSpiceClient(Context context, String appSpId, String applicationId) {
        this.context = context;
        appSpiceId = appSpId;
        appId = applicationId;
        userId = SharedPreferencesHelper.getStringPreference(context, Constants.KEY_USER_ID);

        instance = this;

        if (TextUtils.isEmpty(userId)) {
            createUser();
        } else {
            sendGetAdsAndServiceEvent();
        }
    }

    private void createUser() {
        UniqueIdProvider.getAdvertisingId(context, new UniqueIdProvider.OnAdvertisingIdAvailable() {
            @Override
            public void onAdvertisingIdReady(String advertisingId) {
                if (TextUtils.isEmpty(advertisingId)) {
                    return;
                }

                tempAdvertisingId = advertisingId;

                List<String> packages = InstalledPackagesProvider.installedPackages(context.getPackageManager());

                //TODO: Create user new CreateUser(appSpiceId, appId, advertisingId, packages);
            }
        });
    }

    public void cacheAds(Ads ads) {
        adProvider.cacheAds(ads);
    }

    public void clearCachedAds() {
        adProvider.clearCachedAds();
    }

    public void sendGetAdsAndServiceEvent() {
        startAppsInstalledService();
        //TODO: Get Add Apps
    }

    public void displayConnectionResult() {
        Ion.with(context)
                .load("http://10.0.2.2:8080/echo/123")
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> stringResponse) {
                        if(stringResponse == null) {
                            Log.d(TAG, "NULL");
                        } else {
                            Log.d(TAG, stringResponse.getResult());
                        }
                    }
                });

    }

    public static void displayResult() {
        instance.displayConnectionResult();
    }

    public static void sendAdImpressionEvent(String adProvider, String adType) {
        String eventName = String.format("%s.%s.ad.impression", adProvider, adType);
        instance.sendUpdateCounterEvent(eventName);
    }

    public static void sendAdClickEvent(String adProvider, String adType) {
        String eventName = String.format("%s.%s.ad.click", adProvider, adType);
        instance.sendUpdateCounterEvent(eventName);
    }

    private void sendUpdateCounterEvent(String eventName) {
        //TODO: Update counter: new UpdateCounter(appSpiceId, appId, "appspice", eventName)
    }

    public AdProvider getAdProvider() {
        return adProvider;
    }

    public void storeUsersAdvertisingId() {
        SharedPreferencesHelper.setPreference(context, Constants.KEY_USER_ID, tempAdvertisingId);
        userId = tempAdvertisingId;
    }

    private void startAppsInstalledService() {
        if (!isServiceRunning(InstalledAppsService.class)) {
            Intent serviceIntent = new Intent(context, InstalledAppsService.class);
            context.startService(serviceIntent);
        }
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}