package com.naughtyspirit.appspice.client.client.requests;

/**
 * Created by NaughtySpirit
 * Created on 21/Aug/2014
 */
public class GetAdApps {

    public static final String EVENT_NAME = GetAdApps.class.getSimpleName();
    private final String devId;
    private final String appId;
    private final String userId;

    public GetAdApps(String devId, String appId, String userId) {
        this.devId = devId;
        this.appId = appId;
        this.userId = userId;
    }
}
