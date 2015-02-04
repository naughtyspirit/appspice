package it.appspice.android.api.models;

import com.google.gson.annotations.Expose;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Naughty Spirit
 * on 1/28/15.
 */
public class Event {

    public Event(String namespace, String name, Map<String, Object> data) {
        this.namespace = namespace;
        this.name = name;
        this.data = data;
    }

    public Event(String namespace, String name) {
        this(namespace, name, new HashMap<String, Object>());
    }

    public String getName() {
        return name;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public String getNamespace() {
        return namespace;
    }

    @Expose
    private String namespace;
    @Expose
    private String name;
    @Expose
    private Map<String, Object> data;
}
