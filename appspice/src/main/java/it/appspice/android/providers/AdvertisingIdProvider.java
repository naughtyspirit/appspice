package it.appspice.android.providers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import it.appspice.android.listeners.AdvertisingIdListener;

/**
 * Created by NaughtySpirit
 * Created on 23/Aug/2014
 */
public class AdvertisingIdProvider {

    public static final String TAG = "UniqueIdProvider";

    private static class ObtainAdvertisingIdTask extends AsyncTask<Void, Void, Info> {

        private Context ctx;
        private AdvertisingIdListener listener;

        public ObtainAdvertisingIdTask(Context ctx, AdvertisingIdListener listener) {
            this.ctx = ctx;
            this.listener = listener;
        }

        @Override
        protected Info doInBackground(Void... _) {
            Info adInfo = null;

            try {
                adInfo = AdvertisingIdClient.getAdvertisingIdInfo(ctx);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

            return adInfo;
        }

        @Override
        protected void onPostExecute(Info adInfo) {
            if (adInfo == null) {
                listener.onAdvertisingIdError();
            } else {
                listener.onAdvertisingIdReady(adInfo.getId(), adInfo.isLimitAdTrackingEnabled());
            }
        }
    }

    public static void get(Context ctx, AdvertisingIdListener listener) {
        ObtainAdvertisingIdTask task = new ObtainAdvertisingIdTask(ctx, listener);
        int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(ctx);
        if (result != ConnectionResult.SUCCESS) {
            listener.onAdvertisingIdError();
        } else {
            task.execute();
        }
    }

}
