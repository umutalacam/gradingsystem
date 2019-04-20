package Controller;
import Unit.Course;
import Unit.Grade;
import Unit.Instructor;
import Unit.Student;
import Util.DBUtil;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class GradingPanel extends Application implements Initializable {

    public TableView<Grade> table_grades;
    public TableColumn<Grade, Integer> col_student_id;
    public TableColumn<Grade, String> col_student_name;
    public TableColumn<Grade, Integer> col_point;
    public Button btn_ok;
    public Button btn_change_grade;
    private ObservableList<Grade> grades;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        col_student_id.setCellValueFactory(new PropertyValueFactory<>("student_id"));
        col_student_name.setCellValueFactory(new PropertyValueFactory<>("student_name"));
        col_point.setCellValueFactory(new PropertyValueFactory<>("point"));
        grades = FXCollections.observableArrayList();
        table_grades.setItems(grades);
        loadGrades();

        table_grades.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() >=2 ){
                    Grade selected = table_grades.getFocusModel().getFocusedItem();
                    TextInputDialog dialog = new TextInputDialog(Integer.toString(selected.getPoint()));
                    dialog.setTitle("Edit Point");
                    dialog.setContentText(selected.getStudent_name()+": ");
                    Optional<String> result = dialog.showAndWait();
                    if (result.isPresent()){
                        try {
                            int point = Integer.parseInt(dialog.getEditor().getText());
                            Student.updateGrade(selected.getStudent_id(),
                                    selected.getCourse_id(),
                                    point);
                            loadGrades();
                        }catch (Exception e){
                            new Alert(Alert.AlertType.ERROR,"Please enter a number").showAndWait();
                        }
                    }
                }
            }
        });

    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../View/grading_panel.fxml"));
        stage.setTitle("Grading Panel");
        stage.setScene(new Scene(root, 600, 680));
        stage.setResizable(true);
        stage.show();

    }

    public void loadGrades(){
        table_grades.getItems().clear();
        String course_id = InstructorPanel.selected_course_id;
        Connection conn = DBUtil.connect();
        String sql = "SELECT * FROM grades WHERE course_id = ?";
        System.out.println("Course id: "+ course_id);
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1,course_id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                grades.add(new Grade(Course.getCourseById(course_id),rs.getInt(3),rs.getInt(1)));
            }
            table_grades.setItems(grades);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}