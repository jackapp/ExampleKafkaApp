
import java.time.Instant;

/**
 * Represents the result of sending a message to a destination API.
 */
public class ApiResult {
    private final boolean success;
    private final int statusCode;
    private final String responseBody;
    private final String errorMessage;
    private final String requestPayload;
    private final Instant timestamp;
    private final long durationMs;

    private ApiResult(Builder builder) {
        this.success = builder.success;
        this.statusCode = builder.statusCode;
        this.responseBody = builder.responseBody;
        this.errorMessage = builder.errorMessage;
        this.requestPayload = builder.requestPayload;
        this.timestamp = builder.timestamp;
        this.durationMs = builder.durationMs;
    }

    public boolean isSuccess() { return success; }
    public int getStatusCode() { return statusCode; }
    public String getResponseBody() { return responseBody; }
    public String getErrorMessage() { return errorMessage; }
    public String getRequestPayload() { return requestPayload; }
    public Instant getTimestamp() { return timestamp; }
    public long getDurationMs() { return durationMs; }

    @Override
    public String toString() {
        return "ApiResult{" +
                "success=" + success +
                ", statusCode=" + statusCode +
                ", responseBody='" + responseBody + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", timestamp=" + timestamp +
                ", durationMs=" + durationMs +
                '}';
    }

    public static class Builder {
        private boolean success;
        private int statusCode;
        private String responseBody;
        private String errorMessage;
        private String requestPayload;
        private Instant timestamp = Instant.now();
        private long durationMs;

        public Builder success(boolean success) { this.success = success; return this; }
        public Builder statusCode(int code) { this.statusCode = code; return this; }
        public Builder responseBody(String body) { this.responseBody = body; return this; }
        public Builder errorMessage(String error) { this.errorMessage = error; return this; }
        public Builder requestPayload(String payload) { this.requestPayload = payload; return this; }
        public Builder durationMs(long ms) { this.durationMs = ms; return this; }

        public ApiResult build() { return new ApiResult(this); }
    }
}
