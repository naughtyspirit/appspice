package it.appspice.android.api;

import it.appspice.android.helpers.Log;
import retrofit.RetrofitError;

/**
 * Created by nmp on 1/14/15.
 */
public abstract class Callback<T> implements retrofit.Callback<T> {
    @Override
    public void failure(RetrofitError error) {
        Log.d("API_ERROR", error.toString());
    }
}
