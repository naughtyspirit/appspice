package it.appspice.android.api.request;

import it.appspice.android.api.ApiClient;

/**
 * Created by Naughty Spirit <hi@naughtyspirit.co>
 * on 2/10/15.
 */
public interface HaltedApiRequest {
    void populateFrom(RequestParameterProvider provider);

    void executeFrom(ApiClient apiClient);
}
