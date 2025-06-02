package com.example.defter.Admin;

import com.example.defter.Model.Debitor;
import com.example.defter.Model.Kreditor;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class YeniKassaKreditorMexaric {
    @FXML
    private AnchorPane mexaricKreditorPane;
    @FXML
    private TableView<Kreditor> kreditorTableView;
    @FXML
    private TableColumn<Kreditor,String> id;
    @FXML
    private TableColumn<Kreditor,String> ad;
    @FXML
    private TableColumn<Kreditor,String> borc;
    @FXML
    private TextField kreditorTextField;
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
    private List<Kreditor> kreditorList = new ArrayList<>();
    private ObservableList<Kreditor> kreditorObservableList = FXCollections.observableArrayList();
    private AnchorPane pane;
    private Kreditor kreditor;


    public void init(AdminClient client){
        bitirButton.setDisable(false);
        this.client=client;
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        ad.setCellValueFactory(new PropertyValueFactory<>("ad"));
        borc.setCellValueFactory(new PropertyValueFactory<>("borc"));
        kreditorList.clear();
        kreditorObservableList.clear();
        this.client.sendMessage("GETKREDITORLIST");
        kreditorList = (List<Kreditor>) client.objectReader();
        kreditorObservableList.addAll(kreditorList);
        kreditorTableView.setItems(kreditorObservableList);
        datePicker.setValue(LocalDate.now());
        choiceBox.getItems().addAll("Mədaxil", "Məxaric","Digər");
        choiceBox.setValue("Məxaric");
        kreditorTextField.textProperty().addListener((observable, oldValue, newValue) -> updateElaveEtButtonState());
        meblegTextField.textProperty().addListener((observable, oldValue, newValue) -> updateElaveEtButtonState());
        choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            filterOptions(newValue);
        });
    }
    @FXML
    private void handleDebitorTableViewClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            if (kreditorTableView.getSelectionModel().getSelectedItem() != null) {
                Kreditor selectedItem = kreditorTableView.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    kreditor =selectedItem;
                    kreditorTextField.setText(selectedItem.getAd());
                    borcTextField.setText(String.valueOf(selectedItem.getBorc()));
                }
            }
        }
    }
    public void handleKeyReleased(KeyEvent event){
        if (event.getEventType() == KeyEvent.KEY_RELEASED){
            String name = searchBar.getText();

            Search(kreditorObservableList,name);
        }
    }

    private void Search(ObservableList<Kreditor> kreditorObservableList, String name) {
        kreditorObservableList.clear();
        String lowercaseName = name.toLowerCase();
        for (int i = 0; i < kreditorList.size(); i++) {
            if (kreditorList.get(i).getAd().toLowerCase().contains(lowercaseName)) {
                kreditorObservableList.add(kreditorList.get(i));
            }
        }
    }
    private void updateElaveEtButtonState() {
        if (!areFieldsValid(meblegTextField) || meblegTextField.getText().isEmpty()|| kreditorTextField.getText().isEmpty() || borcTextField.getText().isEmpty()){
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
        if (selectedOption.equals("Mədaxil")) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/defter/AdminFXML/YeniKassaDebitorMedaxil.fxml"));
                pane = loader.load();
                YeniKassaDebitorMedaxil controller = loader.getController();
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
        if (selectedOption.equals("Digər")) {
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
        client.sendMessage(String.valueOf(datePicker.getValue()) + "/" + kreditor.getId() + "/" + String.valueOf(meblegTextField.getText()) + "/" + kreditor.getAd() + "/" + "kreditor" + "/" + "Məxaric") ;
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
        kreditorList.clear();
        kreditorObservableList.clear();
        kreditorTextField.clear();
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
        mexaricKreditorPane.getChildren().clear();
        mexaricKreditorPane.getChildren().add(node);
        FadeTransition ft = new FadeTransition(Duration.millis(1000));
        ft.setNode(node);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();

    }

}
