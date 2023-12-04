package rosaria.http;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public final class Crawler {

    private static final String USER_AGENT = "Mozilla/5.0";
    private static final int DEFAULT_TIMEOUT = 5000;

    public static String crawl(URL url) throws IOException {
        return crawl(url, DEFAULT_TIMEOUT);
    }

    public static String crawl(URL url, int timeout) throws IOException {
        HttpURLConnection connection = null;
        try {
            connection = setupHttpConnection(url, timeout);
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("HTTPレスポンスコードがOKではありません: " + responseCode);
            }

            return readResponse(connection);
        } catch (IOException e) {
            throw new IOException(e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static HttpURLConnection setupHttpConnection(URL url, int timeout) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(timeout); // 接続タイムアウトを設定
        connection.setReadTimeout(timeout);    // 読み込みタイムアウトを設定
        connection.setRequestProperty("User-Agent", USER_AGENT); // ユーザーエージェントを設定
        return connection;
    }

    private static String readResponse(HttpURLConnection connection) throws IOException {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            return response.toString(); // HTTPレスポンスの内容を返す。
        }
    }
}

