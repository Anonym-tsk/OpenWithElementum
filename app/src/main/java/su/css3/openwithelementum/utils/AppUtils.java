package su.css3.openwithelementum.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import su.css3.openwithelementum.system.WebService;

public class AppUtils {

    public static List<CharSequence[]> getKodiPackages(Context context) {
        PackageManager packageManager = context.getPackageManager();
        List<ApplicationInfo> packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        List<CharSequence[]> result = new ArrayList<>();

        for (ApplicationInfo packageInfo : packages) {
            String packageName = packageInfo.packageName;
            if (packageInfo.enabled && (packageName.startsWith("org.xbmc.kodi") || packageName.startsWith("com.semperpax.spmc"))) {
                CharSequence[] item = {packageName, packageInfo.loadLabel(packageManager)};
                result.add(item);
            }
        }

        return result;
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
            if (ai != null && ai.enabled) {
                return true;
            }
        } catch (PackageManager.NameNotFoundException ignored) {}
        return false;
    }

    public static void activateApp(Context context, String packageName) {
        context.startActivity(context.getPackageManager().getLaunchIntentForPackage(packageName));
    }

    public static void showMessage(Context context, int stringId) {
        Toast.makeText(context, context.getString(stringId), Toast.LENGTH_LONG).show();
    }

    public static void playMagnet(Context context, String magnetUrl, WebService.ResponseListener listener) {
        final String host = PreferencesUtils.getHost(context);
        final int maxAttempts = PreferencesUtils.getTimeout(context);

        try {
            String link = "http://" + host + ":65220/playuri?uri=" + URLEncoder.encode(magnetUrl, "UTF-8");
            new WebService(link, listener, maxAttempts).execute();
        } catch (UnsupportedEncodingException e) {
            listener.onReady(false);
        }
    }
}
