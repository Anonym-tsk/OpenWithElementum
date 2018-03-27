package su.css3.openwithelementum.update;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import su.css3.openwithelementum.PreferencesActivity;
import su.css3.openwithelementum.R;

public class DownloadService extends IntentService {
    public static final String PARAM_DOWNLOAD_URL = "url";

    private static final int BUFFER_SIZE = 10 * 1024; // 8k ~ 32K
    private static final String TAG = "DownloadService";

    private static final int NOTIFICATION_ID = 0;

    private NotificationManager mNotifyManager;
    private Builder mBuilder;

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationUtils notificationUtils = new NotificationUtils(this);
        Intent resultIntent = new Intent(this, PreferencesActivity.class);
        PendingIntent pendingintent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder = notificationUtils.createBuilder();
        mBuilder.setContentTitle(getString(R.string.update_download_title))
                .setSmallIcon(android.R.drawable.stat_sys_download)
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_LOW)
                .setContentIntent(pendingintent);
        updateProgress(0);

        String urlStr = intent.getStringExtra(PARAM_DOWNLOAD_URL);
        InputStream inputStream = null;
        FileOutputStream outputStream = null;

        try {
            URL url = new URL(urlStr);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(false);
            urlConnection.setConnectTimeout(10 * 1000);
            urlConnection.setReadTimeout(10 * 1000);
            urlConnection.setRequestProperty("Connection", "Keep-Alive");
            urlConnection.setRequestProperty("Charset", "UTF-8");
            urlConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
            urlConnection.connect();

            long bytetotal = urlConnection.getContentLength();
            long bytesum = 0;
            int byteread = 0;
            int oldProgress = 0;

            inputStream = urlConnection.getInputStream();
            File dir = getCacheDir();
            String apkName = urlStr.substring(urlStr.lastIndexOf("/") + 1, urlStr.length());
            File apkFile = new File(dir, apkName);
            outputStream = new FileOutputStream(apkFile);
            byte[] buffer = new byte[BUFFER_SIZE];

            while ((byteread = inputStream.read(buffer)) != -1) {
                bytesum += byteread;
                outputStream.write(buffer, 0, byteread);

                int progress = (int) (bytesum * 100L / bytetotal);
                if (progress != oldProgress) {
                    updateProgress(progress);
                }
                oldProgress = progress;
            }

            installAPk(this, apkFile);
            mNotifyManager.cancel(NOTIFICATION_ID);
        } catch (Exception e) {
            Log.e(TAG, "download apk file error:" + e.getMessage());
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException ignored) {}
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignored) {}
            }
        }
    }

    private void updateProgress(int progress) {
        mBuilder.setContentText(progress + "%")
                .setProgress(100, progress, false);
        mNotifyManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private void installAPk(Context context, File apkFile) {
        Intent installAPKIntent = getApkInStallIntent(context, apkFile);
        startActivity(installAPKIntent);
    }

    private Intent getApkInStallIntent(Context context, File apkFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Uri uri;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".update.provider", apkFile);
        } else {
            try {
                String[] command = {"chmod", "777", apkFile.toString()};
                ProcessBuilder builder = new ProcessBuilder(command);
                builder.start();
            } catch (IOException ignored) {}
            uri = Uri.fromFile(apkFile);
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");

        return intent;
    }
}
