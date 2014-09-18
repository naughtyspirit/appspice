package it.appspice.android.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

    private ArrayList<String> buffer = new ArrayList<String>();

    public interface OnMsgReceiveListener {
        public void onReceive(String str);
    }

    public static ConnectionManager getInstance(Context context) {
        if (instance == null) {
            instance = new ConnectionManager(context);
        }

        return instance;
    }

    private ConnectionManager(Context context) {
        this.context = context;
    }

    public void init(String endPointAddr, String protocol, final OnMsgReceiveListener listener) {
        if (!isInternetEnabled(context)) {
            return;
        }

        AsyncHttpClient.getDefaultInstance().websocket(endPointAddr, protocol, new AsyncHttpClient.WebSocketConnectCallback() {
            @Override
            public void onCompleted(Exception e, WebSocket ws) {
                if (e != null) {
                    Log.e(TAG, String.valueOf(e));
                    return;
                }

                webSocket = ws;
                msgListener = listener;

                webSocket.setStringCallback(new WebSocket.StringCallback() {
                    @Override
                    public void onStringAvailable(String str) {
                        msgListener.onReceive(str);
                    }
                });

                webSocket.setClosedCallback(new CompletedCallback() {
                    @Override
                    public void onCompleted(Exception e) {
                        // TODO: Retry to connect
                        Log.e(TAG, "Closed...");
                    }
                });

                webSocket.setEndCallback(new CompletedCallback() {
                    @Override
                    public void onCompleted(Exception e) {
                        // TODO: Retry to connect
                        Log.e(TAG, "End...");
                    }
                });

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
        if (webSocket.isOpen()) {
            webSocket.close();
            buffer.clear();
        }
    }

    public boolean isWebSocketOpen() {
        return webSocket != null && webSocket.isOpen();
    }

    private static boolean isInternetEnabled(Context context) {
        try {
            ConnectivityManager connection = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo wifi = connection.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobile = connection.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            boolean isEnabled = wifi.isAvailable() && wifi.isConnected() || mobile.isAvailable() && mobile.isConnected();

            if (!isEnabled) {
                Log.e(TAG, "No internet connection available.");
                return false;
            }

            return true;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }
}
