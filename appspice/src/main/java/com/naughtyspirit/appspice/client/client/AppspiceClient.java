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
import com.naughtyspirit.appspice.client.client.responses.Response;
import com.naughtyspirit.appspice.client.helpers.Constants;
import com.naughtyspirit.appspice.client.helpers.Log;
import com.naughtyspirit.appspice.client.providers.InstalledAppsProvider;

import java.util.List;

/**
 * Created by NaughtySpirit
 * Created on 20/Aug/2014
 */
public class AppspiceClient {

    public static final String TAG = "client.AppspiceClient";

    private WebSocket webSocket;

    private String devId;
    private String appId;
    private String userId;

    private Context ctx;

    public AppspiceClient(Context ctx, String devId, String appId, String userId) {
        this.ctx = ctx;
        this.devId = devId;
        this.appId = appId;
        this.userId = userId;

        try {
            initConnection();
        } catch (NullPointerException e) {
            Log.e("WebSocket Exception", e.getMessage());
        }
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

        createUser(InstalledAppsProvider.installedApps(ctx.getPackageManager()));
        getAds();

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
    }

    private void send(String name, Object data) {
        webSocket.send(new Event(name, data).toJSON());
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

    public void createUser(List<String> installedApps) {
        CreateUser createUser = new CreateUser(userId, installedApps);
        send(CreateUser.EVENT_NAME, createUser);
    }
}
