package it.appspice.android.providers;

import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;

import it.appspice.android.listeners.AdvertisingIdListener;

/**
 * Created by NaughtySpirit
 * Created on 23/Aug/2014
 */
public class AdvertisingIdProvider {

    public static final String TAG = AdvertisingIdProvider.class.getSimpleName();

    private static class GetAdvertisingIdTask extends AsyncTask<Void, Void, Info> {

        private Context ctx;
        private AdvertisingIdListener listener;
        private Throwable error;

        public GetAdvertisingIdTask(Context ctx, AdvertisingIdListener listener) {
            this.ctx = ctx;
            this.listener = listener;
        }

        @Override
        protected Info doInBackground(Void... _) {
            Info adInfo = null;

            try {
                adInfo = AdvertisingIdClient.getAdvertisingIdInfo(ctx);
            } catch (Exception e) {
                error = e;
            }

            return adInfo;
        }

        @Override
        protected void onPostExecute(Info adInfo) {
            if (error != null) {
                listener.onAdvertisingIdError(error);
            } else {
                listener.onAdvertisingIdReady(adInfo.getId(), adInfo.isLimitAdTrackingEnabled());
            }
        }
    }

    public static void get(Context ctx, AdvertisingIdListener listener) {
        GetAdvertisingIdTask task = new GetAdvertisingIdTask(ctx, listener);
        task.execute();
    }

}
