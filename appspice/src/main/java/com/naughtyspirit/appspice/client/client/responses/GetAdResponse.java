package com.naughtyspirit.appspice.client.client.responses;

import com.google.gson.JsonElement;
import com.naughtyspirit.appspice.client.models.Ad;

/**
 * Created by NaughtySpirit
 * Created on 20/Aug/2014
 */
public class GetAdResponse extends BaseResponse {

    @Override
    public void onData(JsonElement data) {
        Ad ad = gson.fromJson(data.getAsJsonObject(), Ad.class);
//        publish(new AdReceived(ad));
    }
}
