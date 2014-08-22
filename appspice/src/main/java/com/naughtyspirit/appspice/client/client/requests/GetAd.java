package com.naughtyspirit.appspice.client.client.requests;

/**
 * Created by NaughtySpirit
 * Created on 20/Aug/2014
 */
public class GetAd {
    public static final String EVENT_NAME = GetAd.class.getSimpleName();
    private final String devId;
    private final String appId;

    public GetAd(String devId, String appId) {
        this.devId = devId;
        this.appId = appId;
    }
}
