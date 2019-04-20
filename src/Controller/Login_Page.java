package Controller;

import Model.Login;
import Unit.Instructor;
import Unit.Student;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class Login_Page extends Application implements Initializable {
    public TextField username_field;
    public PasswordField password_field;
    public Button login_button;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../View/login.fxml"));
        stage.setTitle("Grading System");
        stage.setScene(new Scene(root, 330, 206));
        stage.show();


    }

    public void loginAction() throws IOException {
        if (Login.login(username_field.getText(),password_field.getText())){
            Stage prev = (Stage) username_field.getScene().getWindow();
            if (Login.currentUser instanceof Student){
                Pane newPane = FXMLLoader.load(getClass().getResource("../View/student_panel.fxml"));
                Scene scene = new Scene(newPane);
                Stage newStage = new Stage();
                newStage.setScene(scene);
                newStage.setTitle("Student Panel");
                newPane.setPrefSize(800,600);
                newStage.show();
                prev.close();

            }else if (Login.currentUser instanceof Instructor) {
                if (((Instructor) Login.currentUser).isAdmin()){
                    Pane newPane = FXMLLoader.load(getClass().getResource("../View/admin_panel.fxml"));
                    Scene scene = new Scene(newPane);
                    Stage newStage = new Stage();
                    newStage.setScene(scene);
                    newStage.setTitle("Admin Panel");
                    newPane.setPrefSize(800,600);
                    newStage.show();
                    prev.close();
                }else {
                    Pane newPane = FXMLLoader.load(getClass().getResource("../View/instructor_panel.fxml"));
                    Scene scene = new Scene(newPane);
                    Stage newStage = new Stage();
                    newStage.setScene(scene);
                    newStage.setTitle("Instructor Panel");
                    newPane.setPrefSize(800,600);
                    newStage.show();
                    prev.close();
                }
            }
        }else{
            new Alert(Alert.AlertType.ERROR,"Login Failed",ButtonType.OK).showAndWait();
        }

    }


}
