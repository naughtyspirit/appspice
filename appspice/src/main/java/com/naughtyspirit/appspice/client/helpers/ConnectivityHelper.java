package com.naughtyspirit.appspice.client.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by NaughtySpirit
 * Created on 20/Aug/2014
 */
public class ConnectivityHelper {

    public static boolean isInternetEnabled(Context context) {
        try {
            ConnectivityManager connection = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo wifi = connection.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobile = connection.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            return wifi.isAvailable() && wifi.isConnected() || mobile.isAvailable() && mobile.isConnected();
        } catch (Exception e) {
            return false;
        }
    }
}
