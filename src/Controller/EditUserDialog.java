package Controller;

import Model.Record;
import Unit.Instructor;
import Unit.Student;
import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EditUserDialog extends Application implements Initializable {

    public TextField text_field_user_name;
    public PasswordField password_field_password;
    public TextField text_field_first_name;
    public TextField text_field_last_name;
    public TextField text_field_e_mail;
    public TextField text_field_telephone;
    public TextField text_field_id;
    public Button btn_add_instructor;
    public SwingNode node_operation;
    public SwingNode node_id;

    public final static int ADD_INSTRUCTOR = 0;
    public final static int EDIT_INSTRUCTOR = 1;
    public final static int ADD_STUDENT = 2;
    public final static int EDIT_STUDENT = 3;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../View/edit_user_dialog.fxml"));
        stage.setTitle("Add Instructor");
        stage.setScene(new Scene(root, 390, 255));
        stage.setResizable(false);
        stage.show();
        node_id = new SwingNode();
        node_id.setUserData(0);
        node_operation = new SwingNode();
        node_operation.setUserData(ADD_INSTRUCTOR);
    }

    public void applyAction() throws Throwable {
        int user_id_input = 0;
        String field_user_id_input = text_field_id.getText();

        try{
           user_id_input = Integer.parseInt(field_user_id_input);
        }catch (Exception e){
            new Alert(Alert.AlertType.ERROR,"User id should be a number");
        }

        int old_id = (int) node_id.getUserData();


        String user_name_input = text_field_user_name.getText();
        String password_input = password_field_password.getText();
        String first_name_input = text_field_first_name.getText();
        String last_name_input = text_field_last_name.getText();
        String e_mail_input = text_field_e_mail.getText();
        String telephone_input = text_field_telephone.getText();
        boolean isAdmin = false;
        if (user_name_input.equals("")||
            password_input.equals("")||
            first_name_input.equals("")||
            last_name_input.equals("")||
            e_mail_input.equals("")||
            telephone_input.equals("")){
            new Alert(Alert.AlertType.ERROR,"All fields should be filled!",ButtonType.OK).showAndWait();
        }else {
            //Check operations
            int operation = (int) node_operation.getUserData();
            Instructor newInstructor;

            switch (operation) {
                case ADD_INSTRUCTOR:
                    newInstructor  = new Instructor(user_id_input, user_name_input, password_input, first_name_input, last_name_input, e_mail_input, telephone_input, isAdmin);
                    int check = Record.addInstructor(newInstructor);
                    if (check == 0) {

                    } else {
                        Stage stage = (Stage) btn_add_instructor.getScene().getWindow();
                        stage.close();
                    }
                    break;
                case EDIT_INSTRUCTOR:
                    //Edit Instructor
                    System.out.println("Edit Instructor");
                    newInstructor  = new Instructor(old_id, user_name_input, password_input, first_name_input, last_name_input, e_mail_input, telephone_input, isAdmin);
                    check = Record.editInstructor(newInstructor, user_id_input);
                    if (check == 0) {
                        //new Alert(Alert.AlertType.ERROR, "User can't be edited").showAndWait();
                    } else {
                        Stage stage = (Stage) btn_add_instructor.getScene().getWindow();
                        stage.close();
                    }
                    break;
                case ADD_STUDENT:
                    //Add student
                    check = Record.addStudent(new Student(user_id_input, user_name_input, password_input,
                            first_name_input, last_name_input, e_mail_input, telephone_input));
                    if (check == 0) {
                        //new Alert(Alert.AlertType.ERROR, "Couldn't add student!").showAndWait();
                    } else {
                        Stage stage = (Stage) btn_add_instructor.getScene().getWindow();
                        stage.close();
                    }
                    break;
                case EDIT_STUDENT:
                    //Edit Student
                    System.out.println("Edit Student");
                    check = Record.editStudent(new Student((int)node_id.getUserData(),user_name_input,
                            password_input,first_name_input,last_name_input,e_mail_input,telephone_input),user_id_input);
                    if (check == 0) {
                        //new Alert(Alert.AlertType.ERROR, "Student can't be edited").showAndWait();
                    } else {
                        Stage stage = (Stage) btn_add_instructor.getScene().getWindow();
                        stage.close();
                    }
                    break;
            }
        }
    }

    public void cancelAction(){
        Stage stage = (Stage) btn_add_instructor.getScene().getWindow();
        stage.close();
    }




}
