package Unit;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Grade {
    private SimpleStringProperty grade;
    private SimpleIntegerProperty point;
    private SimpleStringProperty course_id;
    private SimpleStringProperty course_name;
    private SimpleIntegerProperty student_id;
    private SimpleStringProperty student_name;
    private Course course;

    public Grade(Course course, int point) {
        this.point = new SimpleIntegerProperty(point);
        this.course = course;
        this.course_name = new SimpleStringProperty(course.getCourse_name());
        this.course_id = new SimpleStringProperty(course.getCourse_id());
        this.grade = new SimpleStringProperty(pointToLetter(point));
    }
    public Grade(Course course, int point, int student_id) {
        this.student_id = new SimpleIntegerProperty(student_id);
        this.point = new SimpleIntegerProperty(point);
        this.course = course;
        this.course_name = new SimpleStringProperty(course.getCourse_name());
        this.course_id = new SimpleStringProperty(course.getCourse_id());
        this.grade = new SimpleStringProperty(pointToLetter(point));
        this.student_name = new SimpleStringProperty(Student.getStudentById(student_id).getFullname());
    }

    public static String pointToLetter(int point){
        if (point == -1) return "-";
        else if (0 <= point && point < 20) return "F";
        else if (point < 40) return "D";
        else if (point < 60) return "C";
        else if (point < 80) return "B";
        else if (point <= 100) return "A";
        else return "ERROR";
    }

    public String getGrade() {
        return grade.get();
    }

    public SimpleStringProperty gradeProperty() {
        return grade;
    }

    public int getPoint() {
        if (point.get()==-1) return 0;
        return point.get();
    }

    public SimpleIntegerProperty pointProperty() {
        return point;
    }

    public String getCourse_id() {
        return course_id.get();
    }

    public SimpleStringProperty course_idProperty() {
        return course_id;
    }

    public String getCourse_name() {
        return course_name.get();
    }

    public SimpleStringProperty course_nameProperty() {
        return course_name;
    }

    public Course getCourse() {
        return course;
    }

    public void setGrade(String grade) {
        this.grade.set(grade);
    }

    public void setPoint(int point) {
        this.point.set(point);
    }

    public void setCourse_id(String course_id) {
        this.course_id.set(course_id);
    }

    public void setCourse_name(String course_name) {
        this.course_name.set(course_name);
    }

    public int getStudent_id() {
        return student_id.get();
    }

    public SimpleIntegerProperty student_idProperty() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id.set(student_id);
    }

    public String getStudent_name() {
        return student_name.get();
    }

    public SimpleStringProperty student_nameProperty() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name.set(student_name);
    }
}
