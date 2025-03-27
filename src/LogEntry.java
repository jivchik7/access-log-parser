import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LogEntry {
    private LocalDateTime dateTime;
    private final String ipAddress;
    private final HttpMethod method;
    private final String requestPath;
    private final int responseCode;
    private final long responseSize;
    private final String referer;
    private final UserAgent userAgent;

        public enum HttpMethod {
        GET,
        POST,
        PUT,
        DELETE,
        HEAD,
        OPTIONS,
        TRACE,
        CONNECT,
        PATCH
    }

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);

    public LogEntry(String logLine) throws IllegalArgumentException {
        String[] parts = logLine.split(" ");

        if (parts.length < 12) {
            throw new IllegalArgumentException("Недостаточно компонентов в строке лога");
        }

        this.ipAddress = parts[0];

        String text = parts[3] + " " + parts[4];
        this.dateTime = LocalDateTime.parse(
                text.substring(1,text.length()-1),
                DATE_TIME_FORMATTER
        );

        this.method = HttpMethod.valueOf(parts[5].replaceAll("[^A-Z]", ""));
        this.requestPath = parts[6];
        this.responseCode = Integer.parseInt(parts[8]);
        this.responseSize = Long.parseLong(parts[9]);
        this.referer = parts[10];
        this.userAgent = new UserAgent(parts[11]);
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public long getResponseSize() {
        return responseSize;
    }

    public String getReferer() {
        return referer;
    }

    public UserAgent getUserAgent() {
        return userAgent;
    }

    @Override
    public String toString() {
        return "LogEntry{" +
                "ipAddress='" + ipAddress + '\'' +
                ", dateTime=" + dateTime +
                ", method=" + method +
                ", requestPath='" + requestPath + '\'' +
                ", responseCode=" + responseCode +
                ", responseSize=" + responseSize +
                ", referer='" + referer + '\'' +
                ", userAgent=" + userAgent +
                '}';
    }
}