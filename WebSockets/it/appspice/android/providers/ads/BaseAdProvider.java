package it.appspice.android.providers.ads;

import android.app.Activity;
import it.appspice.android.helpers.Constants;

/**
 * Created by NaughtySpirit
 * Created on 23/Aug/2014
 */
public abstract class BaseAdProvider implements AdProvider {

    @Override
    public void showAd(Activity activity, Constants.AdTypes adType) {
        switch (adType) {
            case Wall:
                showAppWall(activity);
                break;
            case FullScreen:
                showFullScreen(activity);
                break;
        }
    }
}
