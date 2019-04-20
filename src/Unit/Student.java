package Unit;

import Util.DBUtil;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Alert;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Student implements User{
    private SimpleIntegerProperty student_id;
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private SimpleStringProperty fullname;
    private SimpleStringProperty email;
    private SimpleStringProperty telephone;

    public Student(int student_id, String username, String password, String firstname,
                   String lastname, String email, String telephone) {
        this.student_id = new SimpleIntegerProperty(student_id);
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.fullname = new SimpleStringProperty(firstname+" "+lastname);
        this.email = new SimpleStringProperty(email);
        this.telephone = new SimpleStringProperty(telephone);
    }

    public int getStudent_id() {
        return student_id.get();
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getFirstname() {
        return firstname;
    }
    public String getLastname() {
        return lastname;
    }
    public String getFullname() {
        return fullname.get();
    }
    public String getEmail() {
        return email.get();
    }
    public String getTelephone() {
        return telephone.get();
    }


    public static boolean updateGrade(int student_id, String course_id, int point){
        Connection conn = DBUtil.connect();
        String sql = "";
        String sql_update = "";
        String sql_select = "";
        String sql_enroll = "";

        if (point > 100) {
            new Alert(Alert.AlertType.ERROR,"Please enter a valid point (0-100) , or -1 to delete grade");
            return false;
        }else if (point == -1) {
            //Enroll course
            //Check if quota full
            Course newCourse = Course.getCourseById(course_id);
            if (newCourse.getAvaibleSpace()<=0) {
                //Quota Full
                new Alert(Alert.AlertType.ERROR,"Course quota full!").showAndWait();
                return false;
            }
            sql = "INSERT INTO `grades` (student_id, course_id, point) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE course_id = values (course_id)";
            sql_select = "SELECT * FROM `grades` WHERE student_id = ? AND course_id = ?";
            sql_enroll = "UPDATE `courses` SET ATTENDANTS = ATTENDANTS+1 WHERE COURSE_ID = ?";

            try {
                //Check for double attendants
                PreparedStatement stmt_select = conn.prepareStatement(sql_select);
                stmt_select.setInt(1, student_id);
                stmt_select.setString(2,course_id);
                ResultSet rs = stmt_select.executeQuery();
                if (rs.next()){
                    new Alert(Alert.AlertType.ERROR,"You can enroll a course one time.").showAndWait();
                    return false;
                }

                //Insert to grades table "enrollment"
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, student_id);
                stmt.setString(2, course_id);
                stmt.setInt(3, point);
                if (stmt.executeUpdate()!=1) return false;

                //Update attendants
                PreparedStatement stmt_enroll = conn.prepareStatement(sql_enroll);
                stmt_enroll.setString(1,course_id);
                if (stmt_enroll.executeUpdate()!=1) return false;

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
            return true;

        }else {
            //Update grade
            sql_update = "UPDATE `grades` SET `point` = ? WHERE student_id = ? and course_id = ?";
            try {
                PreparedStatement stmt = conn.prepareStatement(sql_update);
                stmt.setInt(1, point);
                stmt.setInt(2,student_id);
                stmt.setString(3,course_id);
                if (stmt.executeUpdate()!=1) return false;
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

    }

    public static Student getStudentById(int student_id){
        Connection connection = DBUtil.connect();
        String sql = "SELECT * FROM `students` WHERE ID = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1,student_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                return new Student(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7));
            }else {
                System.err.println("Student not found by id: " + student_id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }



}
