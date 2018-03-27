package su.css3.openwithelementum.update;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.v4.app.NotificationCompat.Builder;

import su.css3.openwithelementum.R;

public class NotificationUtils extends ContextWrapper {

    private NotificationManager mManager;
    private Context mContext;
    public static final String CHANNEL_ID = "su.css3.DOWNLOAD_PROGRESS";

    public NotificationUtils(Context context) {
        super(context);
        mContext = context;
        createChannels();
    }

    private void createChannels() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, getString(R.string.update_notification_channel), NotificationManager.IMPORTANCE_DEFAULT);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            channel.setImportance(NotificationManager.IMPORTANCE_LOW);
            channel.setSound(null, null);
            channel.enableLights(false);
            channel.enableVibration(false);
            getManager().createNotificationChannel(channel);
        }
    }

    private NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    public Builder createBuilder() {
        return new Builder(mContext, CHANNEL_ID);
    }
}
