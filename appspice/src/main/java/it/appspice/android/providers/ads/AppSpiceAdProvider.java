package it.appspice.android.providers.ads;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import it.appspice.android.helpers.Constants;
import it.appspice.android.helpers.Log;
import it.appspice.android.models.Ads;
import it.appspice.android.ui.dialogs.BaseAdDialog;
import it.appspice.android.ui.dialogs.FullScreenAd;
import it.appspice.android.ui.dialogs.WallAd;

import java.util.Random;

/**
 * Created by NaughtySpirit
 * Created on 23/Aug/2014
 */
public class AppSpiceAdProvider extends BaseAdProvider {
    public static final String PROVIDER_NAME = "appspice";

    private static Ads cachedAds = new Ads();
    private Context ctx;

    private Constants.AdTypes adType;

    private void startAdDialog(Context ctx, Constants.AdTypes adType) {
        this.ctx = ctx;

        this.adType = adType;

        WaitForAdsThread thread = new WaitForAdsThread();
        thread.start();
    }

    @Override
    public void onCreate(Activity activity) {

    }

    @Override
    public void onPause(Activity activity) {

    }

    @Override
    public void onResume(Activity activity) {

    }

    @Override
    public void onBackPressed(Activity activity) {

    }

    @Override
    public void showAd(Activity activity, Constants.AdTypes adType) {
        super.showAd(activity, adType);
    }

    @Override
    public void showFullScreen(Activity activity) {
        startAdDialog(activity, Constants.AdTypes.FullScreen);
    }

    @Override
    public void showAppWall(Activity activity) {
        startAdDialog(activity, Constants.AdTypes.Wall);
    }

    @Override
    public String getName() {
        return AppSpiceAdProvider.PROVIDER_NAME;
    }

    public static void cacheAds(Ads ads) {
        cachedAds = ads;
    }

    private class WaitForAdsThread extends Thread {

        private static final String TAG = ".providers.ads.AppSpiceAdProvider.WaitForAdsThread";

        @Override
        public void run() {
            try {
                while (cachedAds.getData().size() <= 0)
                    Thread.sleep(1000);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

            notifyUIThread();
        }

        private void notifyUIThread() {
            Message msgObj = new Message();
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constants.KEY_IS_READY, true);
            msgObj.setData(bundle);
            handler.sendMessage(msgObj);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.getData().getBoolean(Constants.KEY_IS_READY)) {
                BaseAdDialog adDialog;

                switch (adType) {
                    case FullScreen:
                        adDialog = new FullScreenAd(ctx, cachedAds.getData().get(new Random().nextInt(cachedAds.getData().size())));
                        adDialog.show();
                        break;

                    case Wall:
                        adDialog = new WallAd(ctx, cachedAds);
                        adDialog.show();
                        break;
                }
            }
        }
    };
}
