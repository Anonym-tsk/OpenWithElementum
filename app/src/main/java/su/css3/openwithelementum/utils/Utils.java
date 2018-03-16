package su.css3.openwithelementum.utils;

import android.content.Context;
import android.widget.Toast;

public class Utils {
    public static void showMessage(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}
