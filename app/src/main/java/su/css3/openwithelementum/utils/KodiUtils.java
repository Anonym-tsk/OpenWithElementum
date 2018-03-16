package su.css3.openwithelementum.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class KodiUtils {

    private final static Set<String> KODI_PACKAGE_NAMES = new HashSet<>(Arrays.asList(
            "org.xbmc.kodi",
            "com.semperpax.spmc16",
            "com.semperpax.spmc"
    ));

    private static String getKodiPackageName(Context context) {
        PackageManager pm = context.getPackageManager();
        for (String packageName : KODI_PACKAGE_NAMES) {
            try {
                ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
                if (ai != null && ai.enabled) {
                    return packageName;
                }
            } catch (PackageManager.NameNotFoundException ignored) {}
        }
        return null;
    }

    public static boolean activateKodi(Context context) {
        String packageName = getKodiPackageName(context);
        if (packageName != null) {
            context.startActivity(context.getPackageManager().getLaunchIntentForPackage(packageName));
            return true;
        }
        return false;
    }
}
