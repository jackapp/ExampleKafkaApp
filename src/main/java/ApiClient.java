
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiClient {
    private static final Logger log = LoggerFactory.getLogger(ApiClient.class);
    private final String apiUrl;

    public ApiClient(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public ApiResult send(String message) {
        long start = System.currentTimeMillis();
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");

            try (OutputStream os = conn.getOutputStream()) {
                os.write(message.getBytes());
                os.flush();
            }

            int code = conn.getResponseCode();
            String responseBody = new String(conn.getInputStream().readAllBytes());

            return new ApiResult.Builder()
                    .success(code >= 200 && code < 300)
                    .statusCode(code)
                    .responseBody(responseBody)
                    .requestPayload(message)
                    .durationMs(System.currentTimeMillis() - start)
                    .build();

        } catch (Exception e) {
            return new ApiResult.Builder()
                    .success(false)
                    .statusCode(500)
                    .errorMessage(e.getMessage())
                    .requestPayload(message)
                    .durationMs(System.currentTimeMillis() - start)
                    .build();
        }
    }

}
