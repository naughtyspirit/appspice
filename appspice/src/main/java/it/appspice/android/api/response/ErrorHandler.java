package it.appspice.android.api.response;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.squareup.otto.Bus;

/**
 * Created by Naughty Spirit <hi@naughtyspirit.co>
 * on 2/9/15.
 */
public class ErrorHandler implements Response.ErrorListener {

    private final Bus eventBus;

    public ErrorHandler(Bus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        eventBus.post(error);
    }
}
