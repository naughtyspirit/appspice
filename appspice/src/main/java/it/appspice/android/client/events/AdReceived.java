package it.appspice.android.client.events;

import it.appspice.android.models.Ad;

/**
 * Created by NaughtySpirit
 * Created on 20/Aug/2014
 */
public class AdReceived {

    private Ad ad;

    public AdReceived(Ad ad) {
        this.ad = ad;
    }

    public Ad getAd() {
        return ad;
    }
}
