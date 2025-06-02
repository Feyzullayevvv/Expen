package com.example.defter.Admin;

import com.example.defter.Model.Mal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MalSechimiDialog {
    @FXML
    private TableView<Mal> malTableView;
    @FXML
    private TableColumn<Mal,String> id;
    @FXML
    private TableColumn<Mal,String> barcode;
    @FXML
    private TableColumn<Mal,String> kod;
    @FXML
    private TableColumn<Mal,String> malAdi;
    @FXML
    private TableColumn<Mal,String> maya;
    @FXML
    private TableColumn<Mal,String> satish1;
    @FXML
    private TableColumn<Mal,String> satish2;
    private AdminClient client;
    private List<Mal> malList = new ArrayList<>();
    private ObservableList<Mal> malObservableList = FXCollections.observableArrayList();
    @FXML
    private TextField malAdiSearchBar;
    @FXML
    private TextField malKoduSearchBar;
    @FXML
    private TextField malBarcoduSearchBar;
    private String malAdiSearchText = "";
    private String malKoduSearchText = "";
    private String malBarcodeSearchText = "";

    private Button okButton;

    public void init(AdminClient client, Button okButton){
        okButton.setDisable(true);
        this.client= client;
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        barcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        kod.setCellValueFactory(new PropertyValueFactory<>("kod"));
        malAdi.setCellValueFactory(new PropertyValueFactory<>("ad"));
        maya.setCellValueFactory(new PropertyValueFactory<>("maya"));
        satish1.setCellValueFactory(new PropertyValueFactory<>("satish1"));
        satish2.setCellValueFactory(new PropertyValueFactory<>("satish2"));
        malList.clear();
        malObservableList.clear();
        this.client.sendMessage("GETMALLIST");
        malList= (List<Mal>) client.objectReader();
        malObservableList.addAll(malList);
        malTableView.setItems(malObservableList);
        malTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(newValue == null);
        });
    }
    public void handleKeyReleased(KeyEvent event) {
        if (event.getEventType() == KeyEvent.KEY_RELEASED) {
            if (event.getSource() == malAdiSearchBar) {
                malAdiSearchText = malAdiSearchBar.getText();
            } else if (event.getSource() == malKoduSearchBar) {
                malKoduSearchText = malKoduSearchBar.getText();
            } else if (event.getSource() == malBarcoduSearchBar) {
                malBarcodeSearchText = malBarcoduSearchBar.getText();
            }


            searchByProperties();
        }
    }

    private void searchByProperties() {
        malObservableList.clear();

        String lowercaseAdi = malAdiSearchText.toLowerCase();
        String lowercaseKod = malKoduSearchText.toLowerCase();
        String lowercaseBarcode = malBarcodeSearchText.toLowerCase();

        for (Mal mal : malList) {
            String adi = getPropertyByReflection(mal, "ad");
            String kod = getPropertyByReflection(mal, "kod");
            String barcode = getPropertyByReflection(mal, "barcode");

            boolean matchesAdi = adi != null && adi.toLowerCase().contains(lowercaseAdi);
            boolean matchesKod = kod != null && kod.toLowerCase().contains(lowercaseKod);
            boolean matchesBarcode = barcode != null && barcode.toLowerCase().contains(lowercaseBarcode);

            if (matchesAdi && matchesKod && matchesBarcode) {
                malObservableList.add(mal);
            }
        }
    }

    private String getPropertyByReflection(Mal mal, String propertyName) {
        try {
            Field field = Mal.class.getDeclaredField(propertyName);
            field.setAccessible(true);
            Object value = field.get(mal);
            return value != null ? value.toString() : null;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Mal getSelectedItem(){
        return malTableView.getSelectionModel().getSelectedItem();
    }


}
