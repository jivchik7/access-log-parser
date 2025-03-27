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
        OTHER
    }


    public UserAgent(String userAgent) {
        String[] parts = userAgent.split(";");


        OperatingSystem operatingSystem = determineOperatingSystem(parts[0]);
        this.os = operatingSystem;


        Browser browser = determineBrowser(parts[0]);
        this.browser = browser;
    }


    private OperatingSystem determineOperatingSystem(String part) {
        part = part.trim().toLowerCase();
        if (part.contains("windows")) {
            return OperatingSystem.WINDOWS;
        } else if (part.contains("macos")) {
            return OperatingSystem.MAC_OS;
        } else if (part.contains("linux")) {
            return OperatingSystem.LINUX;
        } else {
            return OperatingSystem.UNKNOWN;
        }
    }

    private Browser determineBrowser(String part) {
        part = part.trim().toLowerCase();
        if (part.contains("edge")) {
            return Browser.EDGE;
        } else if (part.contains("firefox")) {
            return Browser.FIREFOX;
        } else if (part.contains("chrome")) {
            return Browser.CHROME;
        } else if (part.contains("opera")) {
            return Browser.OPERA;
        } else {
            return Browser.OTHER;
        }
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