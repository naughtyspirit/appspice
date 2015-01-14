package it.appspice.android.api;

import it.appspice.android.api.models.User;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by nmp on 1/14/15.
 */
public interface AppSpiceApi {
    @POST("/users")
    void createUser(@Body User user, Callback<Response> cb);
}
