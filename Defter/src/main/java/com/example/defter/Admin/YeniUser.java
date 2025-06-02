package com.example.defter.Admin;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class YeniUser {
    private AdminClient client;

    @FXML
    private TextField userNameTextField;
    @FXML
    private TextField passwordTextField;

    public void init(AdminClient client){
        this.client=client;
    }

    public void insertUser(){
        client.sendMessage("INSERTUSER");
        client.sendMessage(userNameTextField.getText());
        client.sendMessage(passwordTextField.getText());
    }
    public TextField getUserNameTextField() {
        return userNameTextField;
    }

    public TextField getPasswordTextField() {
        return passwordTextField;
    }
}
