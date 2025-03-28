import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserAgent {
    private final OperatingSystem os;
    private final Browser browser;

    public enum OperatingSystem {
        WINDOWS,
        MAC_OS,
        LINUX,
        UNKNOWN
    }

    public enum Browser {
        EDGE,
        FIREFOX,
        CHROME,
        OPERA,
        SAFARI,
        OTHER
    }

    public UserAgent(String userAgent) {
        this.os = parseOperatingSystem(userAgent);
        this.browser = parseBrowser(userAgent);
    }

    private OperatingSystem parseOperatingSystem(String userAgent) {
        Pattern pattern = Pattern.compile("\\$(.*?)\\$");
        Matcher matcher = pattern.matcher(userAgent);

        if (matcher.find()) {
            String osPart = matcher.group(1);
            if (osPart.contains("Windows")) {
                return OperatingSystem.WINDOWS;
            } else if (osPart.contains("Macintosh") || osPart.contains("Mac OS X")) {
                return OperatingSystem.MAC_OS;
            } else if (osPart.contains("Linux")) {
                return OperatingSystem.LINUX;
            }
        }
        return OperatingSystem.UNKNOWN;
    }

    private Browser parseBrowser(String userAgent) {
        // Поиск известных браузеров
        if (userAgent.contains("Edg")) {
            return Browser.EDGE;
        } else if (userAgent.contains("Firefox")) {
            return Browser.FIREFOX;
        } else if (userAgent.contains("Chrome")) {
            return Browser.CHROME;
        } else if (userAgent.contains("Opera")) {
            return Browser.OPERA;
        } else if (userAgent.contains("Safari")) {
            return Browser.SAFARI;
        }
        return Browser.OTHER;
    }

    public OperatingSystem getOs() {
        return os;
    }

    public Browser getBrowser() {
        return browser;
    }

    @Override
    public String toString() {
        return "UserAgent{" +
                "os=" + os +
                ", browser=" + browser +
                '}';
    }
}