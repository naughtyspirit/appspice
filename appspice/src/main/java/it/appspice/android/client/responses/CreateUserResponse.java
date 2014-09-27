package it.appspice.android.client.responses;

import com.google.gson.JsonElement;

import it.appspice.android.client.AppSpiceClient;

/**
 * Created by NaughtySpirit
 * Created on 25/Sep/2014
 */
public class CreateUserResponse extends BaseResponse {

    @Override
    public void onData(JsonElement data, AppSpiceClient client) {
        client.storeUsersAdvertisingId();
        client.sendGetAdsAndServiceEvent();
    }
}
