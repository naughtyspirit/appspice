package it.appspice.android.api.request.volley;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;

/**
 * Created by Naughty Spirit <hi@naughtyspirit.co>
 * on 2/9/15.
 */
public class PostGsonRequest<T> extends JsonRequest<T> {

    private final Gson gson = new Gson();
    private final Class<T> responseClass;

    public PostGsonRequest(String url, Object request, Class<T> responseClass, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(Request.Method.POST, url, new Gson().toJson(request), listener, errorListener);
        this.responseClass = responseClass;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString =
                    new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(gson.fromJson(jsonString, responseClass),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }
}
