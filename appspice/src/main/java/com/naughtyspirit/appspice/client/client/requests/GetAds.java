package com.naughtyspirit.appspice.client.client.requests;

/**
 * Created by NaughtySpirit
 * Created on 21/Aug/2014
 */
public class GetAds {

    public static final String EVENT_NAME = GetAds.class.getSimpleName();
    private final String devId;
    private final String appId;

    public GetAds(String devId, String appId) {
        this.devId = devId;
        this.appId = appId;
    }
}
