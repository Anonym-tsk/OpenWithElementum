package su.css3.openwithelementum.update;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.text.Html;

import su.css3.openwithelementum.R;

class UpdateDialog {

    static void show(final Context context, String name, String content, final String downloadUrl) {
        if (isContextValid(context)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(Html.fromHtml(name));
            builder.setMessage(Html.fromHtml(content));
            builder.setPositiveButton(R.string.update_dialog_btn_download, (dialog, id) -> goToDownload(context, downloadUrl));
            builder.setNegativeButton(R.string.update_dialog_btn_cancel, (dialog, id) -> {});

            AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    }

    private static boolean isContextValid(Context context) {
        return context instanceof Activity && !((Activity) context).isFinishing();
    }

    private static void goToDownload(Context context, String downloadUrl) {
        Intent intent = new Intent(context.getApplicationContext(), DownloadService.class);
        intent.putExtra(DownloadService.PARAM_DOWNLOAD_URL, downloadUrl);
        context.startService(intent);
    }
}
