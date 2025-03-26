import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String path = "C:\\Users\\EVMinaeva\\IdeaProjects\\AccessLogParser\\access.log";

        Statistics statistics = new Statistics();

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    LogEntry entry = new LogEntry(line);
                    statistics.addEntry(entry);
                } catch (IllegalArgumentException e) {
                    System.err.println("Ошибка при разборе строки: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Средний объем трафика за час: " + statistics.getTrafficRate());
    }
}