package su.css3.openwithelementum.utils;

import android.content.Context;
import android.net.Uri;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import su.css3.openwithelementum.system.WebService;

public class ElementumUtils {
    public static void playMagnet(Context context, Uri magnetUrl, WebService.ResponseListener listener) {
        final String host = PreferencesUtils.getHost(context);
        final int maxAttempts = PreferencesUtils.getTimeout(context);

        try {
            String link = "http://" + host + ":65220/playuri?uri=" + URLEncoder.encode(magnetUrl.toString(), "UTF-8");
            new WebService(link, listener, maxAttempts).execute();
        } catch (UnsupportedEncodingException e) {
            listener.onReady(false);
        }
    }
}
