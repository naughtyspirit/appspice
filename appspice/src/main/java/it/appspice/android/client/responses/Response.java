package it.appspice.android.client.responses;

/**
 * Created by NaughtySpirit
 * Created on 20/Aug/2014
 */
import com.google.gson.JsonElement;

public interface Response {
    void onData(JsonElement data);
}
