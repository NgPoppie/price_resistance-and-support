import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

public class Trend {

    public static void price_range(double low_price, double high_price, List<Integer> gannNumbers) {
        if (low_price >= high_price) {
            System.out.println("Error: High price must be greater than low price.");
            return;
        }
        
        double range = high_price - low_price;
        boolean foundMatch = false;

        System.out.println("The resistance and support levels are: ");
        for (double i = low_price; i <= high_price; i += range / 9) {
            System.out.printf("%.2f", i);
            // Check if price level is within ±20 of any Gann Square number
            for (int gann : gannNumbers) {
                if (Math.abs(i - gann) <= 20) {
                    System.out.print(" (Near Gann Square number " + gann + " - High possibility of trend shift)");
                    foundMatch = true;
                    break;
                }
            }
            System.out.println();
        }
        if (!foundMatch) {
            System.out.println("No price levels are within ±20 of Gann Square numbers.");
        }
    }

    public static void time_range(Date start, Date end, List<Integer> gannNumbers) {
        if (start == null || end == null) {
            System.out.println("Error: Start or end date cannot be null.");
            return;
        }

        long diffInMillies = end.getTime() - start.getTime();
        long day_range = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

        if (day_range < 0) {
            System.out.println("Error: End date must be after start date.");
            return;
        }

        int range_in_weeks = 0;
        if (day_range > 7) {
            range_in_weeks = (int) (day_range / 7);
            System.out.println("Days: " + day_range + ", Weeks: " + range_in_weeks);
        } else {
            System.out.println("Days: " + day_range + " (Less than or equal to 7 days, no weeks calculated).");
        }

        // Check if day range is within ±20 of any Gann Square number
        boolean dayMatch = false;
        for (int gann : gannNumbers) {
            if (Math.abs(day_range - gann) <= 20) {
                System.out.println("Day range " + day_range + " is near Gann Square number " + gann + " - High possibility of trend shift");
                dayMatch = true;
                break;
            }
        }

        // Check if week range is within ±20 of any Gann Square number (only if weeks are calculated)
        boolean weekMatch = false;
        if (range_in_weeks > 0) {
            for (int gann : gannNumbers) {
                if (Math.abs(range_in_weeks - gann) <= 20) {
                    System.out.println("Week range " + range_in_weeks + " is near Gann Square number " + gann + " - High possibility of trend shift");
                    weekMatch = true;
                    break;
                }
            }
        }

        // Summary if no matches
        if (!dayMatch && !weekMatch) {
            System.out.println("Neither day nor week range is within ±20 of any Gann Square number.");
        } else if (!dayMatch) {
            System.out.println("Day range is not within ±20 of any Gann Square number.");
        } else if (!weekMatch && range_in_weeks > 0) {
            System.out.println("Week range is not within ±20 of any Gann Square number.");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Get price range inputs
        System.out.println("=== Price Range Calculation ===");
        double low_price = getValidDouble(scanner, "Enter the low price: ");
        double high_price = getValidDouble(scanner, "Enter the high price: ");

        // Clear scanner buffer
        scanner.nextLine();

        // Get time range inputs
        System.out.println("\n=== Time Range Calculation ===");
        Date start = getValidDate(scanner, dateFormat, "Enter start date (yyyy-MM-dd, e.g., 2025-04-01): ");
        Date end = getValidDate(scanner, dateFormat, "Enter end date (yyyy-MM-dd, e.g., 2025-04-15): ");

        // Get Gann Square level
        System.out.println("\n=== Gann Square of 12 Calculation ===");
        int level = getValidInt(scanner, "Enter the level of the Square of 12 (1 or higher): ");

        // Generate Gann Square and get cross/diagonal numbers
        List<Integer> gannNumbers = generateSquareOf12CrossDiagonal(level);

        // Run price and time range calculations with Gann checks
        System.out.println("\n=== Price Range Results ===");
        price_range(low_price, high_price, gannNumbers);

        System.out.println("\n=== Time Range Results ===");
        time_range(start, end, gannNumbers);

        scanner.close();
    }

    private static double getValidDouble(Scanner scanner, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return scanner.nextDouble();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number (e.g., 59.86).");
                scanner.nextLine();
            }
        }
    }

    private static int getValidInt(Scanner scanner, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = scanner.nextInt();
                if (value < 1) {
                    System.out.println("Level must be 1 or higher.");
                    continue;
                }
                return value;
            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.nextLine();
            }
        }
    }

    private static Date getValidDate(Scanner scanner, SimpleDateFormat dateFormat, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine();
                return dateFormat.parse(input);
            } catch (Exception e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd (e.g., 2025-04-29).");
            }
        }
    }

    public static List<Integer> generateSquareOf12CrossDiagonal(int level) {
        List<Integer> crossDiagonalNumbers = new ArrayList<>();
        if (level < 1) {
            System.out.println("Level must be 1 or higher.");
            return crossDiagonalNumbers;
        }

        int n = (level * 2) - 1;
        int[][] square = new int[n][n];

        int x = n / 2;
        int y = n / 2;
        int currentNumber = 1;

        square[y][x] = currentNumber;
        if (isCrossOrDiagonal(x, y, n)) {
            crossDiagonalNumbers.add(currentNumber);
        }

        for (int ring = 1; ring <= level - 1; ring++) {
            x++;
            currentNumber += 12;
            square[y][x] = currentNumber;
            if (isCrossOrDiagonal(x, y, n)) {
                crossDiagonalNumbers.add(currentNumber);
            }

            for (int i = 0; i < ring * 2 - 1; i++) {
                y++;
                currentNumber += 12;
                square[y][x] = currentNumber;
                if (isCrossOrDiagonal(x, y, n)) {
                    crossDiagonalNumbers.add(currentNumber);
                }
            }

            for (int i = 0; i < ring * 2; i++) {
                x--;
                currentNumber += 12;
                square[y][x] = currentNumber;
                if (isCrossOrDiagonal(x, y, n)) {
                    crossDiagonalNumbers.add(currentNumber);
                }
            }

            for (int i = 0; i < ring * 2; i++) {
                y--;
                currentNumber += 12;
                square[y][x] = currentNumber;
                if (isCrossOrDiagonal(x, y, n)) {
                    crossDiagonalNumbers.add(currentNumber);
                }
            }

            for (int i = 0; i < ring * 2; i++) {
                x++;
                currentNumber += 12;
                square[y][x] = currentNumber;
                if (isCrossOrDiagonal(x, y, n)) {
                    crossDiagonalNumbers.add(currentNumber);
                }
            }
        }

        printCrossAndDiagonal(square);
        return crossDiagonalNumbers;
    }

    private static boolean isCrossOrDiagonal(int x, int y, int size) {
        int center = size / 2;
        return x == center || y == center || x == y || x + y == size - 1;
    }

    public static void printCrossAndDiagonal(int[][] square) {
        int size = square.length;
        System.out.println("Gann Square of 12 (Cross and Diagonal Numbers):");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (isCrossOrDiagonal(j, i, size)) {
                    System.out.printf("%4d ", square[i][j]);
                } else {
                    System.out.print("     ");
                }
            }
            System.out.println();
        }
    }
}
