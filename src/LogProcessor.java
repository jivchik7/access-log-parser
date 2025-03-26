import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LogProcessor {
    public static void main(String[] args) {
        String path = "C:\\Users\\EVMinaeva\\IdeaProjects\\AccessLogParser\\access.log";

        int totalLines = 0;
        int maxLength = 0;
        int minLength = Integer.MAX_VALUE;

        try (FileReader fileReader = new FileReader(path);
             BufferedReader reader = new BufferedReader(fileReader)) {

            String line;
            while ((line = reader.readLine()) != null) {
                totalLines++;

                int currentLength = line.length();
                if (currentLength > 1024) {
                    throw new LineTooLongException("Длина строки превышает 1024 символа.");
                }

                if (currentLength > maxLength) {
                    maxLength = currentLength;
                }
                if (currentLength < minLength) {
                    minLength = currentLength;
                }
            }
            System.out.println("Общее количество строк: " + totalLines);
            System.out.println("Длина самой длинной строки: " + maxLength);
            System.out.println("Длина самой короткой строки: " + minLength);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineTooLongException e) {
            System.err.println(e.getMessage());
        }
    }
    private static class LineTooLongException extends RuntimeException {
        public LineTooLongException(String message) {
            super(message);
        }
    }
}