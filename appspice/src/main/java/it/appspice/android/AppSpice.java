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

    private final ApiClient apiClient;
    private Context context;
    private static AppSpice instance;

    private static String appNamespace;

    private final Bus eventBus = new Bus();

    private AppSpice(Context context, String appId) {
        if (TextUtils.isEmpty(appId)) {
            throw new IllegalArgumentException("AppSpice is feeling lonely without it's app id");
        }
        apiClient = new ApiClient(context, eventBus, appId);
        this.context = context;
        appNamespace = context.getPackageName();
        requestAdvertisingId();
    }

    private static AppSpice getInstance(Context context, String appId) {
        if (instance == null) {
            instance = new AppSpice(context, appId);
        }
        return instance;
    }

    public static void onStart(Activity activity) {
        String activityName = activity.getClass().getSimpleName();
        track(createEvent("activityStart").with("activity", activityName));
    }

    public static void onStop(Activity activity) {
        String activityName = activity.getClass().getSimpleName();
        track(createEvent("activityStop").with("activity", activityName));
    }

    public static Event createEvent(String name) {
        return new Event(appNamespace, name);
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
            track(createEvent("fragmentStart").with("fragment", fragmentName));
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
     * Initializing the AppSpice SDK with appId
     *
     * @param appContext Application's Context
     * @param appId      Application's appId
     */
    public static void init(Context appContext, String appId) {
        instance = getInstance(appContext, appId);
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

    /**
     * Initializing the AppSpice SDK with appId from the AndroidManifest
     *
     * @param appContext Application's Context
     */
    public static void init(Context appContext) {
        String appId = MetaDataHelper.getMetaData(appContext, Constants.KEY_APP_SPICE_APP_ID);
        instance = getInstance(appContext, appId);
    }

    public static void track(Event event) {
        instance.apiClient.createEvent(event);
    }

    public static void track(String name) {
        track(new Event(appNamespace, name));
    }

    public static void track(String namespace, String name) {
        track(new Event(namespace, name));
    }

    public static void track(String namespace, String name, Map<String, Object> data) {
        track(new Event(namespace, name, data));
    }

    public static <T> void requestVariable(final String name, final Class<T> clazz) {
        instance.apiClient.getVariable(name, clazz);
    }
}
