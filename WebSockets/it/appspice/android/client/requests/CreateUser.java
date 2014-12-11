package it.appspice.android.client.requests;

import java.util.List;

/**
 * Created by NaughtySpirit
 * Created on 23/Aug/2014
 */
public class CreateUser {

    public static final String EVENT_NAME = CreateUser.class.getSimpleName();

    private String uniqueId;
    private List<String> installedPackages;
    private final String appSpiceId;
    private final String appId;

    public CreateUser(String appSpiceId, String appId, String uniqueId, List<String> installedPackages) {
        this.uniqueId = uniqueId;
        this.installedPackages = installedPackages;
        this.appSpiceId = appSpiceId;
        this.appId = appId;
    }
}
