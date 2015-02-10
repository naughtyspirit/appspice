package it.appspice.android.api.models;

import com.google.gson.annotations.Expose;

/**
 * Created by Naughty Spirit <hi@naughtyspirit.co>
 * on 2/10/15.
 */
public class Device {
    @Expose
    private String os;
    @Expose
    private String osVersion;
    @Expose
    private String model;

    public Device(String os, String osVersion, String model) {
        this.os = os;
        this.osVersion = osVersion;
        this.model = model;
    }
}
