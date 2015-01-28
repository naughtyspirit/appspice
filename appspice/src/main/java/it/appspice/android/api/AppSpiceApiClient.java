package it.appspice.android.api;

import retrofit.RestAdapter;

/**
 * Created by nmp on 1/14/15.
 */
public class AppSpiceApiClient {
    private static AppSpiceApi client;
    public static AppSpiceApi getClient() {
        if(client == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint("http://10.0.2.2:8080")
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();

            client = restAdapter.create(AppSpiceApi.class);
        }

        return client;
    }
}
