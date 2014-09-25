package it.appspice.android.helpers;

import android.content.Context;
import android.util.Log;

import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;

import java.util.ArrayList;

/**
 * Created by NaughtySpirit
 * Created on 20/Aug/2014
 */
public class ConnectionManager {

    private static final String TAG = ConnectionManager.class.getSimpleName();

    private Context context;

    private static ConnectionManager instance;
    private WebSocket webSocket;
    private OnMsgReceiveListener msgListener;

    private String endPoint;
    private String protocol;

    private ArrayList<String> buffer = new ArrayList<String>();

    public interface OnMsgReceiveListener {
        public void onReceive(String str);
    }

    public ConnectionManager(Context context, final OnMsgReceiveListener listener) {
        this.endPoint = Constants.API_ENDPOINT;
        this.protocol = Constants.API_PROTOCOL;
        this.msgListener = listener;

        this.context = context;
    }

    public ConnectionManager(Context context, String endPointAddr, String protocol, final OnMsgReceiveListener listener) {
        this.endPoint = endPointAddr;
        this.protocol = protocol;
        this.msgListener = listener;

        this.context = context;
    }

    private void connect() {
        AsyncHttpClient.getDefaultInstance().websocket(endPoint, protocol, new AsyncHttpClient.WebSocketConnectCallback() {
            @Override
            public void onCompleted(Exception e, WebSocket ws) {

                if (e != null || ws == null) {
                    Log.e(TAG, String.valueOf(e) + (ws == null ? " No internet access or the socket is null" : ""));
                    return;
                }

                ws.setStringCallback(new WebSocket.StringCallback() {
                    @Override
                    public void onStringAvailable(String str) {
                        msgListener.onReceive(str);
                    }
                });

                ws.setClosedCallback(new CompletedCallback() {
                    @Override
                    public void onCompleted(Exception e) {
                        // TODO: Retry to connect
                    }
                });

                ws.setEndCallback(new CompletedCallback() {
                    @Override
                    public void onCompleted(Exception e) {
                        // TODO: Retry to connect
                    }
                });

                webSocket = ws;

                if (buffer.size() > 0) {
                    for (String str : buffer) {
                        sendToServer(str);
                    }
                    buffer.clear();
                }
            }
        });
    }

    public void send(String data) {
        if (webSocket == null || !webSocket.isOpen()) {
            buffer.add(data);
            connect();
            return;
        }

        sendToServer(data);
    }

    private void sendToServer(String data) {
        try {
            webSocket.send(data);
        } catch (Exception e) {
            Log.e(TAG, String.valueOf(e.getMessage()));
        }
    }

    public void close() {
        if (webSocket != null && webSocket.isOpen()) {
            webSocket.close();
            webSocket = null;
            buffer.clear();
        }
    }

    public boolean isWebSocketOpen() {
        return webSocket != null && webSocket.isOpen();
    }
}
