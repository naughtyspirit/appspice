package com.naughtyspirit.appspice.client;

import android.content.Context;
import android.widget.Toast;

import com.naughtyspirit.appspice.client.client.AppspiceClient;
import com.naughtyspirit.appspice.client.helpers.ConnectivityHelper;

/**
 * Author: Atanas Dimitrov <seishin90@yandex.ru>
 * Created on: 19/Aug/2014
 */
public class Appspice {

    private AppspiceClient client;
    private Context ctx;

    private static Appspice instance;

    public static Appspice getInstance(Context ctx) {
        if (instance == null) {
            instance = new Appspice(ctx.getApplicationContext());
        }

        return instance;
    }

    public Appspice(Context ctx) {
        this.ctx = ctx;
    }

    public void init(String devId, String appId) {

        if (!ConnectivityHelper.isInternetEnabled(ctx)) {
            Toast.makeText(ctx, "No internet connection!", Toast.LENGTH_SHORT).show();
            return;
        }

        client = new AppspiceClient(devId, appId);
    }

    public void showAd() {

    }
}
