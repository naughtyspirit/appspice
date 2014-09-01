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

    private static AppspiceClient instance;

    public AppspiceClient(Context ctx, String devId, String appId, String userId, OnAppSpiceReadyListener readyListener) {
        this.ctx = ctx;
        this.devId = devId;
        this.appId = appId;
        this.userId = userId;
        this.readyListener = readyListener;

        instance = this;

        initConnection();
    }

    private void initConnection() {
        try {
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
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    private void onConnectionEstablished(WebSocket webSocket) {
        this.webSocket = webSocket;

        Log.e("websocket", String.valueOf(webSocket.isOpen()));

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
            webSocket.send(new Event(name, data).toJSON());
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    private void onStringMsgReceived(String str) {
        Log.e("data", str);

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
        Log.e(TAG, String.valueOf(installedApps.size()));
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
