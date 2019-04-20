package Controller;

import Model.Record;
import Unit.Course;
import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class EditCourseDialog extends Application {

    public TextField field_course_name;
    public TextField field_quota;
    public TextField field_course_id;

    public Node node_operation;
    public Node node_instructor_id;
    public Node node_course_id;

    //Operation codes:
    final static int ADD_COURSE = 0;
    final static int EDIT_COURSE = 1;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../View/edit_course_dialog.fxml"));
        stage.setTitle("Add Course");
        stage.setScene(new Scene(root, 390, 190));
        stage.setResizable(false);
        stage.show();
        node_instructor_id = new SwingNode();
        node_operation = new SwingNode();
        node_instructor_id.setUserData(0);
        node_operation.setUserData(ADD_COURSE);
    }


    public void applyAction(){
        int operation = (int) node_operation.getUserData();
        int instructor_id = (int) node_instructor_id.getUserData();
        String old_course_id = (String) node_course_id.getUserData();

        String course_id_input = field_course_id.getText();
        String course_name_input = field_course_name.getText();
        String quota_input = field_quota.getText();
        int quota;
        if (course_name_input.equals("") || quota_input.equals("")){
            new Alert(Alert.AlertType.ERROR,"Please fill all fields.").showAndWait();
            return;
        }
        try {
            quota = Integer.parseInt(quota_input);
        }catch (Exception e){
            new Alert(Alert.AlertType.ERROR,"Course quota have to be a number").showAndWait();
            return;
        }
        Stage stage = (Stage) field_course_name.getScene().getWindow();
        switch (operation){
            case ADD_COURSE:
                System.out.println("Adding course by instructor id: "+ instructor_id);
                int check = Record.addCourse(new Course(instructor_id,course_name_input,quota,course_id_input));
                if (check == 0){
                    new Alert(Alert.AlertType.ERROR,"Couldn't add course!").showAndWait();
                }
                stage.close();
                break;
            case EDIT_COURSE:
                System.out.println("Editing course by instructor id: "+ instructor_id+" Course id: "+ course_id_input);
                check = Record.editCourse(new Course(instructor_id, course_name_input, quota, old_course_id), course_id_input);
                if (check == 0){
                    new Alert(Alert.AlertType.ERROR,"Couldn't edit course!").showAndWait();
                }
                stage.close();
                break;
        }
    }

    public void cancelAction(){
        Stage stage = (Stage) field_course_name.getScene().getWindow();
        stage.close();
    }

}
