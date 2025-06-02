package com.example.defter.Admin;

import com.example.defter.Model.User;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class UserEditController {
    private  AdminClient client;
    private User user;
    @FXML
    private TextField userNameTextField;
    @FXML
    private TextField passwordTextField;


    public void init(AdminClient client, User user){
        this.client=client;
        this.user=user;
        userNameTextField.setText(user.getName());
        passwordTextField.setText(user.getPassword());
    }
    public void updateUser(){
        client.sendMessage("UPDATEUSERINFO");
        client.sendMessage(String.valueOf(user.getId()));
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
