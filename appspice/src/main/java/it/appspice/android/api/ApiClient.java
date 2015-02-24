package it.appspice.android.api;

import android.content.Context;
import android.content.pm.PackageManager;

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
import it.appspice.android.api.errors.VersionNameNotFoundError;
import it.appspice.android.api.models.Event;
import it.appspice.android.api.models.User;
import it.appspice.android.api.request.GetVariableRequest;
import it.appspice.android.api.request.HaltedApiRequest;
import it.appspice.android.api.request.PostEventRequest;
import it.appspice.android.api.request.volley.PostGsonRequest;
import it.appspice.android.api.response.EmptyResponseHandler;
import it.appspice.android.api.response.ErrorHandler;
import it.appspice.android.helpers.Constants;

/**
 * Created by Naughty Spirit <hi@naughtyspirit.co>
 * on 2/10/15.
 */
public class ApiClient {

    private static RequestQueue requestQueue;
    private Context context;
    private final Bus eventBus;
    private final String appId;
    private String advertisingId;

    private final Queue<HaltedApiRequest> apiRequests = new LinkedList<>();
    private boolean isStarted = false;

    public ApiClient(Context context, Bus eventBus, String appId) {
        this.context = context;
        this.eventBus = eventBus;
        this.appId = appId;
        requestQueue = Volley.newRequestQueue(context);
        requestQueue.stop();
        isStarted = false;
    }

    public void createUser(User user) {
        String url = String.format(Constants.API_ENDPOINT + "/users?appId=%s", appId);
        requestQueue.add(new PostGsonRequest<>(url, user, Object.class, new EmptyResponseHandler<>(), new ErrorHandler(eventBus)));
    }

    public void createEvent(Event event) {
        if (isStarted) {
            event.with("userId", advertisingId);
            String versionName = null;
            try {
                versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                eventBus.post(new VersionNameNotFoundError("Application version name can not be found from the context", e));
            }
            event.with("version", versionName);
            String url = String.format(Constants.API_ENDPOINT + "/events?appId=%s", appId);
            requestQueue.add(new PostGsonRequest<>(url, event, Object.class, new EmptyResponseHandler<>(), new ErrorHandler(eventBus)));
        } else {
            apiRequests.add(new PostEventRequest(event));
        }
    }

    public <T> void getVariable(final String name, final Class<T> clazz) {
        if (isStarted) {
            String url = String.format(Constants.API_ENDPOINT + "/variables/%s?userId=%s&appId=%s", name, advertisingId, appId);
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
            apiRequests.add(new GetVariableRequest<T>(name, clazz));
        }
    }

    public void start() {
        isStarted = true;
        for (HaltedApiRequest request : apiRequests) {
            request.executeFrom(this);
        }
        requestQueue.start();
    }

    public void setAdvertisingId(String advertisingId) {
        this.advertisingId = advertisingId;
    }
}
