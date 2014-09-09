package it.appspice.android.providers.ads;


import android.app.Activity;

import it.appspice.android.helpers.Constants;

/**
 * Created by NaughtySpirit
 * Created on 23/Aug/2014
 */
public interface AdProvider {

    void onCreate(Activity activity);

    void onPause(Activity activity);

    void onResume(Activity activity);

    void onBackPressed(Activity activity);

    void showFullScreen(Activity activity);

    void showAppWall(Activity activity);

    void showAd(Activity activity, Constants.AdTypes adType);

    String getName();
}
