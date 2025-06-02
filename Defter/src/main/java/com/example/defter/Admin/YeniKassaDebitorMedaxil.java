package com.example.defter.Admin;

import com.example.defter.Model.Debitor;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class YeniKassaDebitorMedaxil {
    @FXML
    private AnchorPane medaxilDebitorPane;
    @FXML
    private TableView<Debitor> debitorTableView;
    @FXML
    private TableColumn<Debitor,String> id;
    @FXML
    private TableColumn<Debitor,String> ad;
    @FXML
    private TableColumn<Debitor,String> borc;
    @FXML
    private TextField debitorTextField;
    @FXML
    private TextField borcTextField;
    @FXML
    private TextField meblegTextField;
    @FXML
    private TextField searchBar;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ChoiceBox<String> choiceBox;
    @FXML
    private Button bitirButton;
    private AdminClient client;
    private List<Debitor> debitorList = new ArrayList<>();
    private ObservableList<Debitor> debitorObservableList = FXCollections.observableArrayList();
    private AnchorPane pane;
    private Debitor debitor;


    public void init(AdminClient client){
        bitirButton.setDisable(false);
        this.client=client;
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        ad.setCellValueFactory(new PropertyValueFactory<>("ad"));
        borc.setCellValueFactory(new PropertyValueFactory<>("borc"));
        debitorList.clear();
        debitorObservableList.clear();
        this.client.sendMessage("GETDEBITORLIST");
        debitorList = (List<Debitor>) client.objectReader();
        debitorObservableList.addAll(debitorList);
        debitorTableView.setItems(debitorObservableList);
        datePicker.setValue(LocalDate.now());
        choiceBox.getItems().addAll("Mədaxil", "Məxaric","Digər");
        choiceBox.setValue("Mədaxil");
        debitorTextField.textProperty().addListener((observable, oldValue, newValue) -> updateElaveEtButtonState());
        meblegTextField.textProperty().addListener((observable, oldValue, newValue) -> updateElaveEtButtonState());
        choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            filterOptions(newValue);
        });
    }
    @FXML
    private void handleDebitorTableViewClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            if (debitorTableView.getSelectionModel().getSelectedItem() != null) {
                Debitor selectedDebitor = debitorTableView.getSelectionModel().getSelectedItem();
                if (selectedDebitor != null) {
                    debitor=selectedDebitor;
                    debitorTextField.setText(selectedDebitor.getAd());
                    borcTextField.setText(String.valueOf(selectedDebitor.getBorc()));
                }
            }
        }
    }
    public void handleKeyReleased(KeyEvent event){
        if (event.getEventType() == KeyEvent.KEY_RELEASED){
            String name = searchBar.getText();

            Search(debitorObservableList,name);
        }
    }

    private void Search(ObservableList<Debitor> debitorObservableList, String name) {
        debitorObservableList.clear();
        String lowercaseName = name.toLowerCase();
        for (int i = 0; i < debitorList.size(); i++) {
            if (debitorList.get(i).getAd().toLowerCase().contains(lowercaseName)) {
                debitorObservableList.add(debitorList.get(i));
            }
        }
    }
    private void updateElaveEtButtonState() {
        if (!areFieldsValid(meblegTextField) || meblegTextField.getText().isEmpty()|| debitorTextField.getText().isEmpty() || borcTextField.getText().isEmpty()){
            bitirButton.setDisable(true);
        }else{
            bitirButton.setDisable(false);
        }
    }
    public boolean areFieldsValid(TextField n) {

        String itemPrice = n.getText();

        if (itemPrice.isEmpty()) {
            return false;
        }

        try {
            double itemValue = Double.parseDouble(itemPrice);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void filterOptions(String selectedOption) {
        if (selectedOption.equals("Məxaric")) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/defter/AdminFXML/YeniKassaKreditorMexaric.fxml"));
                pane = loader.load();
                YeniKassaKreditorMexaric controller = loader.getController();
                controller.init(client);
                setNode(pane);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Xəta");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }if (selectedOption.equals("Digər")) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/defter/AdminFXML/DigerKassaEmeliyyat.fxml"));
                pane = loader.load();
                DigerKassaEmeliyyatController controller = loader.getController();
                controller.init(client);
                setNode(pane);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Xəta");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }

    public void handleBitti() {
        client.sendMessage("INSERTKASSA");
        client.sendMessage(String.valueOf(datePicker.getValue()) + "/" +debitor.getId() + "/" + String.valueOf(meblegTextField.getText()) + "/" + debitor.getAd() + "/" + "debitor" + "/" + "Mədaxil") ;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/defter/AdminFXML/Kassa.fxml"));
            pane = loader.load();
            KassaController controller = loader.getController();
            controller.init(client);
            setNode(pane);
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            e.printStackTrace();
            alert.setHeaderText("Xəta");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void handleBack(){
        debitorList.clear();
        debitorObservableList.clear();
        debitorTextField.clear();
        meblegTextField.clear();
        borcTextField.clear();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/defter/AdminFXML/Kassa.fxml"));
            pane = loader.load();
            KassaController controller = loader.getController();
            controller.init(client);
            setNode(pane);
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            e.printStackTrace();
            alert.setHeaderText("Xəta");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

        private void setNode(Node node) {
            medaxilDebitorPane.getChildren().clear();
            medaxilDebitorPane.getChildren().add(node);
            FadeTransition ft = new FadeTransition(Duration.millis(1000));
            ft.setNode(node);
            ft.setFromValue(0.1);
            ft.setToValue(1);
            ft.setCycleCount(1);
            ft.setAutoReverse(false);
            ft.play();

        }


}
