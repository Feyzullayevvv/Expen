package com.example.defter.Admin;

import com.example.defter.Model.*;
import com.example.defter.PrintBill;
import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import javafx.util.converter.DoubleStringConverter;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.w3c.dom.Text;


import java.io.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;

public class YeniMexaric {
    private AdminClient client;
    @FXML
    private AnchorPane mexaricFakturaPane;
    @FXML
    private TableView<MexaricFaktura> mexaricFakturaTableView;
    @FXML
    private TableColumn<MexaricFaktura,String> malNr;
    @FXML
    private TableColumn<MexaricFaktura,String> mal;
    @FXML
    private TableColumn<MexaricFaktura,Double> miqdar;
    @FXML
    private TableColumn<MexaricFaktura,Double> satishQiymet;
    @FXML
    private TableColumn<MexaricFaktura,Double> mebleg;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ChoiceBox<String> choiceBox;
    @FXML
    private TextField malTextField;
    @FXML
    private TextField miqdarTextField;
    @FXML
    private TextField meblegTextField;
    @FXML
    private TextField satishQiymetTextField;
    @FXML
    private TextField debitorTextField;
    @FXML
    private TextField movcudTextField;
    @FXML
    private Button silButton;
    @FXML
    private Button elaveEtButton;
    @FXML
    private Button bitirButton;
    @FXML
    private TextField totalTextField;
    private double total=0.0;
    private AnchorPane pane;
    private Mal selectedMal;
    private Debitor debitor;
    private List<MexaricFaktura> mexaricFakturaList = new ArrayList<>();
    private ObservableList<MexaricFaktura> mexaricFakturaObservableList = FXCollections.observableArrayList();
    private PrintBill printBill;




    public void init(AdminClient client){
        totalTextField.setText("0.0");
        printBill = new PrintBill();
        this.client=client;
        malNr.setCellValueFactory(new PropertyValueFactory<>("malNr"));
        mal.setCellValueFactory(new PropertyValueFactory<>("mal"));
        miqdar.setCellValueFactory(new PropertyValueFactory<>("miqdar"));
        satishQiymet.setCellValueFactory(new PropertyValueFactory<>("satishQiymet"));
        mebleg.setCellValueFactory(new PropertyValueFactory<>("mebleg"));
        datePicker.setValue(LocalDate.now());
        choiceBox.getItems().addAll("Topdan", "Pərandə");
        choiceBox.setValue("Topdan");
        choiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                updateSatishValue();
            }
        });
        satishQiymetTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateMeblegValue();
        });

        miqdarTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateMeblegValue();
        });
        miqdar.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        satishQiymet.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        mebleg.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        mexaricFakturaList.clear();
        mexaricFakturaObservableList.clear();
        elaveEtButton.setDisable(true);
        silButton.setDisable(true);
        bitirButton.setDisable(true);
        malTextField.textProperty().addListener(((observableValue, s, t1) -> updateElaveEtButtonState()));
        miqdarTextField.textProperty().addListener(((observableValue, s, t1) -> updateElaveEtButtonState()));
        satishQiymetTextField.textProperty().addListener(((observableValue, s, t1) -> updateElaveEtButtonState()));
        debitorTextField.textProperty().addListener(((observableValue, s, t1) -> updateElaveEtButtonState()));
        meblegTextField.textProperty().addListener(((observableValue, s, t1) -> updateElaveEtButtonState()));

    }
    public void onEditTableColumn(TableColumn.CellEditEvent<MexaricFaktura, Double> event) {
        MexaricFaktura mexaricFaktura = event.getRowValue();
        int index = mexaricFakturaObservableList.indexOf(mexaricFaktura);
        if (event.getTableColumn() == miqdar) {
            double price = mexaricFaktura.getSatishQiymet();
            total-=mexaricFaktura.getMebleg();
            mexaricFaktura.setMiqdar(event.getNewValue());
            double newMebleg = price * event.getNewValue();
            total+=newMebleg;
            totalTextField.setText(String.valueOf(Math.round(total * 100.0) / 100.0));
            mexaricFaktura.setMebleg(newMebleg);
            mexaricFakturaObservableList.remove(index);
            mexaricFakturaObservableList.add(mexaricFaktura);
        }
        mexaricFakturaTableView.refresh();
    }

    public void initList(){
        movcudTextField.clear();
        malTextField.clear();
        miqdarTextField.clear();
        satishQiymetTextField.clear();
        meblegTextField.clear();
        mexaricFakturaObservableList.clear();
        mexaricFakturaObservableList.addAll(mexaricFakturaList);
        mexaricFakturaTableView.setItems(mexaricFakturaObservableList);
        if (mexaricFakturaObservableList.isEmpty()){
            bitirButton.setDisable(true);
            silButton.setDisable(true);
        }else{
            bitirButton.setDisable(false);
            silButton.setDisable(false);
        }
    }


    public void createMalSiyahiView(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mexaricFakturaPane.getScene().getWindow());
        dialog.setTitle("Mal seçmək");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/example/defter/AdminFXML/MalSechimiDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        MalSechimiDialog controller = fxmlLoader.getController();
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        controller.init(client,(Button) dialog.getDialogPane().lookupButton(ButtonType.OK));

        Optional<ButtonType> result= dialog.showAndWait();
        if ((result.isPresent() && result.get()== ButtonType.OK) && controller.getSelectedItem()!=null){
            selectedMal= controller.getSelectedItem();
            malTextField.setText(controller.getSelectedItem().getAd());
            System.out.println(selectedMal.getId());
            client.sendMessage("GETANBARMOVCUD");
            client.sendMessage(String.valueOf(selectedMal.getId()));
            Anbar anbar = (Anbar) client.itemReader();
            if (anbar!=null){
                movcudTextField.setText(String.valueOf(anbar.getMiqdar()));
            }else {
                movcudTextField.setText("0");
            }
            if (choiceBox.getValue().equals("Topdan")) {
                satishQiymetTextField.setText(String.valueOf(controller.getSelectedItem().getSatish1()));
            } else {
                satishQiymetTextField.setText(String.valueOf(controller.getSelectedItem().getSatish2()));
            }
        }

    }

    public void createDebitorDialog(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mexaricFakturaPane.getScene().getWindow());
        dialog.setTitle("Kreditor seçmək");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/example/defter/AdminFXML/DebitorSiyahiDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        DebitorSiyahiDialog controller = fxmlLoader.getController();
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        controller.init(client);

        Optional<ButtonType> result= dialog.showAndWait();
        if ((result.isPresent() && result.get()== ButtonType.OK) && controller.getSelectedItem()!=null) {
            debitor = controller.getSelectedItem();
            debitorTextField.setText(debitor.getAd());
        }
    }

    private void updateSatishValue() {
        if (selectedMal != null) {
            if (choiceBox.getValue().equals("Topdan")) {
                satishQiymetTextField.setText(String.valueOf(selectedMal.getSatish1()));
            } else {
                satishQiymetTextField.setText(String.valueOf(selectedMal.getSatish2()));
            }
        }
    }
    private void updateMeblegValue() {
        if (areFieldsValid(satishQiymetTextField) && areFieldsValid(miqdarTextField)) {
            double satishQiymet = Double.parseDouble(satishQiymetTextField.getText());
            double miqdar = Double.parseDouble(miqdarTextField.getText());

            double meblegValue = satishQiymet * miqdar;
            meblegTextField.setText(String.valueOf(meblegValue));
        } else {
            meblegTextField.clear();
        }
    }

    public boolean areFieldsValid(TextField textField) {
        String fieldValue = textField.getText();

        if (fieldValue.isEmpty()) {
            return false;
        }

        try {
            double fieldDoubleValue = Double.parseDouble(fieldValue);
            return true;
        } catch (NumberFormatException e) {

            return false;
        }
    }
    private void updateElaveEtButtonState() {
        if (malTextField.getText().isEmpty() || miqdarTextField.getText().isEmpty() || satishQiymetTextField.getText().isEmpty()
                || mebleg.getText().isEmpty() || !areFieldsValid(satishQiymetTextField)
                || !areFieldsValid(miqdarTextField) || debitorTextField.getText().isEmpty() || Double.parseDouble(miqdarTextField.getText()) > Double.parseDouble(movcudTextField.getText())) {
            elaveEtButton.setDisable(true);
        } else {
            elaveEtButton.setDisable(false);
        }
    }

    public void elaveEt(){
        MexaricFaktura mexaricFaktura  = new MexaricFaktura(selectedMal.getId(),selectedMal.getAd(),
                Double.parseDouble(miqdarTextField.getText()),
                Double.parseDouble(satishQiymetTextField.getText()),Double.parseDouble(meblegTextField.getText()));
        mexaricFakturaList.add(mexaricFaktura);
        total+=mexaricFaktura.getMebleg();
        totalTextField.setText(String.valueOf(Math.round(total * 100.0) / 100.0));
        initList();
    }

    public void handleDelete(){
        MexaricFaktura item = mexaricFakturaTableView.getSelectionModel().getSelectedItem();
        if (item!=null){
            try {
                mexaricFakturaList.remove(item);
                total-=item.getMebleg();
                totalTextField.setText(String.valueOf(Math.round(total * 100.0) / 100.0));
                initList();
            }catch (Exception e){
                System.out.println(e.getMessage());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Xəta");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }

    public void clearFields(){
        mexaricFakturaList.clear();
        mexaricFakturaObservableList.clear();
        debitorTextField.clear();
        malTextField.clear();
        satishQiymetTextField.clear();
        miqdarTextField.clear();
        meblegTextField.clear();
        movcudTextField.clear();
    }
    public void handleBack(){
        try {
            clearFields();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/defter/AdminFXML/Mexaric.fxml"));
            pane = loader.load();
            MexaricController controller = loader.getController();
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

    public void handleBitti() throws JRException, FileNotFoundException {
        client.sendMessage("INSERTMEXARIC");
        client.sendMessage(String.valueOf(datePicker.getValue()));
        client.sendMessage(String.valueOf(debitor.getId()));
        int medaxilNr = Integer.parseInt(client.reader());
        client.sendMessage("INSERTMEXARICFAKTURA");
        for (MexaricFaktura m : mexaricFakturaObservableList){
            client.sendMessage(m.getMalNr()+"/"+m.getMiqdar()+"/"+ m.getSatishQiymet() +"/"+
                    m.getMebleg()+"/" + medaxilNr);
        }
        client.sendMessage("DONE");
        Alert printBillAlert = new Alert(Alert.AlertType.NONE);
        printBillAlert.setTitle("Seçimlərdən birini seçin zəhmət olmasa");
        printBillAlert.setHeaderText("Choose an option:");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType buttonTypeCek = new ButtonType("Çek");
        ButtonType buttonTypeInvoice = new ButtonType("Invoice");

        printBillAlert.getButtonTypes().setAll(buttonTypeCancel, buttonTypeCek, buttonTypeInvoice);

        Optional<ButtonType> result = printBillAlert.showAndWait();

        try {
            if (result.isPresent()) {
                if (result.get() == buttonTypeCek) {
                    printBill.printBill(mexaricFakturaList, String.valueOf(datePicker.getValue()), debitor.getAd());
                } else if (result.get() == buttonTypeInvoice) {
                    printBill.printInvoice(mexaricFakturaList, String.valueOf(datePicker.getValue()), debitor.getAd());
                }
            }
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

        try {
            clearFields();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/defter/AdminFXML/Mexaric.fxml"));
            pane = loader.load();
            MexaricController controller = loader.getController();
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
        mexaricFakturaPane.getChildren().clear();
        mexaricFakturaPane.getChildren().add(node);
        FadeTransition ft = new FadeTransition(Duration.millis(1000));
        ft.setNode(node);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();

    }


}
