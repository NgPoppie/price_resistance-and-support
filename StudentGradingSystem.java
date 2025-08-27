import java.util.*;

public class StudentGradingSystem {
    // Class to store student information
    static class Student {
        String name;
        String admissionNumber;
        double[] marks;
        double totalMarks;
        double averageMarks;
        String grade;

        Student(String name, String admissionNumber, int numSubjects) {
            this.name = name;
            this.admissionNumber = admissionNumber;
            this.marks = new double[numSubjects];
        }
    }

    // Scanner for user input
    static Scanner scanner = new Scanner(System.in);

    // Method to collect student data
    public static Student collectStudentData(String[] subjects, int studentIndex) {
        System.out.println("\nEnter details for Student " + (studentIndex + 1));
        System.out.print("Enter student name: ");
        String name = scanner.nextLine();
        System.out.print("Enter admission number: ");
        String admissionNumber = scanner.nextLine();

        Student student = new Student(name, admissionNumber, subjects.length);
        
        // Collect marks for each subject with validation
        for (int i = 0; i < subjects.length; i++) {
            double mark;
            do {
                System.out.print("Enter marks for " + subjects[i] + " (0-100): ");
                while (!scanner.hasNextDouble()) {
                    System.out.println("Invalid input! Please enter a number between 0 and 100.");
                    scanner.next();
                }
                mark = scanner.nextDouble();
                if (mark < 0 || mark > 100) {
                    System.out.println("Invalid marks! Marks must be between 0 and 100.");
                }
            } while (mark < 0 || mark > 100);
            student.marks[i] = mark;
        }
        scanner.nextLine(); // Clear buffer
        return student;
    }

    // Method to calculate total, average marks, and assign grade
    public static void calculateResults(Student student) {
        double sum = 0;
        for (double mark : student.marks) {
            sum += mark;
        }
        student.totalMarks = sum;
        student.averageMarks = sum / student.marks.length;

        // Assign grade based on average marks
        if (student.averageMarks >= 80) {
            student.grade = "A";
        } else if (student.averageMarks >= 60) {
            student.grade = "B";
        } else if (student.averageMarks >= 50) {
            student.grade = "C";
        } else if (student.averageMarks >= 40) {
            student.grade = "D";
        } else {
            student.grade = "F";
        }
    }

    // Method to display individual student report
    public static void displayStudentReport(Student student, String[] subjects) {
        System.out.println("\n--- Student Report ---");
        System.out.println("Name: " + student.name);
        System.out.println("Admission Number: " + student.admissionNumber);
        System.out.println("Number of Subjects: " + subjects.length);
        System.out.println("Subject-wise Marks:");
        for (int i = 0; i < subjects.length; i++) {
            System.out.printf("%s: %.2f%s%n", subjects[i], student.marks[i], 
                            student.marks[i] < 40 ? " (Failed)" : "");
        }
        System.out.printf("Total Marks: %.2f%n", student.totalMarks);
        System.out.printf("Average Marks: %.2f%n", student.averageMarks);
        System.out.println("Grade: " + student.grade);
    }

    // Method to find and display best performers in each subject
    public static void displayBestPerformers(List<Student> students, String[] subjects) {
        System.out.println("\n--- Best Performers by Subject ---");
        for (int i = 0; i < subjects.length; i++) {
            double maxMark = -1;
            String topStudent = "";
            for (Student student : students) {
                if (student.marks[i] > maxMark) {
                    maxMark = student.marks[i];
                    topStudent = student.name;
                }
            }
            System.out.printf("%s: %s (%.2f)%n", subjects[i], topStudent, maxMark);
        }
    }

    public static void main(String[] args) {
        while (true) {
            // Input number of students
            System.out.print("Enter the number of students: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input! Please enter a positive number.");
                scanner.next();
            }
            int numStudents = scanner.nextInt();
            if (numStudents <= 0) {
                System.out.println("Number of students must be positive!");
                continue;
            }
            scanner.nextLine(); // Clear buffer

            // Input number of subjects
            System.out.print("Enter the number of subjects: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input! Please enter a positive number.");
                scanner.next();
            }
            int numSubjects = scanner.nextInt();
            if (numSubjects <= 0) {
                System.out.println("Number of subjects must be positive!");
                continue;
            }
            scanner.nextLine(); // Clear buffer

            // Input subject names
            String[] subjects = new String[numSubjects];
            for (int i = 0; i < numSubjects; i++) {
                System.out.print("Enter name of subject " + (i + 1) + ": ");
                subjects[i] = scanner.nextLine();
            }

            // Collect data for all students
            List<Student> students = new ArrayList<>();
            for (int i = 0; i < numStudents; i++) {
                Student student = collectStudentData(subjects, i);
                calculateResults(student);
                students.add(student);
            }

            // Sort students by total marks (descending)
            students.sort((s1, s2) -> Double.compare(s2.totalMarks, s1.totalMarks));

            // Display all student reports
            System.out.println("\n=== Student Performance Reports (Ranked) ===");
            for (Student student : students) {
                displayStudentReport(student, subjects);
            }

            // Display best performers
            displayBestPerformers(students, subjects);

            // Display failed subjects summary
            System.out.println("\n--- Students with Failed Subjects ---");
            boolean hasFailures = false;
            for (Student student : students) {
                boolean failed = false;
                for (int i = 0; i < subjects.length; i++) {
                    if (student.marks[i] < 40) {
                        if (!failed) {
                            System.out.println("Student: " + student.name + " (" + student.admissionNumber + ")");
                            failed = true;
                            hasFailures = true;
                        }
                        System.out.println("  Failed " + subjects[i] + ": " + student.marks[i]);
                    }
                }
            }
            if (!hasFailures) {
                System.out.println("No students failed any subjects.");
            }

            System.out.println("\nAll reports generated successfully.");

            // Ask if user wants to process another batch
            System.out.print("\nWould you like to process another batch? (yes/no): ");
            String continueChoice = scanner.nextLine().trim().toLowerCase();
            if (!continueChoice.equals("yes")) {
                break;
            }
        }
        scanner.close();
    }
}
