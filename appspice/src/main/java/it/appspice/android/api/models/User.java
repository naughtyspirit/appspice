package it.appspice.android.api.models;

import com.google.gson.annotations.Expose;

/**
 * Created by nmp on 1/14/15.
 */
public class User {
    @Expose
    private String id;
    @Expose
    private final String country;
    @Expose
    private final String language;
    @Expose
    private Device device;

    public User(String id, String country, String language, Device device) {
        this.id = id;
        this.country = country;
        this.language = language;
        this.device = device;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
