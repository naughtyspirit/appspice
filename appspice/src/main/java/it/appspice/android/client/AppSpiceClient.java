package it.appspice.android.client;

import android.content.Context;

import it.appspice.android.api.AppSpiceApiClient;
import it.appspice.android.api.EmptyCallback;
import it.appspice.android.api.models.User;
import it.appspice.android.helpers.Constants;
import it.appspice.android.helpers.SharedPreferencesHelper;
import it.appspice.android.models.Ads;
import it.appspice.android.providers.ads.AdProvider;
import it.appspice.android.providers.ads.AppSpiceAdProvider;
import retrofit.client.Response;

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

    //if (TextUtils.isEmpty(userId)) {
            createUser();
//        } else {
        sendGetAdsAndServiceEvent();
//        }
    }

    private void createUser() {
//        UniqueIdProvider.getAdvertisingId(context, new UniqueIdProvider.OnAdvertisingIdAvailable() {
//            @Override
//            public void onAdvertisingIdReady(String advertisingId) {
//                if (TextUtils.isEmpty(advertisingId)) {
//                    return;
//                }
//
//                tempAdvertisingId = advertisingId;
//                storeUsersAdvertisingId();
        AppSpiceApiClient.getClient().createUser(new User("test"), new EmptyCallback<Response>());
//            }
//        });
    }

    public void cacheAds(Ads ads) {
        adProvider.cacheAds(ads);
    }

    public void clearCachedAds() {
        adProvider.clearCachedAds();
    }

    public void sendGetAdsAndServiceEvent() {
        //TODO: Get Add Apps
    }

    public void displayConnectionResult() {

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
}