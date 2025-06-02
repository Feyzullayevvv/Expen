package com.example.defter.Admin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class AdminClient  extends Application {
    private BufferedReader reader;
    private PrintWriter writer;
    private  ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    @Override
    public void start(Stage stage) throws Exception {
        Socket socket = new Socket("localhost", 4444);
        writer = new PrintWriter(socket.getOutputStream(),true);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        objectInputStream = new ObjectInputStream(socket.getInputStream());
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/defter/AdminFXML/AdminLogin.fxml"));

        Scene scene = new Scene(fxmlLoader.load());
        LoginController controller= fxmlLoader.getController();
        controller.setClient(this);
        stage.setTitle("ExPen");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    public  synchronized void  sendMessage(String message) {
        writer.flush();
        writer.println(message);
        writer.flush();
    }

    public synchronized List<?> objectReader(){
        try {
            return (List<?>) objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public synchronized  Object itemReader(){
        try {
            return  objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public synchronized String reader(){
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public  void sendListResponse(List<?> List) {
        try {
            objectOutputStream.reset();
            objectOutputStream.writeObject(List);
            objectOutputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }


}
