package su.css3.openwithelementum;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import su.css3.openwithelementum.utils.AppUtils;
import su.css3.openwithelementum.utils.PreferencesUtils;

public class OpenLinkActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openLink(getIntent().getData());
        this.finish();
    }

    protected void openLink(Uri magnet) {
        final Context context = getApplicationContext();

        String kodiPackageName = PreferencesUtils.getKodiPackageName(context);
        if (kodiPackageName != null) {
            AppUtils.activateApp(context, kodiPackageName);
        }

        AppUtils.showMessage(context, context.getResources().getString(R.string.elementum_link_sent));

        AppUtils.playMagnet(context, magnet, status -> {
            if (!status) {
                AppUtils.showMessage(context, context.getResources().getString(R.string.elementum_not_available));
            }
        });
    }
}
