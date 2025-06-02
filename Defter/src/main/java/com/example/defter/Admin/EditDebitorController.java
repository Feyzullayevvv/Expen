package com.example.defter.Admin;

import com.example.defter.Model.Debitor;
import com.example.defter.RoundUp;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class EditDebitorController {
    private  AdminClient client;

    @FXML
    private TextField borcTextField;
    @FXML
    private TextField debitorTextField;
    @FXML
    private TextField nomreTextField;
    private Debitor debitor;


    public void init(AdminClient client, Debitor debitor){
        this.debitor=debitor;
        this.client=client;
        debitorTextField.setText(debitor.getAd());
        nomreTextField.setText(debitor.getNomre());
        borcTextField.setText(RoundUp.roundUp(debitor.getBorc()));
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
        client.sendMessage("EDITDEBITOR");
        client.sendMessage(String.valueOf(debitor.getId()));
        client.sendMessage(debitorTextField.getText());
        client.sendMessage(nomreTextField.getText());
        client.sendMessage(borcTextField.getText());
    }
}
