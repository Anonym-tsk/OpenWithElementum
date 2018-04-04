package su.css3.openwithelementum;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.io.IOException;
import java.io.InputStream;

import su.css3.openwithelementum.utils.MagnetBuilder;
import su.css3.openwithelementum.update.UpdateChecker;
import su.css3.openwithelementum.utils.AppUtils;
import su.css3.openwithelementum.utils.PreferencesUtils;

public class OpenLinkActivity extends Activity {

    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    private Uri link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.link = getIntent().getData();
        if (this.link == null) {
            finish();
            return;
        }

        if ("magnet".equals(this.link.getScheme())) {
            openLink(this.link.toString());
            finish();
            return;
        }

        if (checkPermissions()) {
            openTorrent(this.link);
            finish();
            return;
        }

        requestPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openTorrent(this.link);
        }
        finish();
    }

    protected boolean checkPermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    protected void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
    }

    protected void openTorrent(Uri filename) {
        final Context context = getApplicationContext();

        try {
            InputStream inputStream = getContentResolver().openInputStream(filename);
            String link = MagnetBuilder.build(inputStream);
            openLink(link);
        } catch (IOException e) {
            AppUtils.showMessage(context, R.string.torrent_file_broken);
        }
    }

    protected void openLink(String magnet) {
        final Context context = getApplicationContext();

        String kodiPackageName = PreferencesUtils.getKodiPackageName(context);
        if (kodiPackageName != null) {
            AppUtils.activateApp(context, kodiPackageName);
        }

        AppUtils.showMessage(context, R.string.elementum_link_sent);

        AppUtils.playMagnet(context, magnet, status -> {
            if (!status) {
                AppUtils.showMessage(context, R.string.elementum_not_available);
            }
        });

        UpdateChecker.checkForToast(context);
    }
}
