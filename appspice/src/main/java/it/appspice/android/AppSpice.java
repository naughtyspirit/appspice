package it.appspice.android;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.squareup.otto.Bus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import it.appspice.android.api.models.Event;
import it.appspice.android.api.models.User;
import it.appspice.android.api.request.PostGsonRequest;
import it.appspice.android.api.response.EmptyResponseHandler;
import it.appspice.android.api.response.ErrorHandler;
import it.appspice.android.helpers.Constants;
import it.appspice.android.helpers.MetaDataHelper;
import it.appspice.android.listeners.AdvertisingIdListener;
import it.appspice.android.providers.AdvertisingIdProvider;

/**
 * Created by Naughty Spirit <hi@naughtyspirit.co>
 * on 2/6/15.
 */
public class AppSpice {
    private static final String TAG = AppSpice.class.getSimpleName();
    private static String appId;
    private static String defaultNamespace;

    private Context context;

    private static AppSpice instance;

    private static String advertisingId;

    private static RequestQueue requestQueue;

    private static Bus eventBus = new Bus();

    private AppSpice(Context context) {
        this.context = context;
    }

    private static void newInstance(Context context) {
        if (instance == null) {
            instance = new AppSpice(context);
            instance.context = context;
            requestQueue = Volley.newRequestQueue(context);
            requestQueue.stop();
            defaultNamespace = context.getPackageName();
        }
    }

    public static void onResume(Context context) {
        eventBus.register(context);
    }

    public static void onPause(Context context) {
        eventBus.unregister(context);
    }

    /**
     * Initializing the AppSpice SDK with providing AppSpiceId and AppId in the constructor.
     *
     * @param appContext Application's Context
     * @param appSpiceId Developer's appSpiceId
     * @param appId      Application's appId
     */
    public static void init(Context appContext, String appSpiceId, String appId) {
        newInstance(appContext);
        instance.initAppSpice(appSpiceId, appId);
    }

    private void initAppSpice(String appSpiceId, String appId) {

        if (TextUtils.isEmpty(appSpiceId) || TextUtils.isEmpty(appId)) {
            Log.e(TAG, "APP_SPICE_ID or APP_SPICE_APP_ID is empty.");
            return;
        }

        AppSpice.appId = appId;

        AdvertisingIdProvider.get(context, new AdvertisingIdListener() {
            @Override
            public void onAdvertisingIdReady(String advertisingId, boolean _) {
                AppSpice.advertisingId = advertisingId;
                User user = new User(advertisingId);
                requestQueue.add(new PostGsonRequest<>(Constants.API_ENDPOINT + "/users", user, Object.class, new EmptyResponseHandler<>(), new ErrorHandler(eventBus)));
                track("appSpice", "appStart");
                requestQueue.start();
            }

            @Override
            public void onAdvertisingIdError() {
            }
        });
    }

    public static void init(Context appContext) {
        newInstance(appContext);
        String appSpiceId = MetaDataHelper.getMetaData(appContext, Constants.KEY_APP_SPICE_ID);
        String appId = MetaDataHelper.getMetaData(appContext, Constants.KEY_APP_ID);
        instance.initAppSpice(appSpiceId, appId);
    }

    private static void track(Event event) {
        event.getData().put("advertisingId", advertisingId);
        requestQueue.add(new PostGsonRequest<>(Constants.API_ENDPOINT + "/events", event, Object.class, new EmptyResponseHandler<>(), new ErrorHandler(eventBus)));
    }

    public static void track(String name) {
        track(new Event(defaultNamespace, name, appId));
    }

    public static void track(String namespace, String name) {
        track(new Event(namespace, name, appId));
    }

    public static void track(String namespace, String name, Map<String, Object> data) {
        track(new Event(namespace, name, appId, data));
    }

    public static <T> void getVariableProperties(String variable, final Class<T> clazz) {
        String url = String.format(Constants.API_ENDPOINT + "/variables/%s?appId=%s&userId=%s",
                variable,
                appId, advertisingId);
        requestQueue.add(new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Gson gson = new Gson();
                    T value = gson.fromJson(response.getJSONObject("value").toString(), clazz);
                    eventBus.post(value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new ErrorHandler(eventBus)));
    }
}
