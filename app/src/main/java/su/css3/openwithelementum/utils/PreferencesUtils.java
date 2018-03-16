package su.css3.openwithelementum.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferencesUtils {
    public static final String KEY_PREF_KODI_TIMEOUT = "timeout";
    public static final String KEY_PREF_KODI_LOCALLY = "locally";
    public static final String KEY_PREF_KODI_HOST = "host";

    public static final String LOCALHOST = "127.0.0.1";

    public static boolean isLocally(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(KEY_PREF_KODI_LOCALLY, true);
    }

    public static int getTimeout(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            return Integer.parseInt(sharedPreferences.getString(KEY_PREF_KODI_TIMEOUT, "10"));
        } catch (NumberFormatException e) {
            return 10;
        }
    }

    public static String getHost(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (sharedPreferences.getBoolean(KEY_PREF_KODI_LOCALLY, true)) {
            return LOCALHOST;
        }
        return sharedPreferences.getString(KEY_PREF_KODI_HOST, LOCALHOST);
    }
}
