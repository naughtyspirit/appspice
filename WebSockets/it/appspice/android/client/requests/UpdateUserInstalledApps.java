package it.appspice.android.client.requests;

import java.util.List;

/**
 * Created by NaughtySpirit
 * Created on 13/Sep/2014
 */
public class UpdateUserInstalledApps {

    public static final String EVENT_NAME = UpdateUserInstalledApps.class.getSimpleName();

    private String userId;
    private List<String> newPackages;
    private List<String> removedPackages;

    public UpdateUserInstalledApps(String userId, List<String> newPackages, List<String> removedPackages) {
        this.userId = userId;
        this.newPackages = newPackages;
        this.removedPackages = removedPackages;
    }
}
