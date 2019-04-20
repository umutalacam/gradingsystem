package Controller;

import Model.Login;
import Unit.Course;
import Unit.Grade;
import Unit.Student;
import Util.DBUtil;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class StudentPanel extends Application implements Initializable {

    //Your grades tab
    public TableView<Grade> table_your_grades;
    public TableColumn<Grade, Integer> col_grades_course_id;
    public TableColumn<Grade, String> col_grades_course_name;
    public TableColumn<Grade, String> col_grades_grade;
    public PieChart grading_chart;
    private ObservableList<Grade> grades_list;

    //Course enrollment tab
    public TableView<Course> table_courses;
    public TableColumn<Course, Integer> col_course_id;
    public TableColumn<Course, String> col_instructor;
    public TableColumn<Course, String> col_course_name;
    public TableColumn<Course, Integer> col_quota;
    private ObservableList<Course> courses_list;

    public Button btn_enroll_course;
    public ListView<String> selected_courses;
    private ObservableList<String> course_dumps;

    public Student currentStudent = (Student) Login.currentUser;;
    public int currentStudentId=currentStudent.getStudent_id();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Selected Courses ListView
        course_dumps = FXCollections.observableArrayList();
        selected_courses.setItems(course_dumps);

        //Grades are later
        col_grades_course_id.setCellValueFactory(new PropertyValueFactory<>("course_id"));
        col_grades_course_name.setCellValueFactory(new PropertyValueFactory<>("course_name"));
        col_grades_grade.setCellValueFactory(new PropertyValueFactory<>("grade"));
        grades_list = FXCollections.observableArrayList();
        table_your_grades.setItems(grades_list);
        updateGradesTable();

        //Avaible Courses Table
        col_course_id.setCellValueFactory(new PropertyValueFactory<>("course_id"));
        col_instructor.setCellValueFactory(new PropertyValueFactory<>("instructorName"));
        col_course_name.setCellValueFactory(new PropertyValueFactory<>("course_name"));
        col_quota.setCellValueFactory(new PropertyValueFactory<>("avaible_quota"));
        courses_list = FXCollections.observableArrayList();
        table_courses.setItems(courses_list);
        updateCoursesTable();

        table_your_grades.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Grade selected = table_your_grades.getFocusModel().getFocusedItem();
                loadChart(selected);
            }
        });

    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../View/student_panel.fxml"));
        stage.setTitle("Student Panel");
        stage.setScene(new Scene(root, 640, 480));
        stage.setResizable(true);
        stage.show();



    }

    public void updateCoursesTable(){
        Connection connection = DBUtil.connect();
        table_courses.getItems().clear();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM courses");
            ResultSet resultSet = stmt.executeQuery();
            courses_list.clear();
            while (resultSet.next()){
                courses_list.add(new Course(resultSet.getInt(4),
                        resultSet.getString(2),
                        resultSet.getInt(3),
                        resultSet.getString(1),
                        resultSet.getInt(5)));
            }
            table_courses.setItems(courses_list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateGradesTable(){
        Connection connection = DBUtil.connect();
        table_your_grades.getItems().clear();
        selected_courses.getItems().clear();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM grades WHERE student_id=?");
            stmt.setInt(1,currentStudent.getStudent_id());
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()){
                Course course = Course.getCourseById(resultSet.getString(2));
                int point = resultSet.getInt(3);
                grades_list.add(new Grade(course,point));
                course_dumps.add(course.getDump());
            }
            table_courses.setItems(courses_list);
            selected_courses.setItems(course_dumps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void loadChart(Grade grade){
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        int noA, noB, noC, noD, noF;
        noA = noB = noC = noD = noF = 0;
        String course_id = grade.getCourse_id();
        Connection conn = DBUtil.connect();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement("SELECT * FROM `grades` WHERE `course_id` = ?");
            stmt.setString(1,course_id);
            rs = stmt.executeQuery();
            int point;
            String grade_str = "";
            while (rs.next()){
                point = rs.getInt(3);
                grade_str = Grade.pointToLetter(point);
                switch (grade_str){
                    case "A":
                        noA++;
                        break;
                    case "B":
                        noB++;
                        break;
                    case "C":
                        noC++;
                        break;
                    case "D":
                        noD++;
                        break;
                    case "F":
                        noF++;
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        pieChartData.add(new PieChart.Data("A",noA));
        pieChartData.add(new PieChart.Data("B",noB));
        pieChartData.add(new PieChart.Data("C",noC));
        pieChartData.add(new PieChart.Data("D",noD));
        pieChartData.add(new PieChart.Data("F",noF));
        grading_chart.setData(pieChartData);
    }

    public void enrollCourseAction(){
        Course selected = table_courses.getFocusModel().getFocusedItem();
        String selected_course_id = selected.getCourse_id();
        boolean check = Student.updateGrade(currentStudentId,selected_course_id,-1);
        updateGradesTable();
        updateCoursesTable();
    }


}
