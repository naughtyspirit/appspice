package com.naughtyspirit.appspice.client.client.responses;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.naughtyspirit.appspice.client.AppSpice;
import com.naughtyspirit.appspice.client.client.AppspiceClient;
import com.naughtyspirit.appspice.client.models.Ad;
import com.naughtyspirit.appspice.client.models.Ads;

import java.util.ArrayList;

/**
 * Created by NaughtySpirit
 * Created on 21/Aug/2014
 */
public class GetAdsResponse extends BaseResponse {

    @Override
    public void onData(JsonElement data) {
        Ads ads = new Ads();
        ArrayList<Ad> adsData = gson.fromJson(data, new TypeToken<ArrayList<Ad>>() {
        }.getType());
        ads.setData(adsData);

        AppSpice.cacheAds(ads);
    }
}