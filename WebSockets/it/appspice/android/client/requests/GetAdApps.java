package it.appspice.android.client.requests;

/**
 * Created by NaughtySpirit
 * Created on 21/Aug/2014
 */
public class GetAdApps {

    public static final String EVENT_NAME = GetAdApps.class.getSimpleName();
    private final String appSpiceId;
    private final String appId;
    private final String userId;

    public GetAdApps(String appSpiceId, String appId, String userId) {
        this.appSpiceId = appSpiceId;
        this.appId = appId;
        this.userId = userId;
    }
}
