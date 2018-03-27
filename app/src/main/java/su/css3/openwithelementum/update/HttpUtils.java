package su.css3.openwithelementum.update;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {

    public static String get(String urlStr) {
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        BufferedReader buffer = null;
        String result = null;

        try {
            URL url = new URL(urlStr);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            inputStream = urlConnection.getInputStream();
            buffer = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder strBuilder = new StringBuilder();
            String line;

            while ((line = buffer.readLine()) != null) {
                strBuilder.append(line);
            }
            result = strBuilder.toString();
        } catch (Exception ignored) {
            // Do nothing
        } finally {
            if (buffer != null) {
                try {
                    buffer.close();
                } catch (IOException ignored) {}
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignored) {}
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return result;
    }
}
