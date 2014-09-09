package it.appspice.android.client.responses;

import com.google.gson.Gson;

/**
 * Created by NaughtySpirit
 * Created on 20/Aug/2014
 */
public abstract class BaseResponse implements Response {

    protected Gson gson = new Gson();

    protected void publish(Object event) {
        //AdSpice.publish(event);
    }
}

