package com.naughtyspirit.appspice.client;

import android.app.Activity;

import com.naughtyspirit.appspice.client.client.AppSpiceClient;
import com.naughtyspirit.appspice.client.client.OnAppSpiceReadyListener;
import com.naughtyspirit.appspice.client.helpers.ConnectivityHelper;
import com.naughtyspirit.appspice.client.helpers.Constants;
import com.naughtyspirit.appspice.client.helpers.Constants.AdTypes;
import com.naughtyspirit.appspice.client.helpers.Log;
import com.naughtyspirit.appspice.client.helpers.MetaDataHelper;
import com.naughtyspirit.appspice.client.models.Ads;
import com.naughtyspirit.appspice.client.providers.InstalledPackagesProvider;
import com.naughtyspirit.appspice.client.providers.ads.AdProvider;
import com.naughtyspirit.appspice.client.providers.ads.AppSpiceAdProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by NaughtySpirit
 * Created on 20/Aug/2014
 */
public class AppSpice {

    private static final String TAG = "Appspice";

    private static AppSpiceClient client;
    private Activity ctx;

    private static AppSpice instance;
    private Map<String, AdProvider> adProviders = new HashMap<String, AdProvider>();

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

    public static void init(Activity ctx, String appSpiceId, String appId) {
        newInstance(ctx);

        instance.initAppSpice(appSpiceId, appId);
    }

    public static void init(Activity ctx) {
        newInstance(ctx);

        String appSpiceId = MetaDataHelper.getMetaData(ctx, Constants.KEY_APP_SPICE_ID);
        String appId = MetaDataHelper.getMetaData(ctx, Constants.KEY_APP_ID);

        instance.initAppSpice(appSpiceId, appId);
    }

    private void initAppSpice(String appSpiceId, String appId) {

        if (appSpiceId.isEmpty() || appId.isEmpty()) {
            Log.e(TAG, "APP_SPICE_ID or APP_SPICE_APP_ID is empty.");
            return;
        }

        if (!ConnectivityHelper.isInternetEnabled(ctx)) {
            Log.e(TAG, "No internet connection!");
            return;
        }

        client = new AppSpiceClient(ctx, appSpiceId, appId, new OnAppSpiceReadyListener() {
            @Override
            public void onReady() {
                List<String> packages = InstalledPackagesProvider.installedPackages(ctx.getPackageManager());
                Log.e(TAG, String.valueOf(packages.size()));
                client.createUser(packages);
            }
        });
    }

    public static void showAd(final Activity ctx) {
        instance.ctx = ctx;
        instance.adProviders.get(AppSpiceAdProvider.PROVIDER_NAME).showAd(ctx, AdTypes.FullScreen);
    }

    public static void cacheAds(Ads ads) {
        System.err.println("Ad " + ads.getData().size());
        AppSpiceAdProvider.cacheAds(ads);
    }

    private void initAdProviders() {
        adProviders.put(AppSpiceAdProvider.PROVIDER_NAME, new AppSpiceAdProvider());
        for (Map.Entry<String, AdProvider> entry : adProviders.entrySet()) {
            entry.getValue().onCreate(instance.ctx);
        }
    }
}
