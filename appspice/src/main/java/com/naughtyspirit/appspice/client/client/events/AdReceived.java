package com.naughtyspirit.appspice.client.client.events;

import com.naughtyspirit.appspice.client.models.Ad;

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
