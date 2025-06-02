package com.example.defter.Admin;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.time.LocalDate;

public class DigerKassaEmeliyyatController {
    private AdminClient client;
    @FXML
    private AnchorPane digerKassaPane;
    @FXML
    private RadioButton medaxilRadioButton;
    @FXML
    private RadioButton mexaricRadioButton;
    @FXML
    private TextField qeydTextField;
    @FXML
    private TextField meblegTextField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Button bitirButton;
    @FXML
    private ChoiceBox<String> choiceBox;
    private ToggleGroup toggleGroup;
    private AnchorPane pane;

    public void init(AdminClient client){
        this.client=client;
        toggleGroup = new ToggleGroup();
        medaxilRadioButton.setToggleGroup(toggleGroup);
        mexaricRadioButton.setToggleGroup(toggleGroup);
        toggleGroup.selectToggle(mexaricRadioButton);
        datePicker.setValue(LocalDate.now());
        choiceBox.getItems().addAll("Mədaxil", "Məxaric","Digər");
        choiceBox.setValue("Digər");
        meblegTextField.textProperty().addListener((observable, oldValue, newValue) -> updateElaveEtButtonState());
        choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            filterOptions(newValue);
        });
        medaxilRadioButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                mexaricRadioButton.setSelected(false);
            }
        });
        mexaricRadioButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                medaxilRadioButton.setSelected(false);
            }
        });
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
        }
    }

    public void handleBack(){
        qeydTextField.clear();
        meblegTextField.clear();
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
    private void updateElaveEtButtonState() {
        if (!areFieldsValid(meblegTextField) || meblegTextField.getText().isEmpty()|| qeydTextField.getText().isEmpty() || meblegTextField.getText().isEmpty()){
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

    public void handleBitti() {
        client.sendMessage("INSERTKASSA");
        String selectedRadioButton="";
        if (toggleGroup.getSelectedToggle() == medaxilRadioButton) {
            selectedRadioButton = "Mədaxil";
        } else if (toggleGroup.getSelectedToggle() == mexaricRadioButton) {
            selectedRadioButton = "Məxaric";
        }
        client.sendMessage(String.valueOf(datePicker.getValue()) + "/" + "00" + "/" + String.valueOf(meblegTextField.getText()) + "/" + qeydTextField.getText() + "/" + "diger" + "/" + selectedRadioButton) ;
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
        digerKassaPane.getChildren().clear();
        digerKassaPane.getChildren().add(node);
        FadeTransition ft = new FadeTransition(Duration.millis(1000));
        ft.setNode(node);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();

    }
}
