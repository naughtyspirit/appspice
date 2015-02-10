package it.appspice.android.api.models;

import com.google.gson.annotations.Expose;

import java.util.HashMap;
import java.util.Map;

import it.appspice.android.AppSpice;

/**
 * Created by Naughty Spirit
 * on 1/28/15.
 */
public class Event {

    public Event(String namespace, String name, String source, Map<String, Object> data) {
        this.namespace = namespace;
        this.name = name;
        this.source = source;
        this.data = data;
    }

    public Event(String namespace, String name, String source) {
        this(namespace, name, source, new HashMap<String, Object>());
    }

    public Event with(String key, Object value) {
        data.put(key, value);
        return this;
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

    public void track() {
        AppSpice.track(this);
    }

    @Expose
    private String namespace;
    @Expose
    private String name;
    @Expose
    private String source;
    @Expose
    private Map<String, Object> data;

    public String getSource() {
        return source;
    }
}
