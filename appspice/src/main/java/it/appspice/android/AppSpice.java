package it.appspice.android;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;

import com.squareup.otto.Bus;

import java.util.Locale;
import java.util.Map;

import it.appspice.android.api.ApiClient;
import it.appspice.android.api.errors.AppSpiceError;
import it.appspice.android.api.models.Device;
import it.appspice.android.api.models.Event;
import it.appspice.android.api.models.User;
import it.appspice.android.helpers.Constants;
import it.appspice.android.helpers.MetaDataHelper;
import it.appspice.android.listeners.AdvertisingIdListener;
import it.appspice.android.providers.AdvertisingIdProvider;

/**
 * Created by Naughty Spirit <hi@naughtyspirit.co>
 * on 2/6/15.
 */
public class AppSpice {
    private static final String TAG = AppSpice.class.getSimpleName();

    private final ApiClient apiClient;
    private Context context;
    private static AppSpice instance;

    private static String appId;
    private static String appSpiceId;
    private static String appNamespace;

    private final Bus eventBus = new Bus();


    private AppSpice(Context context, String appSpiceId, String appId) {
        if (TextUtils.isEmpty(appSpiceId)) {
            throw new IllegalArgumentException("AppSpice is feeling lost without it's ID");
        }
        if (TextUtils.isEmpty(appId)) {
            throw new IllegalArgumentException("AppSpice is feeling lonely without APP ID");
        }
        apiClient = new ApiClient(context, eventBus, appId);
        this.context = context;
        appNamespace = context.getPackageName();

        AppSpice.appSpiceId = appSpiceId;
        AppSpice.appId = appId;
        requestAdvertisingId();
    }

    private static AppSpice getInstance(Context context, String appSpiceId, String appId) {
        if (instance == null) {
            instance = new AppSpice(context, appSpiceId, appId);
        }
        return instance;
    }

    public static void onStart(Activity activity) {
        String activityName = activity.getClass().getSimpleName();
        track(createEvent("activityStart").put("activity", activityName));
    }

    public static void onStop(Activity activity) {
        String activityName = activity.getClass().getSimpleName();
        track(createEvent("activityStop").put("activity", activityName));
    }

    private static Event createEvent(String name) {
        return new Event(appNamespace, name, appId);
    }

    static class OnBackStackChangeListener implements FragmentManager.OnBackStackChangedListener {

        private final Activity activity;

        OnBackStackChangeListener(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onBackStackChanged() {
            FragmentActivity fragmentActivity = (FragmentActivity) activity;
            FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
            FragmentManager.BackStackEntry backStackEntry = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1);
            String tag = backStackEntry.getName();
            String fragmentName = fragmentManager.findFragmentByTag(tag).getClass().getSimpleName();
            track(createEvent("fragmentStart").put("fragment", fragmentName));
        }
    }

    private FragmentManager.OnBackStackChangedListener onBackStackChangedListener;

    public static void onResume(Activity activity) {
        if (activity instanceof FragmentActivity) {
            instance.onBackStackChangedListener = new OnBackStackChangeListener(activity);
        }
        instance.eventBus.register(activity);
    }

    public static void onPause(Activity activity) {
        instance.eventBus.unregister(activity);
        if (activity instanceof FragmentActivity) {
            FragmentActivity fragmentActivity = (FragmentActivity) activity;
            fragmentActivity.getSupportFragmentManager().removeOnBackStackChangedListener(instance.onBackStackChangedListener);
        }
    }

    /**
     * Initializing the AppSpice SDK with providing AppSpiceId and AppId in the constructor.
     *
     * @param appContext Application's Context
     * @param appSpiceId Developer's appSpiceId
     * @param appId      Application's appId
     */
    public static void init(Context appContext, String appSpiceId, String appId) {
        instance = getInstance(appContext, appSpiceId, appId);
    }

    private void requestAdvertisingId() {

        AdvertisingIdProvider.get(context, new AdvertisingIdListener() {
            @Override
            public void onAdvertisingIdReady(String advertisingId, boolean _) {
                apiClient.setAdvertisingId(advertisingId);
                User user = new User(advertisingId, Locale.getDefault().getCountry(), Locale.getDefault().getLanguage(), new Device("Android", Build.VERSION.RELEASE, Build.MODEL));
                apiClient.createUser(user);
                track("appStart");
                apiClient.start();
            }

            @Override
            public void onAdvertisingIdError(Throwable error) {
                eventBus.post(new AppSpiceError("Unable to get Advertising Id", error));
            }
        });
    }

    public static void init(Context appContext) {
        String appSpiceId = MetaDataHelper.getMetaData(appContext, Constants.KEY_APP_SPICE_ID);
        String appId = MetaDataHelper.getMetaData(appContext, Constants.KEY_APP_ID);
        instance = getInstance(appContext, appSpiceId, appId);
    }

    private static void track(Event event) {
        instance.apiClient.createEvent(event);
    }

    public static void track(String name) {
        track(new Event(appNamespace, name, appId));
    }

    public static void track(String namespace, String name) {
        track(new Event(namespace, name, appId));
    }

    public static void track(String namespace, String name, Map<String, Object> data) {
        track(new Event(namespace, name, appId, data));
    }

    public static <T> void getVariable(final String name, final Class<T> clazz) {
        instance.apiClient.getVariable(name, appId, clazz);
    }
}
