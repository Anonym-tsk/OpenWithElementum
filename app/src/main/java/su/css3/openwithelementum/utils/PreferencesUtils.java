package su.css3.openwithelementum.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferencesUtils {
    public static final String KEY_PREF_KODI_TIMEOUT = "timeout";
    public static final String KEY_PREF_KODI_HOST = "host";
    public static final String KEY_PREF_KODI_APP = "application";
    public static final String KEY_PREF_UPDATE = "update";
    public static final String KEY_PREF_LAST_UPDATE = "lastupdate";

    private static final String LOCALHOST = "127.0.0.1";
    private static final long UPDATE_INTERVAL = 24 * 60 * 60 * 1000;

    public static int getTimeout(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            return Integer.parseInt(sharedPreferences.getString(KEY_PREF_KODI_TIMEOUT, "10"));
        } catch (NumberFormatException e) {
            return 10;
        }
    }

    public static String getHost(Context context) {
        String kodiPackageName = getKodiPackageName(context);
        if (kodiPackageName != null) {
            return LOCALHOST;
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(KEY_PREF_KODI_HOST, LOCALHOST);
    }

    public static String getKodiPackageName(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String packageName = sharedPreferences.getString(KEY_PREF_KODI_APP, "");

        if (!packageName.isEmpty() && AppUtils.isAppInstalled(context, packageName)) {
            return packageName;
        }
        return null;
    }

    public static boolean isNeedCheckUpdate(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return System.currentTimeMillis() - sharedPreferences.getLong(KEY_PREF_LAST_UPDATE, 0) > UPDATE_INTERVAL;
    }

    public static void setLastUpdateTime(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putLong(KEY_PREF_LAST_UPDATE, System.currentTimeMillis()).apply();
    }
}
