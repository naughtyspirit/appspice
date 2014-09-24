package it.appspice.android.client;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.List;

import it.appspice.android.client.events.Event;
import it.appspice.android.client.requests.CreateUser;
import it.appspice.android.client.requests.GetAdApps;
import it.appspice.android.client.requests.UpdateCounter;
import it.appspice.android.client.responses.Response;
import it.appspice.android.helpers.ConnectionManager;
import it.appspice.android.helpers.ConnectionManager.OnMsgReceiveListener;
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
public class AppSpiceClient implements OnMsgReceiveListener {
    public static final String TAG = AppSpiceClient.class.getSimpleName();

    private Context context;
    private ConnectionManager connectionManager;

    private String appSpiceId;
    private String appId;
    private String userId;

    private static AppSpiceClient instance;

    private static AppSpiceAdProvider adProvider = new AppSpiceAdProvider();

    public AppSpiceClient(Context context, String appSpiceId, String appId) {
        this.context = context;
        this.appSpiceId = appSpiceId;
        this.appId = appId;
        this.userId = SharedPreferencesHelper.getStringPreference(context, Constants.KEY_USER_ID);

        instance = this;

        connectionManager = ConnectionManager.getInstance(context);
        connectionManager.init(Constants.API_ENDPOINT, Constants.API_PROTOCOL, this);

        if (TextUtils.isEmpty(userId)) {
            createUser();
        } else {
            send(GetAdApps.EVENT_NAME, new GetAdApps(appSpiceId, appId, userId));
            startAppsInstalledService();
        }
    }

    private void send(String name, Object data) {
        connectionManager.send(new Event(name, data).toJSON());
    }

    private void createUser() {
        UniqueIdProvider.getAdvertisingId(context, new UniqueIdProvider.OnAdvertisingIdAvailable() {
            @Override
            public void onAdvertisingIdReady(String advertisingId) {
                if (TextUtils.isEmpty(advertisingId)) {
                    return;
                }

                userId = advertisingId;
                SharedPreferencesHelper.setPreference(context, Constants.KEY_USER_ID, advertisingId);

                List<String> packages = InstalledPackagesProvider.installedPackages(context.getPackageManager());

                CreateUser createUser = new CreateUser(appSpiceId, appId, advertisingId, packages);
                send(CreateUser.EVENT_NAME, createUser);

                send(GetAdApps.EVENT_NAME, new GetAdApps(appSpiceId, appId, advertisingId));
            }
        });
    }

    public static void cacheAds(Ads ads) {
        adProvider.cacheAds(ads);
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

    public void close() {
        connectionManager.close();
        adProvider.clearCachedAds();
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

    public static AdProvider getAdProvider() {
        return adProvider;
    }

    @Override
    public void onReceive(String str) {
        JsonObject jsonObject = new JsonParser().parse(str).getAsJsonObject();
        JsonArray jsonArray = jsonObject.getAsJsonArray("data");
        String eventName = jsonArray.get(0).getAsString();
        JsonElement data = jsonArray.get(1);

        try {
            Class<?> resultClass = Class.forName("it.appspice.android.client.responses." + eventName + "Response");
            Response resultHandler = (Response) resultClass.newInstance();
            resultHandler.onData(data);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }
}