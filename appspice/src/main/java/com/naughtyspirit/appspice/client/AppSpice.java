package com.naughtyspirit.appspice.client;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.naughtyspirit.appspice.client.ad_screens.SingleAdDialog;
import com.naughtyspirit.appspice.client.client.AppspiceClient;
import com.naughtyspirit.appspice.client.helpers.ConnectivityHelper;
import com.naughtyspirit.appspice.client.helpers.Constants;
import com.naughtyspirit.appspice.client.helpers.Log;
import com.naughtyspirit.appspice.client.helpers.MetaDataHelper;
import com.naughtyspirit.appspice.client.models.Ads;

import java.util.Random;


/**
 * Created by NaughtySpirit
 * Created on 20/Aug/2014
 */
public class AppSpice {

    private static final String TAG = "Appspice";

    private static AppspiceClient client;
    private Context ctx;

    private static AppSpice instance;

    private static Ads cachedAds = new Ads();

    private AppSpice(Context ctx) {
        this.ctx = ctx;
    }

    public static void init(Context ctx, String devId, String appId) {
        if (instance == null) {
            instance = new AppSpice(ctx);
        }

        instance.initAppSpice(devId, appId);
    }

    public static void init(Context ctx) {
        String devId = MetaDataHelper.getMetaData(ctx, Constants.KEY_DEV_ID);
        String appId = MetaDataHelper.getMetaData(ctx, Constants.KEY_APP_ID);

        instance.initAppSpice(devId, appId);
    }

    private void initAppSpice(String devId, String appId) {

        if (devId.isEmpty() || appId.isEmpty()) {
            Log.e(TAG, "DEV_ID or APP_ID is empty.");
            return;
        }

        if (!ConnectivityHelper.isInternetEnabled(ctx)) {
            Log.e(TAG, "No internet connection!");
            return;
        }

        client = new AppspiceClient(devId, appId);
    }

    public static void showAd(final Context ctx) {
        instance.ctx = ctx;
        WaitForAdsThread waitForAdsThread = new WaitForAdsThread();
        waitForAdsThread.start();
    }

    public static void cacheAds(Ads ads) {
        cachedAds = ads;
    }

    private static class WaitForAdsThread extends Thread {

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
            instance.handler.sendMessage(msgObj);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.getData().getBoolean(Constants.KEY_IS_READY)) {
                SingleAdDialog adDialog = new SingleAdDialog(ctx, cachedAds.getData().get(new Random().nextInt(cachedAds.getData().size())));
                adDialog.show();
            }
        }
    };
}
