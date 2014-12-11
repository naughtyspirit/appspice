package it.appspice.android.client.responses;

import com.google.gson.JsonElement;

import it.appspice.android.client.AppSpiceClient;

/**
 * Created by Naughty Spirit
 * on 9/26/14.
 */
public class UpdateCounterResponse extends BaseResponse {
    @Override
    public void onData(JsonElement data, AppSpiceClient client) {
        client.close();
    }
}
