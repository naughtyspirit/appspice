package it.appspice.android.helpers;

/**
 * Created by NaughtySpirit
 * Created on 20/Aug/2014
 */
public class Log {

    public static void d(String tag, String msg) {
        // TODO: Check BUILD TYPE

        android.util.Log.d(tag, msg);
    }

    public static void e(String tag, String msg) {
        // TODO: Check BUILD TYPE

        android.util.Log.e(tag, msg);
    }
}
