package it.appspice.android.client.requests;

/**
 * Created by NaughtySpirit
 * Created on 25/Aug/2014
 */
public class UpdateCounter {

    public static final String EVENT_NAME = "UpdateCounter";

    private String appSpiceId;
    private String appId;
    private String namespace;
    private String eventName;
    private String userId;

    public UpdateCounter(String appSpiceId, String appId, String namespace, String eventName, String userId) {
        this.appSpiceId = appSpiceId;
        this.appId = appId;
        this.namespace = namespace;
        this.eventName = eventName;
        this.userId = userId;
    }
}
