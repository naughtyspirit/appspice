package it.appspice.android.api.request;

import it.appspice.android.api.ApiClient;

/**
 * Created by Naughty Spirit <hi@naughtyspirit.co>
 * on 2/10/15.
 */
public class GetVariableRequest<T> implements HaltedApiRequest {
    private final String name;
    private final Class<T> clazz;

    public GetVariableRequest(String name, Class<T> clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    @Override
    public void executeFrom(ApiClient apiClient) {
        apiClient.getVariable(name, clazz);
    }
}
