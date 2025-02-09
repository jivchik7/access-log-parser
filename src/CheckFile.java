import java.io.File;
import java.util.Scanner;

public class CheckFile {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int correctFilesCount = 0;

        while (true) {
            System.out.println("Введите путь к файлу:");
            String path = new Scanner(System.in).nextLine();

            File file = new File(path);
            boolean fileExists = file.exists();
            boolean isDirectory = file.isDirectory();

            if (!fileExists || isDirectory) {
                if (!fileExists) {
                    System.out.println("Файл не существует.");
                } else if (isDirectory) {
                    System.out.println("Указанный путь ведет к папке, а не к файлу");
                }
                continue;
            }
            correctFilesCount++;
            System.out.printf("Путь указан верно. Это файл номер %d.\n", correctFilesCount);
        }
}
}




