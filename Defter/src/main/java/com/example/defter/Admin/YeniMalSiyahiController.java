package com.example.defter.Admin;

import com.example.defter.Model.Mal;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class YeniMalSiyahiController {
    @FXML
    private AnchorPane yeniMalPane;
    @FXML
    private TableView<Mal> yeniMalTableView;
    @FXML
    private TableColumn<Mal,Double> barcodeTable;
    @FXML
    private TableColumn<Mal,String> kodTable;
    @FXML
    private TableColumn<Mal,String> malAdiTable;
    @FXML
    private TableColumn<Mal,String> mayaTable;
    @FXML
    private TableColumn<Mal,String> satish1Table;
    @FXML
    private TableColumn<Mal,String> satish2Table;
    @FXML
    private TextField barcodeTextField;
    @FXML
    private TextField kodTextField;
    @FXML
    private TextField malAdiTextField;
    @FXML
    private TextField mayaTextField;
    @FXML
    private TextField satish1TextField;
    @FXML
    private TextField satish2TextField;
    @FXML
    private Button silButton;
    @FXML
    private Button elaveEtButton;
    @FXML
    private Button bitirButton;
    private List<Mal> malList =new ArrayList<>();
    private ObservableList<Mal> malObservableList = FXCollections.observableArrayList();

    private AdminClient client;
    private AnchorPane pane;
    public void init(){
        barcodeTable.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        kodTable.setCellValueFactory(new PropertyValueFactory<>("kod"));
        malAdiTable.setCellValueFactory(new PropertyValueFactory<>("ad"));
        mayaTable.setCellValueFactory(new PropertyValueFactory<>("maya"));
        satish1Table.setCellValueFactory(new PropertyValueFactory<>("satish1"));
        satish2Table.setCellValueFactory(new PropertyValueFactory<>("satish2"));
        malList.clear();
        malObservableList.clear();

        barcodeTextField.textProperty().addListener(((observableValue, s, t1) -> updateElaveEtButtonState()));
        kodTextField.textProperty().addListener(((observableValue, s, t1) -> updateElaveEtButtonState()));
        malAdiTextField.textProperty().addListener(((observableValue, s, t1) -> updateElaveEtButtonState()));
        mayaTextField.textProperty().addListener(((observableValue, s, t1) -> updateElaveEtButtonState()));
        satish1TextField.textProperty().addListener(((observableValue, s, t1) -> updateElaveEtButtonState()));
        satish2TextField.textProperty().addListener(((observableValue, s, t1) -> updateElaveEtButtonState()));
        initList();

    }

    public void initList(){
        barcodeTextField.clear();
        kodTextField.clear();
        malAdiTextField.clear();
        mayaTextField.clear();
        satish1TextField.clear();
        satish2TextField.clear();
        malObservableList.clear();
        barcodeTextField.setText("111111111111111111");
        kodTextField.setText("EMPTY");
        malObservableList.addAll(malList);
        yeniMalTableView.setItems(malObservableList);
        if (malObservableList.isEmpty()){
            bitirButton.setDisable(true);
            silButton.setDisable(true);
        }else{
            bitirButton.setDisable(false);
            silButton.setDisable(false);
        }
    }

    public void setClient(AdminClient client){
        this.client=client;
    }
    public boolean areFieldsValid(TextField textField) {
        String qiymetText = textField.getText();

        if (qiymetText.isEmpty()) {
            return false;
        }

        try {
            double qiymetValue = Double.parseDouble(qiymetText);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private void updateElaveEtButtonState() {
        if (malAdiTextField.getText().isEmpty() || kodTextField.getText().isEmpty() || malAdiTextField.getText().isEmpty() ||
        mayaTextField.getText().isEmpty() || satish1TextField.getText().isEmpty() || satish2TextField.getText().isEmpty() || !areFieldsValid(barcodeTextField)
                || !areFieldsValid(satish1TextField) || !areFieldsValid(satish2TextField)){
            elaveEtButton.setDisable(true);
        }else{
            elaveEtButton.setDisable(false);
        }
    }

    private void setNode(Node node) {
        yeniMalPane.getChildren().clear();
        yeniMalPane.getChildren().add(node);
        FadeTransition ft = new FadeTransition(Duration.millis(1000));
        ft.setNode(node);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();

    }

    public void handleBack(){
        try {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Təstiq");
            confirmationAlert.setHeaderText("Ləğv etmək istədiyinizdən əminsiniz?");
            confirmationAlert.setContentText("Qeydə alınmamış məlumatlar silinəcək.");

            confirmationAlert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
            ButtonType result = confirmationAlert.showAndWait().orElse(ButtonType.CANCEL);
            if (result == ButtonType.OK) {
                malList.clear();
                malObservableList.clear();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/defter/AdminFXML/MalSiyahi.fxml"));
                pane = loader.load();
                MalSiyahiController controller = loader.getController();
                controller.setClient(client);
                controller.init();
                setNode(pane);
            }
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            e.printStackTrace();
            alert.setHeaderText("Xəta");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    public void handleDelete(){
        Mal selectedItem = yeniMalTableView.getSelectionModel().getSelectedItem();
        try {
            malList.remove(selectedItem);
            initList();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            Alert  alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Xəta");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void handleBitti(){
        client.sendMessage("INSERTNEWMAL");
        for (Mal mal : malObservableList){
            System.out.println("Barcode  in controller" + mal.getBarcode());
            client.sendMessage(mal.getBarcode() + "/" + mal.getKod() + "/" + mal.getAd() +"/" +mal.getMaya() + "/" +
                    mal.getSatish1() + "/" + mal.getSatish2());
        }
        client.sendMessage("DONE");

        try {
            malList.clear();
            malObservableList.clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/defter/AdminFXML/MalSiyahi.fxml"));
            pane = loader.load();
            MalSiyahiController controller = loader.getController();
            controller.setClient(client);
            controller.init();
            setNode(pane);
        }catch (Exception e){
            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Xəta");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void onElaveEtClicked(){

        Mal mal = new Mal(Long.parseLong(barcodeTextField.getText()),kodTextField.getText(),malAdiTextField.getText(),Double.parseDouble(mayaTextField.getText()),
                Double.parseDouble(satish1TextField.getText()),Double.parseDouble(satish2TextField.getText()));

        malList.add(mal);
        initList();
    }




}
