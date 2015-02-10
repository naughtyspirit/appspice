package it.appspice.android.api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.squareup.otto.Bus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.Queue;

import it.appspice.android.api.errors.AppSpiceError;
import it.appspice.android.api.models.Event;
import it.appspice.android.api.models.User;
import it.appspice.android.api.request.GetVariableRequest;
import it.appspice.android.api.request.HaltedApiRequest;
import it.appspice.android.api.request.PostEventRequest;
import it.appspice.android.api.request.RequestParameterProvider;
import it.appspice.android.api.request.volley.PostGsonRequest;
import it.appspice.android.api.response.EmptyResponseHandler;
import it.appspice.android.api.response.ErrorHandler;
import it.appspice.android.helpers.Constants;

/**
 * Created by Naughty Spirit <hi@naughtyspirit.co>
 * on 2/10/15.
 */
public class ApiClient implements RequestParameterProvider {

    private static RequestQueue requestQueue;
    private final Bus eventBus;
    private final String appId;
    private String advertisingId;

    private final Queue<HaltedApiRequest> apiRequests = new LinkedList<>();
    private boolean isStarted = false;

    public ApiClient(Context context, Bus eventBus, String appId) {
        this.eventBus = eventBus;
        this.appId = appId;
        requestQueue = Volley.newRequestQueue(context);
        requestQueue.stop();
        isStarted = false;
    }

    public void createUser(User user) {
        requestQueue.add(new PostGsonRequest<>(Constants.API_ENDPOINT + "/users", user, Object.class, new EmptyResponseHandler<>(), new ErrorHandler(eventBus)));
    }

    public void createEvent(Event event) {
        if (isStarted) {
            requestQueue.add(new PostGsonRequest<>(Constants.API_ENDPOINT + "/events", event, Object.class, new EmptyResponseHandler<>(), new ErrorHandler(eventBus)));
        } else {
            apiRequests.add(new PostEventRequest(event));
        }
    }

    public <T> void getVariable(final String name, String appId, final Class<T> clazz) {
        if (isStarted) {
            String url = String.format(Constants.API_ENDPOINT + "/variables/%s?appId=%s&userId=%s", name, appId, advertisingId);
            requestQueue.add(new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Gson gson = new Gson();
                        T value = gson.fromJson(response.getJSONObject("value").toString(), clazz);
                        eventBus.post(value);
                    } catch (JSONException e) {
                        eventBus.post(new AppSpiceError(String.format("Unable to parse variable %s", name), e));
                    }
                }
            }, new ErrorHandler(eventBus)));
        } else {
            apiRequests.add(new GetVariableRequest<T>(name, appId, clazz));
        }
    }

    public void start() {
        isStarted = true;
        for (HaltedApiRequest request : apiRequests) {
            request.populateFrom(this);
            request.executeFrom(this);
        }
        requestQueue.start();
    }

    @Override
    public String getAdvertisingId() {
        return advertisingId;
    }

    public void setAdvertisingId(String advertisingId) {
        this.advertisingId = advertisingId;
    }

    @Override
    public String getAppId() {
        return appId;
    }
}
