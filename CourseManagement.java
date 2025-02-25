import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class CourseManagement {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/CourseManagement";
    private static final String USER = "root"; // Change to your MySQL username
    private static final String PASSWORD = "venkat28"; // Change to your MySQL password

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             Scanner scanner = new Scanner(System.in)) {

            createTable(conn);

            while (true) {
                System.out.println("\nChoose an operation:");
                System.out.println("1. Insert Course");
                System.out.println("2. Update Course Credits");
                System.out.println("3. Delete Course");
                System.out.println("4. Display Courses");
                System.out.println("5. Exit");
                System.out.print("Enter choice: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        insertCourse(conn, scanner);
                        break;
                    case 2:
                        updateCourse(conn, scanner);
                        break;
                    case 3:
                        deleteCourse(conn, scanner);
                        break;
                    case 4:
                        displayCourses(conn);
                        break;
                    case 5:
                        System.out.println("Exiting program...");
                        return;
                    default:
                        System.out.println("Invalid choice, try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS Courses (" +
                     "CourseID INT PRIMARY KEY, " +
                     "Name VARCHAR(100), " +
                     "Credits INT)";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table 'Courses' created or already exists.");
        }
    }

    private static void insertCourse(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter Course ID: ");
        int courseId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter Course Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Credits: ");
        int credits = scanner.nextInt();

        String sql = "INSERT INTO Courses (CourseID, Name, Credits) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, courseId);
            pstmt.setString(2, name);
            pstmt.setInt(3, credits);
            pstmt.executeUpdate();
            System.out.println("Record inserted successfully.");
        }
    }

    private static void updateCourse(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter Course ID to update: ");
        int courseId = scanner.nextInt();
        System.out.print("Enter new Credits: ");
        int credits = scanner.nextInt();

        String sql = "UPDATE Courses SET Credits = ? WHERE CourseID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, credits);
            pstmt.setInt(2, courseId);
            int updated = pstmt.executeUpdate();
            if (updated > 0) {
                System.out.println("Record updated successfully.");
            } else {
                System.out.println("Course ID not found.");
            }
        }
    }

    private static void deleteCourse(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter Course ID to delete: ");
        int courseId = scanner.nextInt();

        String sql = "DELETE FROM Courses WHERE CourseID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, courseId);
            int deleted = pstmt.executeUpdate();
            if (deleted > 0) {
                System.out.println("Record deleted successfully.");
            } else {
                System.out.println("Course ID not found.");
            }
        }
    }

    private static void displayCourses(Connection conn) throws SQLException {
        String sql = "SELECT * FROM Courses";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\nCourses Table Records:");
            System.out.println("-----------------------------------------");
            while (rs.next()) {
                System.out.println("CourseID: " + rs.getInt("CourseID") +
                                   " | Name: " + rs.getString("Name") +
                                   " | Credits: " + rs.getInt("Credits"));
            }
        }
    }
}
