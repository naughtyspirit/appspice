package it.appspice.android.listeners;

/**
* Created by Naughty Spirit <hi@naughtyspirit.co>
* on 2/8/15.
*/
public interface AdvertisingIdListener {
    void onAdvertisingIdReady(String advertisingId, boolean isLimitAdTrackingEnabled);

    void onAdvertisingIdError();
}
