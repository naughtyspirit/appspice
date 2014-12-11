package it.appspice.android.helpers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Created by Naughty Spirit
 * on 9/26/14.
 */
public class JsonResponse {

    public static final String DATA_ARRAY = "data";
    private final String eventName;
    private final JsonElement data;

    public JsonResponse(String jsonData) {
        JsonObject jsonObject = new JsonParser().parse(jsonData).getAsJsonObject();
        JsonArray jsonArray = jsonObject.getAsJsonArray(DATA_ARRAY);
        eventName = jsonArray.get(0).getAsString();
        data = jsonArray.get(1);
    }

    public String getEventName() {
        return eventName;
    }

    public JsonElement getData() {
        return data;
    }
}
