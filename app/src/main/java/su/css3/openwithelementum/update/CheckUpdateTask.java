package su.css3.openwithelementum.update;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import su.css3.openwithelementum.R;
import su.css3.openwithelementum.utils.AppUtils;
import su.css3.openwithelementum.utils.PreferencesUtils;

class CheckUpdateTask extends AsyncTask<Void, Void, String> {

    private static final String UPDATE_URL = "https://api.github.com/repos/Anonym-tsk/OpenWithElementum/releases/latest";
    public static final int TYPE_TOAST = 2;
    public static final int TYPE_DIALOG = 1;

    private ProgressDialog dialog;
    private Context mContext;
    private int mType;

    CheckUpdateTask(Context context, int type) {
        this.mContext = context;
        this.mType = type;
    }

    @Override
    protected void onPreExecute() {
        if (mType == TYPE_DIALOG) {
            dialog = new ProgressDialog(mContext);
            dialog.setMessage(mContext.getString(R.string.update_dialog_checking));
            dialog.show();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        parseJson(result);
        PreferencesUtils.setLastUpdateTime(mContext);
    }

    private void parseJson(String result) {
        if (result != null && !result.isEmpty()) {
            try {
                JSONObject obj = new JSONObject(result);
                String updateMessage = obj.getString("body");
                int apkCode = obj.getInt("tag_name");
                String releaseName = obj.getString("name");

                JSONObject assets = obj.getJSONArray("assets").getJSONObject(0);
                String apkUrl = assets.getString("browser_download_url");

                int versionCode = getCurrentVersionCode();

                if (apkCode > versionCode) {
                    if (mType == TYPE_DIALOG) {
                        UpdateDialog.show(mContext, releaseName, updateMessage, apkUrl);
                    } else if (mType == TYPE_TOAST) {
                        AppUtils.showMessage(mContext, R.string.update_notify_toast);
                    }
                } else if (mType == TYPE_DIALOG) {
                    AppUtils.showMessage(mContext, R.string.update_toast_no_new_update);
                }
                return;
            } catch (JSONException ignored) {}
        }

        if (mType == TYPE_DIALOG) {
            AppUtils.showMessage(mContext, R.string.update_check_error);
        }
    }

    private int getCurrentVersionCode() {
        try {
            return mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
    }

    @Override
    protected String doInBackground(Void... args) {
        return HttpUtils.get(UPDATE_URL);
    }
}
