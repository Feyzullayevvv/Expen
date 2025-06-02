package com.example.defter.Admin;

import com.example.defter.Model.Mal;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class MalSiyahiDialog {
    private AdminClient client;
    private Mal mal;

    @FXML
    private TextField barcodeTextField;
    @FXML
    private TextField kodTextField;
    @FXML
    private TextField adTextField;
    @FXML
    private TextField mayaTextField;
    @FXML
    private TextField satish1TextField;
    @FXML
    private TextField satish2TextField;

    public void init(AdminClient client, Mal mal){
        this.client=client;
        this.mal=mal;
        barcodeTextField.setText(String.valueOf(mal.getBarcode()));
        kodTextField.setText(String.valueOf(mal.getKod()));
        adTextField.setText(mal.getAd());
        mayaTextField.setText(String.valueOf(mal.getMaya()));
        satish1TextField.setText(String.valueOf(mal.getSatish1()));
        satish2TextField.setText(String.valueOf(mal.getSatish2()));
    }

    public void updateMal(){
        client.sendMessage("UPDATEMALSIYAHI");
        client.sendMessage(String.valueOf(mal.getId()));
        client.sendMessage(barcodeTextField.getText());
        client.sendMessage(kodTextField.getText());
        client.sendMessage(adTextField.getText());
        client.sendMessage(mayaTextField.getText());
        client.sendMessage(satish1TextField.getText());
        client.sendMessage(satish2TextField.getText());


    }

    public TextField getBarcodeTextField(){
        return barcodeTextField;
    }
    public TextField getKodTextField() {
        return kodTextField;
    }

    public TextField getAdTextField() {
        return adTextField;
    }

    public TextField getMayaTextField() {
        return mayaTextField;
    }

    public TextField getSatish1TextField() {
        return satish1TextField;
    }

    public TextField getSatish2TextField() {
        return satish2TextField;
    }
}
