package com.naughtyspirit.appspice.client.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;
import com.naughtyspirit.appspice.client.client.events.Event;
import com.naughtyspirit.appspice.client.client.requests.GetAds;
import com.naughtyspirit.appspice.client.client.responses.Response;
import com.naughtyspirit.appspice.client.helpers.Constants;
import com.naughtyspirit.appspice.client.helpers.Log;

/**
 * Created by NaughtySpirit
 * Created on 20/Aug/2014
 */
public class AppspiceClient {

    public static final String TAG = "client.AppspiceClient";

    private WebSocket webSocket;

    private String devId;
    private String appId;

    public AppspiceClient(String devId, String appId) {
        this.devId = devId;
        this.appId = appId;

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
        send(GetAds.EVENT_NAME, new GetAds(devId, appId));
    }
}
