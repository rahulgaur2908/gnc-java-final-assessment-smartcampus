import java.util.*;
import java.io.*;

public class StudentManagementSystem {

    static HashMap<Integer, Student> students = new HashMap<>();
    static HashMap<Integer, Course> courses = new HashMap<>();
    static HashMap<Integer, ArrayList<Integer>> enrollments = new HashMap<>();

    static Scanner sc = new Scanner(System.in);

    // Add Student method with input validation and exception handling.

    public static void addStudent() throws InvalidDataException {
        System.out.print("Enter Student's ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter Student's Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Student's Email: ");
        String email = sc.nextLine();

        if (name.isEmpty() || email.isEmpty()) {
            throw new InvalidDataException("Invalid student data!");
        }

        students.put(id, new Student(id, name, email));
        System.out.println(" ✅ Student added successfully!");
    }

    // Add Course method with input validation and exception handling.

    public static void addCourse() throws InvalidDataException {
        System.out.print("Enter Course ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter Course Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Fee: ");
        double fee = sc.nextDouble();

        if (name.isEmpty() || fee <= 0) {
            throw new InvalidDataException("Invalid course data!");
        }

        courses.put(id, new Course(id, name, fee));
        System.out.println(" ✅ Course added successfully!");
    }

    // Enroll Student Method with checks for student and course existence, course capacity.
    public static void enrollStudent() throws InvalidDataException {
        System.out.print("Enter Student ID: ");
        int stuid = sc.nextInt();

        System.out.print("Enter Course ID: ");
        int corsid = sc.nextInt();

        if (!students.containsKey(stuid)) {
            throw new InvalidDataException(" ❌ Student not found!");
        }

        if (!courses.containsKey(corsid)) {
            throw new InvalidDataException(" ❌ Course not found!");
        }

        enrollments.putIfAbsent(stuid, new ArrayList<>());
        enrollments.get(stuid).add(corsid);

        // Start thread
        new EnrollmentProcessor(stuid, corsid).start();
    }

    // Method to view all students in the system.
    public static void viewStudents() {
        for (Student stu : students.values()) {
            System.out.println(stu);
        }
    }

    // View enrollments for each student, showing which courses they are enrolled in.
    public static void viewEnrollments() {
        for (int stuid : enrollments.keySet()) {
            System.out.println("Student ID: " + stuid + " -> Courses: " + enrollments.get(stuid));
        }
    }

    // Method to save student data to a file.

    public static void saveData() {
        try (PrintWriter writer = new PrintWriter("students.txt")) {
            for (Student stu : students.values()) {
                writer.println(stu.studentId + "," + stu.name + "," + stu.email);
            }
            System.out.println("✅ Data saved to file.");
        } catch (Exception e) {
            System.out.println(" ❌ Error saving file");
        }
    }

    // Main method to display menu and handle user input for different operations in the student management system.
    public static void main(String[] args) {

        while (true) {
            try {
                System.out.println("\n--- MENU ---");
                System.out.println("1. Add Student");
                System.out.println("2. Add Course");
                System.out.println("3. Enroll Student");
                System.out.println("4. View Students");
                System.out.println("5. View Enrollments");
                System.out.println("6. Save Data");
                System.out.println("7. Exit");

                System.out.print("Enter choice: ");
                int choice = sc.nextInt();

                // Handle user choice with a switch statement, calling the appropriate method for each operation.

                switch (choice) {
                    case 1: addStudent(); break;
                    case 2: addCourse(); break;
                    case 3: enrollStudent(); break;
                    case 4: viewStudents(); break;
                    case 5: viewEnrollments(); break;
                    case 6: saveData(); break;
                    case 7: System.exit(0);
                    default: System.out.println(" ❌ Invalid choice!");
                }
                // Catch specific exceptions for invalid data and input mismatch, providing user-friendly error messages.

            } catch (InvalidDataException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (InputMismatchException e) {
                System.out.println(" ❌Invalid input!.");
                sc.nextLine(); // clear buffer
            } catch (Exception e) {
                System.out.println(" ❌ An error occurred.");
            }
        }
    }
}

class Student {
    int studentId;
    String name;
    String email;

    public Student(int studentId, String name, String email) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
    }

    public String toString() {
        return studentId + " - " + name + " - " + email;
    }
}

class Course {
    int courseId;
    String courseName;
    double fee;

    public Course(int courseId, String courseName, double fee) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.fee = fee;
    }

    public String toString() {
        return courseId + " - " + courseName + " - Fee: " + fee;
    }
}

class EnrollmentProcessor extends Thread {
    private int studentId;
    private int courseId;

    public EnrollmentProcessor(int studentId, int courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
    }

    public void run() {
        try {
            System.out.println("Processing enrollment...");
            Thread.sleep(2000); // simulate delay
            System.out.println("Enrollment completed for Student " + studentId + " in Course " + courseId);
                              
        } catch (InterruptedException e) {
            System.out.println(" ❌ Error ");
        }
    }
}

class InvalidDataException extends Exception {
    public InvalidDataException(String message) {
        super(message);
    }
}