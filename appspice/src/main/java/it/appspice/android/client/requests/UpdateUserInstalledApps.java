package it.appspice.android.client.requests;

import java.util.List;

/**
 * Created by NaughtySpirit
 * Created on 13/Sep/2014
 */
public class UpdateUserInstalledApps {

    public static final String EVENT_NAME = UpdateUserInstalledApps.class.getSimpleName();

    private String appSpiceId;
    private String appId;
    private String userId;
    private List<String> newApps;
    private List<String> removedApps;

    public UpdateUserInstalledApps(String appSpiceId, String appId, String userId, List<String> newApps, List<String> removedApps) {
        this.appSpiceId = appSpiceId;
        this.appId = appId;
        this.userId = userId;
        this.newApps = newApps;
        this.removedApps = removedApps;
    }
}
