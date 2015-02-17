package it.appspice.android.api.request;

import it.appspice.android.api.ApiClient;
import it.appspice.android.api.models.Event;

/**
 * Created by Naughty Spirit <hi@naughtyspirit.co>
 * on 2/10/15.
 */
public class PostEventRequest implements HaltedApiRequest {

    private final Event event;

    public PostEventRequest(Event event) {
        this.event = event;
    }

    @Override
    public void executeFrom(ApiClient apiClient) {
        apiClient.createEvent(event);
    }
}
