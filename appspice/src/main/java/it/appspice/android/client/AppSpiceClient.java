package it.appspice.android.client;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.google.gson.JsonElement;

import java.util.List;

import it.appspice.android.client.events.Event;
import it.appspice.android.client.requests.CreateUser;
import it.appspice.android.client.requests.GetAdApps;
import it.appspice.android.client.requests.UpdateCounter;
import it.appspice.android.client.responses.Response;
import it.appspice.android.helpers.ConnectionManager;
import it.appspice.android.helpers.ConnectionManager.OnMsgReceiveListener;
import it.appspice.android.helpers.Constants;
import it.appspice.android.helpers.JsonResponse;
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
public class AppSpiceClient implements OnMsgReceiveListener {
    public static final String TAG = AppSpiceClient.class.getSimpleName();

    private Context context;
    private ConnectionManager connectionManager;

    private static String appSpiceId;
    private static String appId;
    private static String userId;
    private String tempAdvertisingId;

    private static AppSpiceClient instance;

    private final AppSpiceAdProvider adProvider = new AppSpiceAdProvider();

    public AppSpiceClient(final Context context, String appSpId, String applicationId) {
        this.context = context;
        appSpiceId = appSpId;
        appId = applicationId;
        userId = SharedPreferencesHelper.getStringPreference(context, Constants.KEY_USER_ID);

        instance = this;

        connectionManager = new ConnectionManager(this);

        UniqueIdProvider.getAdvertisingId(context, new UniqueIdProvider.OnAdvertisingIdAvailable() {
            @Override
            public void onAdvertisingIdReady(String advertisingId) {
                tempAdvertisingId = advertisingId;

                if (TextUtils.isEmpty(advertisingId)) {
                    close();
                    return;
                }

                if (!TextUtils.isEmpty(userId) && userId.equals(advertisingId)) {
                    sendGetAdsAndServiceEvent();
                } else if (!userId.equals(advertisingId)) {
                    createUser();
                }
            }
        });
    }

    private void send(String name, Object data) {
        connectionManager.send(new Event(name, data).toJSON());
    }

    private void createUser() {
        List<String> packages = InstalledPackagesProvider.installedPackages(context.getPackageManager());

        CreateUser createUser = new CreateUser(appSpiceId, appId, userId, packages);
        send(CreateUser.EVENT_NAME, createUser);
    }

    public void cacheAds(Ads ads) {
        adProvider.cacheAds(ads);
    }

    public void clearCachedAds() {
        adProvider.clearCachedAds();
    }

    public void sendGetAdsAndServiceEvent() {
        startAppsInstalledService();
        send(GetAdApps.EVENT_NAME, new GetAdApps(appSpiceId, appId, userId));
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
        send(UpdateCounter.EVENT_NAME, new UpdateCounter(appSpiceId, appId, "appspice", eventName));
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

    @Override
    public void onReceive(String str) {

        JsonResponse response = new JsonResponse(str);
        String eventName = response.getEventName();
        JsonElement data = response.getData();

        try {
            Class<?> resultClass = Class.forName("it.appspice.android.client.responses." + eventName + "Response");
            Response resultHandler = (Response) resultClass.newInstance();
            resultHandler.onData(data, this);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    public void close() {
        connectionManager.close();
    }
}