package it.appspice.android.providers;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.io.IOException;

import it.appspice.android.helpers.Log;

/**
 * Created by NaughtySpirit
 * Created on 23/Aug/2014
 */
public class UniqueIdProvider {

    public static final String TAG = "client.providers.UniqueIdProvider";

    private static class ObtainAdvertisingIdTask extends AsyncTask<Void, Void, String> {

        private Context ctx;
        private OnAdvertisingIdAvailable listener;

        public ObtainAdvertisingIdTask(Context ctx, OnAdvertisingIdAvailable listener) {
            this.ctx = ctx;
            this.listener = listener;
        }

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

            if (adInfo != null) {
                return adInfo.getId();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String id) {
            listener.onAdvertisingIdReady(id);
        }
    }

    public static void getAdvertisingId(Context ctx, OnAdvertisingIdAvailable listener) {
        ObtainAdvertisingIdTask task = new ObtainAdvertisingIdTask(ctx, listener);

        int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(ctx);
        if (result != ConnectionResult.SUCCESS) {
            try {
                GooglePlayServicesUtil.showErrorDialogFragment(result, (Activity) ctx, 0);
            } catch (Exception e) {
                try {
                    GooglePlayServicesUtil.getErrorDialog(result, (Activity) ctx, 0).show();
                } catch (Exception ex) {}
            }
        } else {
            task.execute();
        }
    }

    public interface OnAdvertisingIdAvailable {
        void onAdvertisingIdReady(String advertisingId);
    }
}
