package com.naughtyspirit.appspice.client.client;

import android.util.Log;

import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;
import com.naughtyspirit.appspice.client.helpers.Constants;

/**
 * Author: Atanas Dimitrov <seishin90@yandex.ru>
 * Created on: 19/Aug/2014
 */
public class AppspiceClient {

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
                    return;
                }

                onConnectionEstablished(webSocket);
            }
        });
    }

    private void onConnectionEstablished(WebSocket webSocket) {
        this.webSocket = webSocket;

        webSocket.setStringCallback(new WebSocket.StringCallback() {
            @Override
            public void onStringAvailable(String str) {

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
}
