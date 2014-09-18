package it.appspice.android.providers;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import it.appspice.android.helpers.Log;

import java.io.IOException;

/**
 * Created by NaughtySpirit
 * Created on 23/Aug/2014
 */
public class UniqueIdProvider {

    public static final String TAG = "client.providers.UniqueIdProvider";

    private final OnUniqueIdAvailable listener;
    private Context ctx;

    private AsyncTask<Void, Void, String> requestIdTask = new AsyncTask<Void, Void, String>() {

        @Override
        protected String doInBackground(Void... voids) {
            Info adInfo = null;

            try {
                adInfo = AdvertisingIdClient.getAdvertisingIdInfo(ctx);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            } catch (GooglePlayServicesNotAvailableException e) {
                Log.e(TAG, e.getMessage());
            } catch (GooglePlayServicesRepairableException e) {
                Log.e(TAG, e.getMessage());
            }

            final String id = adInfo.getId();
            final boolean isLAT = adInfo.isLimitAdTrackingEnabled();

            return id;
        }

        @Override
        protected void onPostExecute(String uniqueId) {
            listener.onUniqueId(uniqueId);
        }
    };

    public UniqueIdProvider(Context ctx, OnUniqueIdAvailable listener) {
        this.ctx = ctx;
        this.listener = listener;
    }

    public void requestId() {
        int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(ctx);
        if (result != ConnectionResult.SUCCESS) {
            Log.e(TAG, "Cannot obtain AdvertiserID.");
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
