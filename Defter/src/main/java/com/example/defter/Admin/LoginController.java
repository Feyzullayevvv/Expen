package com.example.defter.Admin;

import com.example.defter.Model.User;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoginController {
    @FXML
    private TextField userName;
    @FXML
    private PasswordField passwordField;
    private  AdminClient client;
    List<User> userList = new ArrayList<>();



    public void setClient(AdminClient client) {
        this.client = client;
    }

    public void signIn() throws IOException {
        userList.clear();
        client.sendMessage("GETUSERLIST");
        userList = (List<User>) client.objectReader();
        for (User user : userList){
            if (user.getName().equals(userName.getText()) && user.getPassword().equals(passwordField.getText())){
                Stage loginstage = (Stage) userName.getScene().getWindow();
                loginstage.close();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/defter/AdminFXML/MainPage.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                MainController controller = fxmlLoader.getController();
                controller.setClient(client);
                stage.setTitle("ExPen");
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
            }
        }
//        Stage loginstage = (Stage) userName.getScene().getWindow();
//        loginstage.close();
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/defter/AdminFXML/MainPage.fxml"));
//        Scene scene = new Scene(fxmlLoader.load());
//        Stage stage = new Stage();
//        MainController controller = fxmlLoader.getController();
//        controller.setClient(client);
//        stage.setTitle("ExPen");
//        stage.setScene(scene);
//        stage.setResizable(false);
//        stage.show();
        applyShakeAnimation(passwordField);
        applyShakeAnimation(userName);
    }
    private void applyShakeAnimation(TextField field) {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(0), new KeyValue(field.translateXProperty(), 0)),
                new KeyFrame(Duration.millis(100), new KeyValue(field.translateXProperty(), -10)),
                new KeyFrame(Duration.millis(200), new KeyValue(field.translateXProperty(), 10)),
                new KeyFrame(Duration.millis(300), new KeyValue(field.translateXProperty(), -10)),
                new KeyFrame(Duration.millis(400), new KeyValue(field.translateXProperty(), 10)),
                new KeyFrame(Duration.millis(500), new KeyValue(field.translateXProperty(), 0))
        );
        timeline.play();
        timeline.setOnFinished(event -> field.getStyleClass().remove("shake"));
        field.getStyleClass().add("shake");
    }
}
