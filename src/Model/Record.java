package Model;

import Unit.Course;
import Unit.Instructor;
import Unit.Student;
import Util.DBUtil;
import com.mysql.jdbc.Connection;
import com.sun.org.apache.xpath.internal.SourceTreeManager;
import javafx.scene.control.Alert;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Record {

    public static final int MYSQL_DUPLICATE_PK = 1062;

    public static int addInstructor(Instructor ınstructor) {
        Connection conn = DBUtil.connect();
        System.out.print("Registering Instructor: "+ınstructor.getFirstname()+": ");
        try {
            String sql = "INSERT INTO instructors (ID, USER_NAME, PASSWORD, FIRST_NAME," +
                    " LAST_NAME, EMAIL, TELEPHONE) " +
                    "VALUES(?,?,?,?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1,ınstructor.getInstructor_id());
            stmt.setString(2,ınstructor.getUsername());
            stmt.setString(3,ınstructor.getPassword());
            stmt.setString(4,ınstructor.getFirstname());
            stmt.setString(5,ınstructor.getLastname());
            stmt.setString(6,ınstructor.getEmail());
            stmt.setString(7,ınstructor.getTelephone());
            if (stmt.executeUpdate()==1){
                System.out.println(" Success!");
                return 1;
            }else {
                System.out.println("No row affected!");
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == MYSQL_DUPLICATE_PK){
                new Alert(Alert.AlertType.ERROR,"Instructor ID must be unique").showAndWait();
            }
        }
        return 0;
    }

    public static int addStudent(Student student){
        Connection conn = DBUtil.connect();
        System.out.print("Registering Student: "+student.getFullname()+": ");
        try {
            String sql = "INSERT INTO students (ID, USER_NAME, PASSWORD, FIRST_NAME, LAST_NAME, EMAIL, TELEPHONE) " +
                    "VALUES(?,?,?,?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, student.getStudent_id());
            stmt.setString(2, student.getUsername());
            stmt.setString(3, student.getPassword());
            stmt.setString(4, student.getFirstname());
            stmt.setString(5, student.getLastname());
            stmt.setString(6, student.getEmail());
            stmt.setString(7, student.getTelephone());
            if (stmt.executeUpdate()==1){
                System.out.println(" Success!");
                return 1;
            }else {
                System.out.println("No row affected!");
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == MYSQL_DUPLICATE_PK){
                new Alert(Alert.AlertType.ERROR,"Student ID must be unique").showAndWait();
            }
            return 0;
        }
        return 0;
    }

    public static int addCourse(Course course){
        Connection conn = DBUtil.connect();
        System.out.print("Registering Course: "+ course.getCourse_name() +"by instructor: "+course.getInstructor_id());
        try {
            String sql = "INSERT INTO courses (COURSE_ID, COURSE_NAME, QUOTA, INSTRUCTOR_ID) "+
                    "VALUES(?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, course.getCourse_id());
            stmt.setString(2, course.getCourse_name());
            stmt.setInt(3, course.getQuota());
            stmt.setInt(4, course.getInstructor_id());
            if (stmt.executeUpdate()==1){
                System.out.println(" Success!");
                return 1;
            }else {
                System.out.println("No row affected!");
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == MYSQL_DUPLICATE_PK){
                new Alert(Alert.AlertType.ERROR,"Course ID must be unique").showAndWait();
            }
        }
        return 0;
    }

    public static int deleteInstructor(int instructor_id){
        Connection connection = DBUtil.connect();
        String sql = "DELETE FROM instructors WHERE ID = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1,instructor_id);
            if (stmt.executeUpdate() == 1){
                return 1;
            }
            else return 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int deleteStudent(int student_id){
        Connection connection = DBUtil.connect();
        String sql = "DELETE FROM students WHERE ID = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1,student_id);
            if (stmt.executeUpdate() == 1){
                return 1;
            } else return 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int deleteCourse(int course_id){
        Connection connection = DBUtil.connect();
        String sql = "DELETE FROM courses WHERE COURSE_ID = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1,course_id);
            if (stmt.executeUpdate() == 1){
                return 1;
            }
            else return 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int editInstructor(Instructor ınstructor, int new_id){
        Connection connection = DBUtil.connect();
        String sql = "UPDATE instructors SET USER_NAME = ?, PASSWORD = ?, FIRST_NAME = ?, LAST_NAME = ?, " +
                "EMAIL = ?, TELEPHONE = ?, ADMIN = ?, ID = ?  WHERE ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, ınstructor.getUsername());
            stmt.setString(2, ınstructor.getPassword());
            stmt.setString(3,ınstructor.getFirstname());
            stmt.setString(4,ınstructor.getLastname());
            stmt.setString(5,ınstructor.getEmail());
            stmt.setString(6,ınstructor.getTelephone());
            stmt.setBoolean(7,ınstructor.isAdmin());
            stmt.setInt(8,new_id);
            stmt.setInt(9,ınstructor.getInstructor_id());

            if (stmt.executeUpdate()==1){
                return 1;
            }else return 0;

        } catch (SQLException e) {
            if (e.getErrorCode() == MYSQL_DUPLICATE_PK){
                new Alert(Alert.AlertType.ERROR,"Instructor ID must be unique").showAndWait();
            }
            return 0;
        }

    }

    public static int editStudent(Student student, int new_id){
        Connection connection = DBUtil.connect();
        String sql = "UPDATE students SET USER_NAME = ?, PASSWORD = ?, FIRST_NAME = ?, LAST_NAME = ?, " +
                "EMAIL = ?, TELEPHONE = ?, ID = ? WHERE ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, student.getUsername());
            stmt.setString(2, student.getPassword());
            stmt.setString(3,student.getFirstname());
            stmt.setString(4,student.getLastname());
            stmt.setString(5,student.getEmail());
            stmt.setString(6,student.getTelephone());
            stmt.setInt(7, new_id);
            stmt.setInt(8, student.getStudent_id());

            if (stmt.executeUpdate()==1){
                return 1;
            }else return 0;

        } catch (SQLException e) {
            if (e.getErrorCode() == MYSQL_DUPLICATE_PK){
                new Alert(Alert.AlertType.ERROR,"Student ID must be unique").showAndWait();
            }
            return 0;
        }

    }
    public static int editCourse(Course course, String new_course_id){
        Connection connection = DBUtil.connect();
        String sql = "UPDATE courses SET COURSE_NAME = ?, QUOTA = ?, COURSE_ID = ?"+
                "WHERE COURSE_ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, course.getCourse_name());
            stmt.setInt(2, course.getQuota());
            stmt.setString(3,new_course_id);
            stmt.setString(4,course.getCourse_id());
            if (stmt.executeUpdate()==1){
                return 1;
            }else return 0;

        } catch (SQLException e) {
            if (e.getErrorCode() == MYSQL_DUPLICATE_PK){
                new Alert(Alert.AlertType.ERROR,"Course ID must be unique").showAndWait();
            }
            return 0;
        }
    }

}
