package com.naughtyspirit.appspice.client.client.requests;

/**
 * Created by NaughtySpirit
 * Created on 25/Aug/2014
 */
public class UpdateCounter {

    public static final String EVENT_NAME = "UpdateCounter";

    private String appSpiceId;
    private String namespace;
    private String eventName;
    private String userId;

    public UpdateCounter(String appSpiceId, String namespace, String eventName, String userId) {
        this.appSpiceId = appSpiceId;
        this.namespace = namespace;
        this.eventName = eventName;
        this.userId = userId;
    }
}
