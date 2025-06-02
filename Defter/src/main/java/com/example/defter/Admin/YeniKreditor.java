package com.example.defter.Admin;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class YeniKreditor {
    private  AdminClient client;

    @FXML
    private TextField borcTextField;
    @FXML
    private TextField kreditorTextField;


    public void init(AdminClient client){
        this.client=client;
        borcTextField.setText("0");
    }

    public TextField getBorcTextField() {
        return borcTextField;
    }

    public TextField getKreditorTextField() {
        return kreditorTextField;
    }

    public void insertDebitor(){
        client.sendMessage("INSERTKREDITOR");
        client.sendMessage(kreditorTextField.getText());
        client.sendMessage(borcTextField.getText());
    }
}
