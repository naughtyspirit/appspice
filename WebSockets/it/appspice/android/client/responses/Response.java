package it.appspice.android.client.responses;

/**
 * Created by NaughtySpirit
 * Created on 20/Aug/2014
 */
import com.google.gson.JsonElement;

import it.appspice.android.client.AppSpiceClient;

public interface Response {
    void onData(JsonElement data, AppSpiceClient client);
}
