package it.appspice.android.client.responses;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import it.appspice.android.client.AppSpiceClient;
import it.appspice.android.models.Ad;
import it.appspice.android.models.Ads;

/**
 * Created by NaughtySpirit
 * Created on 21/Aug/2014
 */
public class GetAdAppsResponse extends BaseResponse {

    @Override
    public void onData(JsonElement data, AppSpiceClient client) {
        ArrayList<Ad> adsData = gson.fromJson(data, new TypeToken<ArrayList<Ad>>() {
        }.getType());

        Ads ads = new Ads();
        ads.setData(adsData);

        client.cacheAds(ads);
    }
}
