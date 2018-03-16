package su.css3.openwithelementum.system;

import android.os.AsyncTask;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WebService extends AsyncTask<String, Void, Boolean> {
    private String url;
    private ResponseListener responseListener;
    private Integer maxAttempts;
    private Integer attempt = 0;

    public WebService(String url, ResponseListener responseListener, Integer maxAttempts) {
        this.url = url;
        this.responseListener = responseListener;
        this.maxAttempts = maxAttempts;
    }

    private boolean makeRequest(URL url) {
        if (attempt++ > 0) {
            if (attempt > maxAttempts) {
                return false;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        HttpURLConnection httpConnection = null;
        try {
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("GET");
            httpConnection.setRequestProperty("Content-length", "0");
            httpConnection.setUseCaches(false);
            httpConnection.setAllowUserInteraction(false);
            httpConnection.setConnectTimeout(200);
            httpConnection.setReadTimeout(1000);

            httpConnection.connect();
            int responseCode = httpConnection.getResponseCode();
            httpConnection.disconnect();
            return responseCode == HttpURLConnection.HTTP_OK || makeRequest(url);
        } catch (Exception ex) {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
            return makeRequest(url);
        }
    }

    @Override
    protected Boolean doInBackground(String... params) {
        URL url;
        try {
            url = new URL(this.url);
        } catch (MalformedURLException e) {
            return false;
        }
        return makeRequest(url);
    }

    @Override
    protected void onPostExecute(Boolean status) {
        super.onPostExecute(status);
        if (responseListener != null) {
            responseListener.onReady(status);
        }
    }

    public interface ResponseListener {
        void onReady(Boolean status);
    }
}