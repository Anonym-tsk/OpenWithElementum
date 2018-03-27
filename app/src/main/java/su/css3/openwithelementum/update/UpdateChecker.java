package su.css3.openwithelementum.update;

import android.content.Context;

import su.css3.openwithelementum.utils.PreferencesUtils;

public class UpdateChecker {

    public static void checkForDialog(Context context) {
        new CheckUpdateTask(context, CheckUpdateTask.TYPE_DIALOG).execute();
    }

    public static void checkForToast(Context context) {
        if (PreferencesUtils.isNeedCheckUpdate(context)) {
            new CheckUpdateTask(context, CheckUpdateTask.TYPE_TOAST).execute();
        }
    }
}
