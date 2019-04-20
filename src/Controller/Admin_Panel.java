package Controller;

import Model.Record;
import Unit.Instructor;
import Unit.Student;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sun.font.ScriptRun;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;

import static Controller.EditUserDialog.*;
import static javax.security.auth.callback.ConfirmationCallback.YES;

public class Admin_Panel extends Application implements Initializable {

    @FXML
    public TableColumn<Instructor, String> email_instructor;
    public TableColumn<Instructor, String> tel_instructor;
    public TableColumn<Instructor, Integer> id_instructor;
    public TableColumn<Instructor, String> name_instructor;
    public Button btn_add_instructor;
    public Button btn_edit_instructor;
    public Button btn_delete_instructor;
    public TableColumn<Student, Integer> id_student;
    public TableColumn<Student, String> name_student;
    public TableColumn<Student, String> email_student;
    public TableColumn<Student, String> tel_student;
    public Button btn_add_student;
    public Button btn_delete_student;
    public Button btn_edit_student;

    @FXML
    private TableView<Instructor> table_instructors;
    @FXML
    private TableView<Student> table_students;
    public ObservableList<Instructor> instructor_data;
    public ObservableList<Student> student_data;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        id_instructor.setCellValueFactory(new PropertyValueFactory<>("instructor_id"));
        name_instructor.setCellValueFactory(new PropertyValueFactory<>("full_name"));
        email_instructor.setCellValueFactory(new PropertyValueFactory<>("email"));
        tel_instructor.setCellValueFactory(new PropertyValueFactory<>("telephone"));

        instructor_data = FXCollections.observableArrayList();
        table_instructors.setItems(instructor_data);
        updateInstructorsTableContent();

        id_student.setCellValueFactory(new PropertyValueFactory<>("student_id"));
        name_student.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        email_student.setCellValueFactory(new PropertyValueFactory<>("email"));
        tel_student.setCellValueFactory(new PropertyValueFactory<>("telephone"));

        student_data = FXCollections.observableArrayList();
        table_students.setItems(student_data);
        updateStudentsTableContent();
    }
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../View/admin_panel.fxml"));
        stage.setTitle("Admin Panel - Grading System");
        stage.setScene(new Scene(root, 800, 400));
        stage.show();

    }

    public void updateInstructorsTableContent(){
        table_instructors.getItems().clear();
        Connection conn = DBUtil.connect();
        String query = "SELECT * FROM instructors";
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                System.out.println(rs.getString(4));
                instructor_data.add(new Instructor(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getString(7),
                        false));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        table_instructors.setItems(instructor_data);
    }

    private void updateStudentsTableContent(){
        table_students.getItems().clear();
        Connection conn = DBUtil.connect();
        String query = "SELECT * FROM students";
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                student_data.add(new Student(
                        rs.getInt(1),rs.getString(2),rs.getString(3)
                        ,rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7)));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        table_students.setItems(student_data);
    }

    public void addInstructorAction() throws IOException {
        Pane myPane = FXMLLoader.load(getClass().getResource("../View/edit_user_dialog.fxml"));
        Scene scene = new Scene(myPane);
        Stage stage = new Stage();
        stage.setTitle("Add Instructor");
        SwingNode node_operation = (SwingNode) myPane.lookup("#node_operation");
        SwingNode instructor_id_input = (SwingNode) myPane.lookup("#node_id");
        instructor_id_input.setUserData(0);
        node_operation.setUserData(ADD_INSTRUCTOR);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();
        updateInstructorsTableContent();
    }

    public void editInstructorAction() throws IOException {
        Pane myPane = FXMLLoader.load(getClass().getResource("../View/edit_user_dialog.fxml"));
        Scene scene = new Scene(myPane);
        Stage stage = new Stage();
        stage.setTitle("Edit Instructor");
        SwingNode instructor_id_input = (SwingNode) myPane.lookup("#node_id");
        SwingNode node_operation = (SwingNode) myPane.lookup("#node_operation");
        TextField user_name_input = (TextField) myPane.lookup("#text_field_user_name");
        PasswordField password_input = (PasswordField) myPane.lookup("#password_field_password");
        TextField first_name_input = (TextField) myPane.lookup("#text_field_first_name");
        TextField last_name_field = (TextField) myPane.lookup("#text_field_last_name");
        TextField email_field = (TextField) myPane.lookup("#text_field_e_mail");
        TextField telephone_field = (TextField) myPane.lookup("#text_field_telephone");
        TextField user_id_input = (TextField) myPane.lookup("#text_field_id");

        Instructor selected = table_instructors.getFocusModel().getFocusedItem();

        user_id_input.setText(String.valueOf(selected.getInstructor_id()));
        user_name_input.setText(selected.getUsername());
        password_input.setText(selected.getPassword());
        first_name_input.setText(selected.getFirstname());
        last_name_field.setText(selected.getLastname());
        email_field.setText(selected.getEmail());
        telephone_field.setText(selected.getTelephone());

        instructor_id_input.setUserData(selected.getInstructor_id());

        node_operation.setUserData(EDIT_INSTRUCTOR);

        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();
        updateInstructorsTableContent();
    }

    public void deleteInstructorAction(){
        Instructor selected = table_instructors.getFocusModel().getFocusedItem();
        Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure you want to delete "+selected.getFull_name(),ButtonType.YES,ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES){
            Record.deleteInstructor(selected.getInstructor_id());
        } else {
            // Nothing
        }
        updateInstructorsTableContent();
    }

    public void addStudentAction() throws IOException {
        Pane myPane = FXMLLoader.load(getClass().getResource("../View/edit_user_dialog.fxml"));
        Scene scene = new Scene(myPane);
        Stage stage = new Stage();
        stage.setTitle("Add Student");
        SwingNode node_operation = (SwingNode) myPane.lookup("#node_operation");
        SwingNode instructor_id_input = (SwingNode) myPane.lookup("#node_id");
        instructor_id_input.setUserData(0);
        node_operation.setUserData(ADD_STUDENT);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();
        updateStudentsTableContent();
    }

    public void editStudentAction() throws IOException {
        Pane myPane = FXMLLoader.load(getClass().getResource("../View/edit_user_dialog.fxml"));
        Scene scene = new Scene(myPane);
        Stage stage = new Stage();
        stage.setTitle("Edit Student");
        SwingNode student_id_input = (SwingNode) myPane.lookup("#node_id");
        SwingNode node_operation = (SwingNode) myPane.lookup("#node_operation");
        TextField user_id_input = (TextField) myPane.lookup("#text_field_id");
        TextField user_name_input = (TextField) myPane.lookup("#text_field_user_name");
        PasswordField password_input = (PasswordField) myPane.lookup("#password_field_password");
        TextField first_name_input = (TextField) myPane.lookup("#text_field_first_name");
        TextField last_name_field = (TextField) myPane.lookup("#text_field_last_name");
        TextField email_field = (TextField) myPane.lookup("#text_field_e_mail");
        TextField telephone_field = (TextField) myPane.lookup("#text_field_telephone");

        Student selected = table_students.getFocusModel().getFocusedItem();

        user_id_input.setText(String.valueOf(selected.getStudent_id()));
        user_name_input.setText(selected.getUsername());
        password_input.setText(selected.getPassword());
        first_name_input.setText(selected.getFirstname());
        last_name_field.setText(selected.getLastname());
        email_field.setText(selected.getEmail());
        telephone_field.setText(selected.getTelephone());

        student_id_input.setUserData(selected.getStudent_id());
        node_operation.setUserData(EDIT_STUDENT);

        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();
        updateStudentsTableContent();
    }

    public void deleteStudentAction(){
        Student selected = table_students.getFocusModel().getFocusedItem();
        Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure you want to delete "+selected.getFullname(),ButtonType.YES,ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES){
            Record.deleteStudent(selected.getStudent_id());
        } else {
            // Nothing
        }
        updateStudentsTableContent();
    }
}
