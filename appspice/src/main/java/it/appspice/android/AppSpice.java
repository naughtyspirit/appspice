package it.appspice.android;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import it.appspice.android.client.AppSpiceClient;
import it.appspice.android.helpers.Constants;
import it.appspice.android.helpers.Constants.AdTypes;
import it.appspice.android.helpers.Log;
import it.appspice.android.helpers.MetaDataHelper;


/**
 * Created by NaughtySpirit
 * Created on 20/Aug/2014
 */
public class AppSpice {

    private static final String TAG = AppSpice.class.getSimpleName();

    private static AppSpiceClient client;
    private Context context;

    private static AppSpice instance;

    private AppSpice(Context context) {
        this.context = context;
    }

    private static void newInstance(Context context) {
        if (instance == null) {
            instance = new AppSpice(context);
        }

        instance.context = context;
    }

    /**
     * Initializing the AppSpice SDK with providing AppSpiceId and AppId in the constructor.
     *
     * @param appContext Application's Context
     * @param appSpiceId Developer's appSpiceId
     * @param appId      Application's appId
     */
    public static void init(Context appContext, String appSpiceId, String appId) {
        newInstance(appContext);

        instance.initAppSpice(appSpiceId, appId);
    }

    /**
     * Initializing the AppSpice SDK with using the AppSpiceId and AppId provided in the Android Manifest
     * Meta Data.
     *
     * @param appContext Application's Context
     */
    public static void init(Context appContext) {
        newInstance(appContext);

        String appSpiceId = MetaDataHelper.getMetaData(appContext, Constants.KEY_APP_SPICE_ID);
        String appId = MetaDataHelper.getMetaData(appContext, Constants.KEY_APP_ID);

        instance.initAppSpice(appSpiceId, appId);
    }

    private void initAppSpice(String appSpiceId, String appId) {

        if (TextUtils.isEmpty(appSpiceId) || TextUtils.isEmpty(appId)) {
            Log.e(TAG, "APP_SPICE_ID or APP_SPICE_APP_ID is empty.");
            return;
        }

        client = new AppSpiceClient(context, appSpiceId, appId);
    }

    /**
     * Showing an Advertisement on the current Activity.
     *
     * @param activity Current Activity
     */
    public static void showAd(Activity activity) {
        instance.context = activity;
        client.getAdProvider().showAd(activity, AdTypes.FullScreen);
    }

    /**
     * Closing the AppSpice SDK connection.
     */
    public static void onDestroy() {
        client.close();
        client.clearCachedAds();
    }
}