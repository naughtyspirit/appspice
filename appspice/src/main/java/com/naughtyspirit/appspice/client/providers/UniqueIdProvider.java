package com.naughtyspirit.appspice.client.providers;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Created by NaughtySpirit
 * Created on 23/Aug/2014
 */
public class UniqueIdProvider {

    private final OnUniqueIdAvailable listener;
    private Context context;

    private AsyncTask<Void, Void, String> requestIdTask = new AsyncTask<Void, Void, String>() {

        @Override
        protected String doInBackground(Void... voids) {
            AdvertisingIdClient.Info adInfo = null;
            try {
                adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
                return adInfo.getId();
            } catch (Exception e) {
                return generateDeviceId();
            }
        }

        @Override
        protected void onPostExecute(String uniqueId) {
            listener.onUniqueId(uniqueId);
        }
    };

    public UniqueIdProvider(Context context, OnUniqueIdAvailable listener) {
        this.context = context;
        this.listener = listener;
    }

    public void requestId() {
        int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (result != ConnectionResult.SUCCESS) {
            listener.onUniqueId(generateDeviceId());
        } else {
            requestIdTask.execute();
        }
    }

    public String generateDeviceId() {
        return "42"
                + Build.BOARD.length() % 10 + Build.BRAND.length() % 10
                + Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10
                + Build.DISPLAY.length() % 10 + Build.HOST.length() % 10
                + Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10
                + Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10
                + Build.TAGS.length() % 10 + Build.TYPE.length() % 10
                + Build.USER.length() % 10;
    }

    public interface OnUniqueIdAvailable {
        void onUniqueId(String uniqueId);
    }
}
