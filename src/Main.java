import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Введите первое число:");
        int firstNumber = new Scanner(System.in).nextInt();
        System.out.println("Введите второе число:");
        int secondNumber = new Scanner(System.in).nextInt();

        int sub = firstNumber + secondNumber;
        int add = firstNumber - secondNumber;
        int mul = firstNumber * secondNumber;
        double div = ((double) (firstNumber))/secondNumber;

        System.out.println("Сумма чисел: " + sub);
        System.out.println("Разность чисел: " + add);
        System.out.println("Произведение чисел: " + mul);
        System.out.println("Частное чисел: " + div);
    }
}