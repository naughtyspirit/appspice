package it.appspice.android.client.events;

import com.google.gson.Gson;
import it.appspice.android.helpers.Log;

/**
 * Created by NaughtySpirit
 * Created on 20/Aug/2014
 */
public class Event {
    private static final int DTO_EVENT_TYPE = 0;

    public Event(String name, Object data) {
        this.name = name;
        this.data = data;
    }

    private static class EventDTO {
        public int type;
        public Object data;
    }

    private String name;
    private Object data;

    public String toJSON() {
        Log.d("data", String.format("%s : %s", name, data.toString()));

        Object[] dataParam = new Object[] {name, data};
        EventDTO dto = new EventDTO();
        dto.type = DTO_EVENT_TYPE;
        dto.data = dataParam;
        Gson gson = new Gson();
        return gson.toJson(dto);
    }
}
