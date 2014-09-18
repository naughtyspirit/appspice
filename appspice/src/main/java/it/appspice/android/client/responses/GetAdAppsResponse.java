package it.appspice.android.client.responses;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import it.appspice.android.AppSpice;
import it.appspice.android.helpers.Log;
import it.appspice.android.models.Ad;
import it.appspice.android.models.Ads;

import java.util.ArrayList;

/**
 * Created by NaughtySpirit
 * Created on 21/Aug/2014
 */
public class GetAdAppsResponse extends BaseResponse {

    @Override
    public void onData(JsonElement data) {
        ArrayList<Ad> adsData = gson.fromJson(data, new TypeToken<ArrayList<Ad>>() {
        }.getType());

        Ads ads = new Ads();
        ads.setData(adsData);

        AppSpice.cacheAds(ads);
    }
}
