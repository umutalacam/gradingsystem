package Unit;

import Util.DBUtil;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Course {
    private int instructor_id;
    private int attendants;
    private SimpleStringProperty instructorName;
    private SimpleStringProperty course_id;
    private SimpleStringProperty course_name;
    private SimpleIntegerProperty  quota;
    private SimpleStringProperty avaible_quota;
    private String dump;

    public Course(int instructor_id, String course_name, int quota, String course_id) {
        this.setInstructor_id(instructor_id);
        this.course_id = new SimpleStringProperty(course_id);
        this.course_name = new SimpleStringProperty(course_name);
        this.quota = new SimpleIntegerProperty(quota);
        this.instructorName = new SimpleStringProperty(getInstructorFullName());
        this.dump = String.format("%s\t%s\t%s",course_id,getInstructorFullName(),getCourse_name());
    }

    public Course(int instructor_id, String course_name, int quota, String course_id, int attendants) {
        this.setInstructor_id(instructor_id);
        this.course_id = new SimpleStringProperty(course_id);
        this.course_name = new SimpleStringProperty(course_name);
        this.quota = new SimpleIntegerProperty(quota);
        this.instructorName = new SimpleStringProperty(getInstructorFullName());
        this.dump = String.format("%s\t%s\t%s",course_id,getInstructorFullName(),getCourse_name());
        this.avaible_quota = new SimpleStringProperty(attendants+"/"+quota);
        this.attendants = attendants;
    }

    private String getInstructorFullName(){
        Connection conn = DBUtil.connect();
        String fullname = "Error";
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT `first_name`, `last_name` FROM instructors WHERE ID = ?");
            stmt.setInt(1, getInstructor_id());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                fullname = rs.getString(1) + " " + rs.getString(2);
            }
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fullname;
    }

    public static Course getCourseById(String course_id){
        Connection conn = DBUtil.connect();
        Course course = null;
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM courses WHERE COURSE_ID = ?");
            stmt.setString(1, course_id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                 course = new Course(rs.getInt(4),
                         rs.getString(2),
                         rs.getInt(3),
                         rs.getString(1),
                         rs.getInt(5));
            }
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return course;
    }

    public int getInstructor_id() {
        return instructor_id;
    }

    public void setInstructor_id(int instructor_id) {
        this.instructor_id = instructor_id;
    }

    public String getInstructorName() {
        return instructorName.get();
    }

    public SimpleStringProperty instructorNameProperty() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName.set(instructorName);
    }

    public String getCourse_id() {
        return course_id.get();
    }


    public void setCourse_id(String course_id) {
        this.course_id.set(course_id);
    }

    public String getCourse_name() {
        return course_name.get();
    }

    public SimpleStringProperty course_nameProperty() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name.set(course_name);
    }

    public int getQuota() {
        return quota.get();
    }

    public SimpleIntegerProperty quotaProperty() {
        return quota;
    }

    public void setQuota(int quota) {
        this.quota.set(quota);
    }

    public String getDump() {
        return dump;
    }

    public String getAvaible_quota() {
        return avaible_quota.get();
    }

    public int getAvaibleSpace(){
        return quota.get()-attendants;
    }

    public SimpleStringProperty avaible_quotaProperty() {
        return avaible_quota;
    }
}