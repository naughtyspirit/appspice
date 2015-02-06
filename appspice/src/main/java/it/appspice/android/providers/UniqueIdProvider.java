package it.appspice.android.providers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.io.IOException;

/**
 * Created by NaughtySpirit
 * Created on 23/Aug/2014
 */
public class UniqueIdProvider {

    public static final String TAG = "client.providers.UniqueIdProvider";

    private static class ObtainAdvertisingIdTask extends AsyncTask<Void, Void, Info> {

        private Context ctx;
        private AdvertisingIdListener listener;

        public ObtainAdvertisingIdTask(Context ctx, AdvertisingIdListener listener) {
            this.ctx = ctx;
            this.listener = listener;
        }

        @Override
        protected Info doInBackground(Void... voids) {
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

            if (adInfo != null) {
                return adInfo;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Info adInfo) {
            listener.onAdvertisingIdReady(adInfo.getId(), adInfo.isLimitAdTrackingEnabled());
        }
    }

    public static void getAdvertisingId(Context ctx, AdvertisingIdListener listener) {
        ObtainAdvertisingIdTask task = new ObtainAdvertisingIdTask(ctx, listener);

        int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(ctx);
        if (result != ConnectionResult.SUCCESS) {
            listener.onAdvertisingIdError();
        } else {
            task.execute();
        }
    }

    public interface AdvertisingIdListener {
        void onAdvertisingIdReady(String advertisingId, boolean isLimitAdTrackingEnabled);

        void onAdvertisingIdError();
    }
}
