package com.naughtyspirit.appspice.client.client;

import android.content.Context;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;
import com.naughtyspirit.appspice.client.client.events.Event;
import com.naughtyspirit.appspice.client.client.requests.CreateUser;
import com.naughtyspirit.appspice.client.client.requests.GetAdApps;
import com.naughtyspirit.appspice.client.client.requests.UpdateCounter;
import com.naughtyspirit.appspice.client.client.responses.Response;
import com.naughtyspirit.appspice.client.helpers.Constants;
import com.naughtyspirit.appspice.client.helpers.Log;
import com.naughtyspirit.appspice.client.providers.UniqueIdProvider;
import com.squareup.otto.Bus;

import java.util.List;

/**
 * Created by NaughtySpirit
 * Created on 20/Aug/2014
 */
public class AppspiceClient {

    public static final String TAG = "client.AppspiceClient";

    private static final String APPSPICE_NAMESPACE = "appspice";

    private WebSocket webSocket;
    private Context ctx;

    private String devId;
    private String appId;
    private String userId;

    private OnAppSpiceReadyListener readyListener;

    private static Bus bus = new Bus();

    private static AppspiceClient instance;

    public AppspiceClient(Context ctx, String devId, String appId, String userId, OnAppSpiceReadyListener readyListener) {
        this.ctx = ctx;
        this.devId = devId;
        this.appId = appId;
        this.userId = userId;
        this.readyListener = readyListener;

        instance = this;

        try {
            initConnection();
        } catch (NullPointerException e) {
            Log.e("WebSocket Exception", e.getMessage());
        }

        bus.register(this);
    }

    private void initConnection() throws NullPointerException {
        AsyncHttpClient.getDefaultInstance().websocket(Constants.API_ENDPOINT, Constants.API_PROTOCOL, new AsyncHttpClient.WebSocketConnectCallback() {
            @Override
            public void onCompleted(Exception ex, WebSocket webSocket) {
                if (ex != null) {
                    Log.e(TAG, "Cannot establish a connection");
                    return;
                }

                onConnectionEstablished(webSocket);
            }
        });
    }

    private void onConnectionEstablished(WebSocket webSocket) {
        this.webSocket = webSocket;

        Log.d("websocket", "established!");

        webSocket.setStringCallback(new WebSocket.StringCallback() {
            @Override
            public void onStringAvailable(String str) {
                onStringMsgReceived(str);
            }
        });

        webSocket.setEndCallback(new CompletedCallback() {
            @Override
            public void onCompleted(Exception ex) {

            }
        });

        webSocket.setClosedCallback(new CompletedCallback() {
            @Override
            public void onCompleted(Exception ex) {

            }
        });

        readyListener.onReady();
    }

    private void send(String name, Object data) {
        try {
            if (!name.isEmpty() || data != null)
                webSocket.send(new Event(name, data).toJSON());
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    private void onStringMsgReceived(String str) {
        Log.d("data", str);

        JsonObject jsonObject = new JsonParser().parse(str).getAsJsonObject();
        JsonArray jsonArray = jsonObject.getAsJsonArray("data");
        String eventName = jsonArray.get(0).getAsString();
        JsonElement data = jsonArray.get(1);
        try {
            Class<?> resultClass = Class.forName("com.naughtyspirit.appspice.client.client.responses." + eventName + "Response");
            Response resultHandler = (Response) resultClass.newInstance();
            resultHandler.onData(data);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            // unavailable handler
        }
    }

    public void getAds() {
        send(GetAdApps.EVENT_NAME, new GetAdApps(devId, appId, userId));
    }

    public void createUser(final List<String> installedApps) {
        final UniqueIdProvider idProvider = new UniqueIdProvider(ctx, new UniqueIdProvider.OnUniqueIdAvailable() {
            @Override
            public void onUniqueId(String uniqueId) {
                userId = uniqueId;
                CreateUser createUser = new CreateUser(uniqueId, installedApps);
                send(CreateUser.EVENT_NAME, createUser);

                getAds();
            }
        });
        idProvider.requestId();
    }

    public static void sendAdImpressionEvent(String adProvicer, String adType) {
        String eventName = String.format("%s.%s.ad.impression", adProvicer, adType);
        instance.sendUpdateCounterEvent(eventName);
    }

    public static void sendAdClickEvent(String adProvicer, String adType) {
        String eventName = String.format("%s.%s.ad.click", adProvicer, adType);
        instance.sendUpdateCounterEvent(eventName);
    }

    private void sendUpdateCounterEvent(String name) {
        send(UpdateCounter.EVENT_NAME, new UpdateCounter(appId, devId, name, userId));
    }
}
