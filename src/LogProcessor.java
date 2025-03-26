import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogProcessor {
    public static void main(String[] args) {
        String path = "C:\\Users\\EVMinaeva\\IdeaProjects\\AccessLogParser\\access.log"; // Исправлен путь для Windows

        int totalLines = 0;
        int yandexBotCount = 0;
        int googleBotCount = 0;

        Pattern userAgentPattern = Pattern.compile("\"([^\"]+)\"$");

        try (FileReader fileReader = new FileReader(path);
             BufferedReader reader = new BufferedReader(fileReader)) {

            String line;
            while ((line = reader.readLine()) != null) {
                totalLines++;

                int currentLength = line.length();
                if (currentLength > 1024) {
                    throw new LineTooLongException("Длина строки превышает 1024 символа.");
                }

                Matcher matcher = userAgentPattern.matcher(line);
                if (matcher.find()) {
                    String userAgent = matcher.group(1); // извлекаем User-Agent

                    // Обработка User-Agent
                    int startIndex = userAgent.indexOf('(');
                    if (startIndex == -1 || startIndex >= userAgent.length() - 1) {
                        continue; // Пропускаем строку, если не найдена открывающая скобка
                    }

                    int endIndex = userAgent.indexOf(')', startIndex + 1);
                    if (endIndex == -1 ||  endIndex <= startIndex) {
                        continue; // Пропускаем строку, если не найдена закрывающая скобка
                    }

                    String firstBracketContent = userAgent.substring(startIndex + 1, endIndex);

                    String[] parts = firstBracketContent.split(";");
                    if (parts.length >= 2) {
                        String fragment = parts[1].trim(); // Второй фрагмент после точки с запятой

                        // Отделяем часть до слэша
                        String programName = fragment.contains("/")
                                ? fragment.substring(0, fragment.indexOf('/'))
                                : fragment;

                        if (programName.equalsIgnoreCase("Googlebot")) {
                            googleBotCount++;
                        } else if (programName.equalsIgnoreCase("YandexBot")) {
                            yandexBotCount++;
                        }
                    }
                }
            }

            double yandexBotRatio = (totalLines == 0) ? 0 : (double) yandexBotCount / totalLines * 100;
            double googleBotRatio = (totalLines == 0) ? 0 : (double) googleBotCount / totalLines * 100;

            System.out.println("Общее количество строк: " + totalLines);
            System.out.printf("Доля запросов от YandexBot: %.2f%%%n", yandexBotRatio);
            System.out.printf("Доля запросов от Googlebot: %.2f%%%n", googleBotRatio);

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