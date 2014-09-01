package com.naughtyspirit.appspice.client;

import android.app.Activity;

import com.naughtyspirit.appspice.client.client.AppspiceClient;
import com.naughtyspirit.appspice.client.client.OnAppSpiceReadyListener;
import com.naughtyspirit.appspice.client.helpers.ConnectivityHelper;
import com.naughtyspirit.appspice.client.helpers.Constants;
import com.naughtyspirit.appspice.client.helpers.Constants.AdTypes;
import com.naughtyspirit.appspice.client.helpers.Log;
import com.naughtyspirit.appspice.client.helpers.MetaDataHelper;
import com.naughtyspirit.appspice.client.models.Ads;
import com.naughtyspirit.appspice.client.providers.InstalledAppsProvider;
import com.naughtyspirit.appspice.client.providers.ads.AdProvider;
import com.naughtyspirit.appspice.client.providers.ads.AppSpiceAdProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by NaughtySpirit
 * Created on 20/Aug/2014
 */
public class AppSpice {

    private static final String TAG = "Appspice";

    private static AppspiceClient client;
    private Activity ctx;

    private static AppSpice instance;
    private Map<String, AdProvider> adProviders = new HashMap<String, AdProvider>();

    private String userId;

    private AppSpice(Activity ctx) {
        this.ctx = ctx;
    }

    private static void newInstance(Activity ctx) {
        if (instance == null) {
            instance = new AppSpice(ctx);
        }

        instance.ctx = ctx;

        instance.initAdProviders();
    }

    public static void init(Activity ctx, String devId, String appId) {
        newInstance(ctx);

        instance.initAppSpice(devId, appId);
    }

    public static void init(Activity ctx) {
        newInstance(ctx);

        String devId = MetaDataHelper.getMetaData(ctx, Constants.KEY_DEV_ID);
        String appId = MetaDataHelper.getMetaData(ctx, Constants.KEY_APP_ID);

        instance.initAppSpice(devId, appId);
    }

    private void initAppSpice(String devId, String appId) {

        if (devId.isEmpty() || appId.isEmpty()) {
            Log.e(TAG, "DEV_ID or APP_ID is empty.");
            return;
        }

        if (!ConnectivityHelper.isInternetEnabled(ctx)) {
            Log.e(TAG, "No internet connection!");
            return;
        }

        client = new AppspiceClient(ctx, devId, appId, userId, new OnAppSpiceReadyListener() {
            @Override
            public void onReady() {
                List<String> apps = InstalledAppsProvider.installedApps(ctx.getPackageManager());
                Log.e(TAG, String.valueOf(apps.size()));
                client.createUser(apps);
            }
        });
    }

    public static void showAd(final Activity ctx) {
        instance.ctx = ctx;
        instance.adProviders.get(AppSpiceAdProvider.PROVIDER_NAME).showAd(ctx, AdTypes.FullScreen);
    }

    public static void cacheAds(Ads ads) {
        AppSpiceAdProvider.cacheAds(ads);
    }

    private void initAdProviders() {
        adProviders.put(AppSpiceAdProvider.PROVIDER_NAME, new AppSpiceAdProvider());
        for (Map.Entry<String, AdProvider> entry : adProviders.entrySet()) {
            entry.getValue().onCreate(instance.ctx);
        }
    }
}
