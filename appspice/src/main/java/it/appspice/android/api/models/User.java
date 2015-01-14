package it.appspice.android.api.models;

import com.google.gson.annotations.Expose;

/**
 * Created by nmp on 1/14/15.
 */
public class User {
    @Expose
    private String advertisingId;

    public User(String advertisingId) {
        this.advertisingId = advertisingId;
    }

    public String getAdvertisingId() {
        return advertisingId;
    }

    public void setAdvertisingId(String advertisingId) {
        this.advertisingId = advertisingId;
    }
}
