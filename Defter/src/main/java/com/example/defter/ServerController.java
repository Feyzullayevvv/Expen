package com.example.defter;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ServerController  extends Application {
    @FXML
    private ToggleButton toggleButton;
    private Thread serverThread;

//    public void showLoggedINUI(){
//        try {
//
//            FXMLLoader fxmlLoader = new FXMLLoader();
//            fxmlLoader.setLocation(getClass().getResource("/com/example/serverclienthicaz/Server.fxml"));
//            Scene scene = new Scene(fxmlLoader.load(),161,95);
//            fxmlLoader.setController(this);
//            Stage stage = new Stage();
//            stage.setResizable(true);
//            stage.setTitle("SERVER");
//            stage.setScene(scene);
//            stage.show();
//
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//
//
//    }


    public void startServer() {
        serverThread = new Thread(() -> {
            Server server = new Server();
            server.start(4455);
        });
        serverThread.setDaemon(true);
        serverThread.start();

        toggleButton.setDisable(true);

    }

    public void stopServer() {
        System.exit(0);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/defter/Server.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}





