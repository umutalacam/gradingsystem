package Controller;

import Model.Login;
import Unit.Course;
import Unit.Instructor;
import Util.DBUtil;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sun.rmi.runtime.Log;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.function.LongFunction;
import java.util.function.LongToIntFunction;

import static Controller.EditCourseDialog.ADD_COURSE;
import static Controller.EditCourseDialog.EDIT_COURSE;
import static Controller.EditUserDialog.ADD_STUDENT;

public class InstructorPanel extends Application implements Initializable {


    //Own Courses Table
    @FXML
    private TableView<Course> table_own_courses;
    public TableColumn<Course, Integer> col_own_course_id;
    public TableColumn<Course, String> col_own_course_name;
    public TableColumn<Course, Integer> col_own_quota;

    //Own Courses Buttons
    public Button btn_create_new_course;
    public Button btn_show_course_grades;
    public Button btn_remove_course;
    public Button btn_edit_course;

    //All Courses Table Just For Information
    @FXML
    private TableView<Course> table_all_courses;
    public TableColumn<Course, Integer> col_course_id;
    public TableColumn<Course, String> col_instructor;
    public TableColumn<Course, String> col_course_name;
    public TableColumn<Course, Integer> col_quota;

    private ObservableList<Course> own_courses_list;
    private ObservableList<Course> all_courses_list;

    public static Instructor currentInstructor;
    public static void main(String[] args) {
        launch(args);
    }
    public static String selected_course_id="";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentInstructor = (Instructor) Login.currentUser;

        col_own_course_id.setCellValueFactory(new PropertyValueFactory<>("course_id"));
        col_own_course_name.setCellValueFactory(new PropertyValueFactory<>("course_name"));
        col_own_quota.setCellValueFactory(new PropertyValueFactory<>("avaible_quota"));

        own_courses_list = FXCollections.observableArrayList();
        table_own_courses.setItems(own_courses_list);
        updateOwnCoursesTable();

        col_course_id.setCellValueFactory(new PropertyValueFactory<>("course_id"));
        col_instructor.setCellValueFactory(new PropertyValueFactory<>("instructorName"));
        col_course_name.setCellValueFactory(new PropertyValueFactory<>("course_name"));
        col_quota.setCellValueFactory(new PropertyValueFactory<>("avaible_quota"));
        all_courses_list = FXCollections.observableArrayList();
        table_all_courses.setItems(all_courses_list);
        updateAllCoursesTable();
    }

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../View/instructor_panel.fxml"));
        stage.setTitle("Instructor Panel");
        stage.setScene(new Scene(root, 600, 480));
        stage.setResizable(true);
        stage.show();
    }

    public void updateOwnCoursesTable(){
        Connection connection = DBUtil.connect();
        System.out.println("Update own courses");
        table_own_courses.getItems().clear();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM courses WHERE INSTRUCTOR_ID = ?");
            stmt.setInt(1, currentInstructor.getInstructor_id());
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()){
                System.out.println(resultSet.getString(2));
                own_courses_list.add(new Course(resultSet.getInt(4),
                        resultSet.getString(2),
                        resultSet.getInt(3),
                        resultSet.getString(1),
                        resultSet.getInt(5)));
            }
            table_own_courses.setItems(own_courses_list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateAllCoursesTable(){
        Connection connection = DBUtil.connect();
        table_all_courses.getItems().clear();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM courses");
            ResultSet resultSet = stmt.executeQuery();
            all_courses_list.clear();
            while (resultSet.next()){
                all_courses_list.add(new Course(resultSet.getInt(4),
                        resultSet.getString(2),
                        resultSet.getInt(3),
                        resultSet.getString(1),
                        resultSet.getInt(5))
                );
            }
            table_all_courses.setItems(all_courses_list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createNewCourseAction() throws IOException {
        Pane myPane = FXMLLoader.load(getClass().getResource("../View/edit_course_dialog.fxml"));
        Scene scene = new Scene(myPane);
        Stage stage = new Stage();
        stage.setTitle("Add Course");
        SwingNode node_operation = (SwingNode) myPane.lookup("#node_operation");
        SwingNode instructor_id_input = (SwingNode) myPane.lookup("#node_instructor_id");
        System.out.println("Current User Id:" + currentInstructor.getInstructor_id());
        instructor_id_input.setUserData(currentInstructor.getInstructor_id());
        node_operation.setUserData(ADD_COURSE);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();
        updateOwnCoursesTable();
        updateAllCoursesTable();
    }

    public void editCourseAction() throws IOException {
        Pane myPane = FXMLLoader.load(getClass().getResource("../View/edit_course_dialog.fxml"));
        Scene scene = new Scene(myPane);
        Stage stage = new Stage();
        stage.setTitle("Edit Course");

        //Get user fields
        TextField field_course_name = (TextField) myPane.lookup("#field_course_name");
        TextField field_quota = (TextField) myPane.lookup("#field_quota");
        TextField field_course_id = (TextField) myPane.lookup("#field_course_id");

        //Selected course
        Course selected = table_own_courses.getFocusModel().getFocusedItem();
        field_course_name.setText(selected.getCourse_name());
        field_quota.setText(Integer.toString(selected.getQuota()));
        field_course_id.setText(selected.getCourse_id());

        //Get data nodes
        SwingNode node_operation = (SwingNode) myPane.lookup("#node_operation");
        SwingNode node_instructor_id = (SwingNode) myPane.lookup("#node_instructor_id");
        SwingNode node_course_id = (SwingNode) myPane.lookup("#node_course_id");

        node_instructor_id.setUserData(currentInstructor.getInstructor_id());
        node_operation.setUserData(EDIT_COURSE);
        node_course_id.setUserData(selected.getCourse_id());

        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();
        updateOwnCoursesTable();
        updateAllCoursesTable();
    }

    public void showCourseGradesAction() throws IOException {
        Course selected = table_own_courses.getFocusModel().getFocusedItem();
        selected_course_id = selected.getCourse_id();
        Pane myPane = FXMLLoader.load(getClass().getResource("../View/grading_panel.fxml"));
        Scene scene = new Scene(myPane);
        Stage stage = new Stage();
        stage.setTitle(selected.getCourse_name() + " grades");

        stage.setScene(scene);
        stage.showAndWait();

    }

    public void removeCourseAction(){

    }

}

