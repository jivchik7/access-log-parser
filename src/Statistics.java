import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Map.Entry;
import java.net.URI;
import java.net.URISyntaxException;

public class Statistics {
    private long totalTraffic;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;

    private Set<String> nonExistentPages = new HashSet<>();
    private Map<String, Integer> browserFrequency = new HashMap<>();

    private Map<Integer, Integer> visitsBySecond = new HashMap<>();

    private Map<String, Integer> ipVisitCounts = new HashMap<>();

    private Set<String> referringDomains = new HashSet<>();

    public Statistics() {
        reset();
    }

    public void addEntry(LogEntry entry) {
        totalTraffic += entry.getResponseSize();

        LocalDateTime time = entry.getDateTime();
        if (minTime == null ||  time.isBefore(minTime)) {
            minTime = time;
        }
        if (maxTime == null ||  time.isAfter(maxTime)) {
            maxTime = time;
        }

        if (entry.getStatusCode() == 404) {
            nonExistentPages.add(entry.getRequestURL());
        }


        boolean isBot = entry.getUserAgent().contains("bot");
        if (!isBot) {

            int second = time.getSecond();
            visitsBySecond.compute(second, (k, v) -> v == null ? 1 : v + 1);


            String ipAddress = entry.getIpAddress();
            ipVisitCounts.merge(ipAddress, 1, Integer::sum);


            String referrer = entry.getReferer();
            if (referrer != null) {

                String domain = extractDomain(referrer);
                if (domain != null) {
                    referringDomains.add(domain);
                }
            }
        }

        String browserName = entry.getUserAgent().getBrowser().toString();
        if (!browserFrequency.containsKey(browserName)) {
            browserFrequency.put(browserName, 1);
        } else {
            browserFrequency.put(browserName, browserFrequency.get(browserName) + 1);
        }
    }

    private static String extractDomain(String url) {
        try {
            URI uri = new URI(url);
            String host = uri.getHost();
            if (host != null) {
                return host.startsWith("www.") ? host.substring(4) : host;
            }
        } catch (URISyntaxException e) {
            System.err.println("Ошибка при разборе URL: " + url);
        }
        return null;
    }


    public int getPeakVisitsPerSecond() {
        return visitsBySecond.values().stream()
                .mapToInt(Integer::valueOf)
                .max()
                .orElse(0);
    }

    public Set<String> getReferringDomains() {
        return referringDomains;
    }

    public int getMaxVisitsBySingleUser() {
        return ipVisitCounts.values().stream()
                .mapToInt(Integer::valueOf)
                .max()
                .orElse(0);
    }

    public Set<String> getNonExistentPages() {
        return nonExistentPages;
    }
    public double getTrafficRate() {
        if (minTime == null || maxTime == null) {
            return 0;
        }

        Duration duration = Duration.between(minTime, maxTime);
        long hoursBetween = duration.toHours();

        if (hoursBetween == 0) {
            return totalTraffic;
        }

        return (double) totalTraffic / hoursBetween;
    }


    public Map<String, Double> getBrowserDistribution() {
        int totalBrowsers = browserFrequency.values().stream().mapToInt(Integer::intValue).sum();
        Map<String, Double> distribution = new HashMap<>();

        for (Entry<String, Integer> entry : browserFrequency.entrySet()) {
            String browserName = entry.getKey();
            int count = entry.getValue();
            double ratio = (double) count / totalBrowsers;
            distribution.put(browserName, ratio);
        }

        return distribution;
    }

    public void reset() {
        totalTraffic = 0;
        minTime = null;
        maxTime = null;
        nonExistentPages.clear();
        browserFrequency.clear();
        visitsBySecond.clear();
        ipVisitCounts.clear();
        referringDomains.clear();
    }
}