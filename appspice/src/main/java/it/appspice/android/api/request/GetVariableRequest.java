package it.appspice.android.api.request;

import it.appspice.android.api.ApiClient;

/**
 * Created by Naughty Spirit <hi@naughtyspirit.co>
 * on 2/10/15.
 */
public class GetVariableRequest<T> implements HaltedApiRequest {
    private final String name;
    private final Class<T> clazz;
    private String appId;

    public GetVariableRequest(String name, String appId, Class<T> clazz) {
        this.name = name;
        this.appId = appId;
        this.clazz = clazz;
    }

    @Override
    public void populateFrom(RequestParameterProvider provider) {
    }

    @Override
    public void executeFrom(ApiClient apiClient) {
        apiClient.getVariable(name, appId, clazz);
    }
}
