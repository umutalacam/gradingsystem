package Model;

import Unit.Instructor;
import Unit.Student;
import Unit.User;
import Util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login {

    public static User currentUser;

    public static boolean login(String username, String password) {
        System.out.println("Attempt to Log In.");
        Connection conn = DBUtil.connect();
        String sql_instructor = "SELECT * FROM instructors WHERE USER_NAME = ? AND PASSWORD = ?";
        String sql_student = "SELECT * FROM students WHERE USER_NAME = ? AND PASSWORD = ?";
        try {
            PreparedStatement stmt_instructor = conn.prepareStatement(sql_instructor);
            PreparedStatement stmt_student = conn.prepareStatement(sql_student);
            stmt_instructor.setString(1, username);
            stmt_instructor.setString(2, password);
            stmt_student.setString(1, username);
            stmt_student.setString(2, password);

            ResultSet rs_instructor = stmt_instructor.executeQuery();
            ResultSet rs_student = stmt_student.executeQuery();

            if (rs_instructor.next()) {
                currentUser = new Instructor(rs_instructor.getInt(1), rs_instructor.getString(2),
                        rs_instructor.getString(3), rs_instructor.getString(4),
                        rs_instructor.getString(5), rs_instructor.getString(6),
                        rs_instructor.getString(7), rs_instructor.getBoolean(8));
                return true;
            } else if (rs_student.next()) {
                currentUser = new Student(rs_student.getInt(1), rs_student.getString(2),
                        rs_student.getString(3), rs_student.getString(4),
                        rs_student.getString(5), rs_student.getString(6),
                        rs_student.getString(7));
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

}
