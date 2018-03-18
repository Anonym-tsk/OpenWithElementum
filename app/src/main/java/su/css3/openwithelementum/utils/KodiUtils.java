package su.css3.openwithelementum.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import java.util.List;

public class KodiUtils {

    private static String getKodiPackageName(Context context) {
        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {
            String packageName = packageInfo.packageName;
            if (packageName.startsWith("org.xbmc.kodi") || packageName.startsWith("com.semperpax.spmc")) {
                try {
                    ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
                    if (ai != null && ai.enabled) {
                        return packageName;
                    }
                } catch (PackageManager.NameNotFoundException ignored) {}
            }
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
