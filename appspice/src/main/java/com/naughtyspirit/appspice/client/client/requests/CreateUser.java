package com.naughtyspirit.appspice.client.client.requests;

import java.util.List;

/**
 * Created by NaughtySpirit
 * Created on 23/Aug/2014
 */
public class CreateUser {

    public static final String EVENT_NAME = CreateUser.class.getSimpleName();

    private String uniqueId;
    private List<String> installedApps;

    public CreateUser(String uniqueId, List<String> installedApps) {
        this.uniqueId = uniqueId;
        this.installedApps = installedApps;
    }
}
