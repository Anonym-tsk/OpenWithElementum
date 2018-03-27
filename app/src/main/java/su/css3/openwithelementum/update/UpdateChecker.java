package su.css3.openwithelementum.update;

import android.content.Context;

public class UpdateChecker {

    public static void checkForDialog(Context context) {
        new CheckUpdateTask(context, CheckUpdateTask.TYPE_DIALOG).execute();
    }

    public static void checkForToast(Context context) {
        new CheckUpdateTask(context, CheckUpdateTask.TYPE_TOAST).execute();
    }
}
