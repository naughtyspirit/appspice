package it.appspice.android.api.models;

import com.google.gson.annotations.Expose;

import java.util.Map;

/**
 * Created by Naughty Spirit
 * on 1/28/15.
 */
public class Event {

    public Event(String name, Map<String, Object> data) {
        this.name = name;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public Map<String, Object> getData() {
        return data;
    }

    @Expose
    private String name;
    @Expose
    private Map<String, Object> data;


}
