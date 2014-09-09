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

    public CreateUser(String uniqueId, List<String> installedPackages) {
        this.uniqueId = uniqueId;
        this.installedPackages = installedPackages;
    }
}
