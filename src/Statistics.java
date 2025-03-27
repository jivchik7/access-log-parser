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

    private HashSet<String> uniquePages = new HashSet<>();


    private HashMap<String, Integer> osFrequency = new HashMap<>();

    public Statistics() {
        reset();
    }

    public void addEntry(LogEntry entry) {
        totalTraffic += entry.getResponseSize();

        LocalDateTime time = entry.getDateTime();
        if (minTime == null || time.isBefore(minTime)) {
            minTime = time;
        }
        if (maxTime == null || time.isAfter(maxTime)) {
            maxTime = time;
        }
        if (entry.getStatusCode() == 200) {
            uniquePages.add(entry.getRequestURL());
        }


        String osName = entry.getUserAgent().getOs().toString();
        if (!osFrequency.containsKey(osName)) {
            osFrequency.put(osName, 1);
        } else {
            osFrequency.put(osName, osFrequency.get(osName) + 1);
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


    public Set<String> getUniquePages() {
        return uniquePages;
    }


    public Map<String, Double> getOSDistribution() {
        int totalCount = osFrequency.values().stream().mapToInt(Integer::intValue).sum();
        HashMap<String, Double> distribution = new HashMap<>();

        for (Entry<String, Integer> entry : osFrequency.entrySet()) {
            String osName = entry.getKey();
            int count = entry.getValue();
            double ratio = (double) count / totalCount;
            distribution.put(osName, ratio);
        }

        return distribution;
    }

    public void reset() {
        totalTraffic = 0;
        minTime = null;
        maxTime = null;
        uniquePages.clear();
        osFrequency.clear();
    }
}