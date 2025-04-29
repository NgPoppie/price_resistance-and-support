import java.util.Locale;
import java.util.Scanner;

public class Trend {

    public static void price_range(double low_price, double high_price) {
        double range = high_price - low_price;

        System.out.println("The resistance and support levels are: ");
        for (double i = low_price; i <= high_price; i += range / 9) {
            System.out.printf("%.2f%n", i); // Format output to 2 decimal places
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US); // Ensure dot (.) is used as decimal separator

        double low_price = getValidDouble(scanner, "Enter the low price: ");
        double high_price = getValidDouble(scanner, "Enter the high price: ");
        scanner.close();

        price_range(low_price, high_price);
    }

    private static double getValidDouble(Scanner scanner, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return scanner.nextDouble();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number (e.g., 59.86).");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }
}
