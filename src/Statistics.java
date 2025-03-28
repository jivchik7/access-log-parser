import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Statistics {
    private long totalTraffic;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;


    private HashSet<String> nonExistentPages = new HashSet<>();


    private HashMap<String, Integer> browserFrequency = new HashMap<>();

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


        String browserName = entry.getUserAgent().getBrowser().toString();
        if (!browserFrequency.containsKey(browserName)) {
            browserFrequency.put(browserName, 1);
        } else {
            browserFrequency.put(browserName, browserFrequency.get(browserName) + 1);
        }
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


    public Set<String> getNonExistentPages() {
        return nonExistentPages;
    }


    public Map<String, Double> getBrowserDistribution() {
        int totalBrowsers = browserFrequency.values().stream().mapToInt(Integer::intValue).sum();
        HashMap<String, Double> distribution = new HashMap<>();

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
    }
}