package it.appspice.android.client.requests;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NaughtySpirit
 * Created on 13/Sep/2014
 */
public class UpdateUserInstalledApps {

    public static final String EVENT_NAME = UpdateUserInstalledApps.class.getSimpleName();

    private List<String> newApps = new ArrayList<String>();
    private List<String> removedApps = new ArrayList<String>();

    public UpdateUserInstalledApps(List<String> newApps, List<String> removedApps) {
        this.newApps = newApps;
        this.removedApps = removedApps;
    }
}
