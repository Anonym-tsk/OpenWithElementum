package su.css3.openwithelementum;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import su.css3.openwithelementum.system.WebService;
import su.css3.openwithelementum.utils.ElementumUtils;
import su.css3.openwithelementum.utils.KodiUtils;
import su.css3.openwithelementum.utils.PreferencesUtils;
import su.css3.openwithelementum.utils.Utils;

public class OpenLinkActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openLink(getIntent().getData());
        this.finish();
    }

    protected void openLink(Uri magnet) {
        final Context context = getApplicationContext();

        if (PreferencesUtils.isLocally(context)) {
            boolean hasKodi = KodiUtils.activateKodi(context);
            if (!hasKodi) {
                Utils.showMessage(context, "Kodi is not installed");
                return;
            }
        }

        ElementumUtils.playMagnet(context, magnet, new WebService.ResponseListener() {
            @Override
            public void onReady(Boolean status) {
                if (!status) {
                    Utils.showMessage(context, "Elementum not available");
                }
            }
        });
    }
}
