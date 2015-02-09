package it.appspice.android.api.response;

import android.util.Log;

import com.android.volley.Response;

/**
 * Created by Naughty Spirit <hi@naughtyspirit.co>
 * on 2/9/15.
 */
public class EmptyResponseHandler<T> implements Response.Listener<T> {

    @Override
    public void onResponse(T response) {
        Log.d("AppSpice", response.toString());
    }
}
