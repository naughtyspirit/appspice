package it.appspice.android;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

import it.appspice.android.client.AppSpiceClient;
import it.appspice.android.helpers.Constants;
import it.appspice.android.helpers.Constants.AdTypes;
import it.appspice.android.helpers.Log;
import it.appspice.android.helpers.MetaDataHelper;
import it.appspice.android.models.Ads;
import it.appspice.android.providers.ads.AdProvider;
import it.appspice.android.providers.ads.AppSpiceAdProvider;


/**
 * Created by NaughtySpirit
 * Created on 20/Aug/2014
 */
public class AppSpice {

    private static final String TAG = "Appspice";

    private static AppSpiceClient client;
    private Context ctx;

    private static AppSpice instance;
    private Map<String, AdProvider> adProviders = new HashMap<String, AdProvider>();

    private AppSpice(Context ctx) {
        this.ctx = ctx;
    }

    private static void newInstance(Context ctx) {
        if (instance == null) {
            instance = new AppSpice(ctx);
        }

        instance.ctx = ctx;

        instance.initAdProviders();
    }

    public static void init(Context ctx, String appSpiceId, String appId) {
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

        if (TextUtils.isEmpty(appSpiceId) || TextUtils.isEmpty(appId)) {
            Log.e(TAG, "APP_SPICE_ID or APP_SPICE_APP_ID is empty.");
            return;
        }

        client = new AppSpiceClient(ctx, appSpiceId, appId);
    }

    public static void showAd(final Activity context) {
        instance.ctx = context;
        instance.adProviders.get(AppSpiceAdProvider.PROVIDER_NAME).showAd(context, AdTypes.FullScreen);
    }

    public static void cacheAds(Ads ads) {
        ((AppSpiceAdProvider) instance.adProviders.get(AppSpiceAdProvider.PROVIDER_NAME)).cacheAds(ads);
    }

    private void initAdProviders() {
        adProviders.put(AppSpiceAdProvider.PROVIDER_NAME, new AppSpiceAdProvider());
        for (Map.Entry<String, AdProvider> entry : adProviders.entrySet()) {
//            entry.getValue().onCreate((Activity) instance.ctx);
        }
    }

    public static void close() {
        client.close();
        ((AppSpiceAdProvider) instance.adProviders.get(AppSpiceAdProvider.PROVIDER_NAME)).clearCachedAds();
    }
}
