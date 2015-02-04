package it.appspice.android;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

import it.appspice.android.api.AppSpiceApiClient;
import it.appspice.android.api.Callback;
import it.appspice.android.api.EmptyCallback;
import it.appspice.android.api.models.Event;
import it.appspice.android.api.models.User;
import it.appspice.android.api.models.Variable;
import it.appspice.android.api.models.VariableProperties;
import it.appspice.android.helpers.Constants;
import it.appspice.android.helpers.MetaDataHelper;
import retrofit.client.Response;

/**
 * Created by nmp on 12/11/14.
 */
public class AppSpice {
    private static final String TAG = AppSpice.class.getSimpleName();

    private Context context;

    private static AppSpice instance;

    private AppSpice(Context context) {
        this.context = context;
    }

    private static void newInstance(Context context) {
        if (instance == null) {
            instance = new AppSpice(context);
        }
        instance.context = context;
    }

    /**
     * Initializing the AppSpice SDK with providing AppSpiceId and AppId in the constructor.
     *
     * @param appContext Application's Context
     * @param appSpiceId Developer's appSpiceId
     * @param appId      Application's appId
     */
    public static void init(Context appContext, String appSpiceId, String appId) {
        newInstance(appContext);

        instance.initAppSpice(appSpiceId, appId);
    }

    private void initAppSpice(String appSpiceId, String appId) {

        if (TextUtils.isEmpty(appSpiceId) || TextUtils.isEmpty(appId)) {
            Log.e(TAG, "APP_SPICE_ID or APP_SPICE_APP_ID is empty.");
            return;
        }

        AppSpiceApiClient.getClient().createUser(new User("test"), new EmptyCallback<Response>());

        HashMap<String, Object> data = new HashMap<>();
        data.put("userId", "test");

        AppSpiceApiClient.getClient().createEvent(new Event("appSpice", "appStart", data), new EmptyCallback<Response>());
    }

    public static void init(Context appContext) {
        newInstance(appContext);

        String appSpiceId = MetaDataHelper.getMetaData(appContext, Constants.KEY_APP_SPICE_ID);
        String appId = MetaDataHelper.getMetaData(appContext, Constants.KEY_APP_ID);

        instance.initAppSpice(appSpiceId, appId);
    }

    public static void track(Event event) {
        AppSpiceApiClient.getClient().createEvent(event, new EmptyCallback<Response>());
    }

    public static void track(String namespace, String name) {
        AppSpiceApiClient.getClient().createEvent(new Event(namespace, name), new EmptyCallback<Response>());
    }

    public static void getVariable(String variable) {
        AppSpiceApiClient.getClient().getVariable(variable, "54cf8542bb98aa9a7ae778ba", new Callback<Variable>() {
            @Override
            public void success(Variable variable, Response response) {
                VariableProperties variableProperties = VariableProperties.fromVariable(variable);
                Log.d("Response", variableProperties.get("color") + "");
            }
        });
    }
}
