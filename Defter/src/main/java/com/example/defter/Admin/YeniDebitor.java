package com.example.defter.Admin;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class YeniDebitor {
    private  AdminClient client;

    @FXML
    private TextField borcTextField;
    @FXML
    private TextField debitorTextField;
    @FXML
    private TextField nomreTextField;


    public void init(AdminClient client){
        this.client=client;
        borcTextField.setText("0");
    }

    public TextField getBorcTextField() {
        return borcTextField;
    }

    public TextField getDebitorTextField() {
        return debitorTextField;
    }

    public TextField getNomreTextField() {
        return nomreTextField;
    }

    public void insertDebitor(){
        client.sendMessage("INSERTDEBITOR");
        client.sendMessage(debitorTextField.getText());
        client.sendMessage(nomreTextField.getText());
        client.sendMessage(borcTextField.getText());
    }
}
