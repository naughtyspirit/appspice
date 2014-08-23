package com.naughtyspirit.appspice.client.providers;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NaughtySpirit
 * Created on 23/Aug/2014
 */
public class InstalledAppsProvider {

    public static List<String> installedApps(PackageManager packageManager) {
        List<ApplicationInfo> packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        List<String> installedPackages = new ArrayList<String>();
        for (ApplicationInfo applicationInfo : packages) {
            if(!isSystemPackage(applicationInfo)) {
                installedPackages.add(applicationInfo.packageName);
            }
        }
        return installedPackages;
    }

    private static boolean isSystemPackage(ApplicationInfo applicationInfo) {
        return ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }
}
