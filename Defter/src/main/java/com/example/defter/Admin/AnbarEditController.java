package com.example.defter.Admin;

import com.example.defter.Model.Anbar;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AnbarEditController {
    private AdminClient client;
    private Anbar item;
    @FXML
    private TextField malKodu;
    @FXML
    private TextField barcode;
    @FXML
    private TextField ad;
    @FXML
    private TextField miqdar;
    @FXML
    private TextField mebleg;

    public void init(AdminClient client,Anbar item){
        this.client=client;
        this.item= item;
        malKodu.setText(String.valueOf(item.getMalKod()));
        barcode.setText(String.valueOf(item.getBarcode()));
        ad.setText(item.getMalAdi());
        miqdar.setText(String.valueOf(item.getMiqdar()));
        mebleg.setText(String.valueOf(item.getMebleg()));
    }

    public void updateAnbarItem(){
        client.sendMessage("UPDATEANBARITEM");
        client.sendMessage(String.valueOf(item.getId()));
        client.sendMessage(String.valueOf(miqdar.getText()));
        client.sendMessage(String.valueOf(mebleg.getText()));
    }

    public TextField getMalKodu() {
        return malKodu;
    }

    public TextField getMiqdar() {
        return miqdar;
    }

    public TextField getMebleg() {
        return mebleg;
    }
}
