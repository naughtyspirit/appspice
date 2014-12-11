package it.appspice.android.providers;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

import it.appspice.android.helpers.Log;
import it.appspice.android.services.InstalledAppsService;

/**
 * Created by NaughtySpirit
 * Created on 23/Aug/2014
 */
public class InstalledPackagesProvider {

    private static final String TAG = InstalledAppsService.class.getSimpleName();

    public static List<String> installedPackages(PackageManager packageManager) {
        List<String> installedPackages = new ArrayList<String>();

        try {
            List<ApplicationInfo> packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

            for (ApplicationInfo applicationInfo : packages) {
                if (!isSystemPackage(applicationInfo)) {
                    installedPackages.add(applicationInfo.packageName);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        return installedPackages;
    }

    private static boolean isSystemPackage(ApplicationInfo applicationInfo) {
        return ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }
}
