import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Map.Entry;

public class Statistics {
    private long totalTraffic;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;

    private Set<String> nonExistentPages = new HashSet<>();
    private Map<String, Integer> browserFrequency = new HashMap<>();


    private int visitCount = 0;
    private int errorCount = 0;
    private Set<String> uniqueIPAddresses = new HashSet<>();

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
            visitCount++;
            uniqueIPAddresses.add(entry.getIpAddress());
        }

        if (entry.getStatusCode() >= 400 && entry.getStatusCode() < 600) {
            errorCount++;
        }

        String browserName = entry.getUserAgent().getBrowser().toString();
        if (!browserFrequency.containsKey(browserName)) {
            browserFrequency.put(browserName, 1);
        } else {
            browserFrequency.put(browserName, browserFrequency.get(browserName) + 1);
        }
    }

    public double getTrafficRate() {
        if (minTime == null ||  maxTime == null) {
            return 0;
        }

        Duration duration = Duration.between(minTime, maxTime);
        long hoursBetween = duration.toHours();

        if (hoursBetween == 0) {
            return totalTraffic;
        }

        return (double) totalTraffic / hoursBetween;
    }

    public double getAverageVisitsPerHour() {
        if (visitCount == 0 || minTime == null ||  maxTime == null) {
            return 0;
        }

        Duration duration = Duration.between(minTime, maxTime);
        long hoursBetween = duration.toHours();

        if (hoursBetween == 0) {
            return visitCount;
        }

        return (double) visitCount / hoursBetween;
    }

    public double getAverageErrorsPerHour() {
        if (errorCount == 0 || minTime == null || maxTime == null) {
            return 0;
        }

        Duration duration = Duration.between(minTime, maxTime);
        long hoursBetween = duration.toHours();

        if (hoursBetween == 0) {
            return errorCount;
        }

        return (double) errorCount / hoursBetween;
    }

    public double getAverageVisitsPerUniqueUser() {
        if (uniqueIPAddresses.size() == 0 || visitCount == 0) {
            return 0;
        }

        return (double) visitCount / uniqueIPAddresses.size();
    }

    public Set<String> getNonExistentPages() {
        return nonExistentPages;
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
        visitCount = 0;
        errorCount = 0;
        uniqueIPAddresses.clear();
    }
}