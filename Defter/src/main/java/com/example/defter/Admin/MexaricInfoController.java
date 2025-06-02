package com.example.defter.Admin;

import com.example.defter.Model.Mexaric;
import com.example.defter.Model.MexaricFaktura;
import com.example.defter.PrintBill;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.util.converter.DoubleStringConverter;
import net.sf.jasperreports.engine.JRException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MexaricInfoController {
    @FXML
    private AnchorPane mexaricInfoPane;
    private Mexaric mexaric;
    private AdminClient client;
    private AnchorPane pane;
    @FXML
    private TableView<MexaricFaktura> mexaricFakturaTableView;
    @FXML
    private TableColumn<MexaricFaktura,String> malNr;
    @FXML
    private TableColumn<MexaricFaktura,String> barcode;
    @FXML
    private TableColumn<MexaricFaktura,String> malKodu;
    @FXML
    private TableColumn<MexaricFaktura,String> malAdi;
    @FXML
    private TableColumn<MexaricFaktura,Double> miqdar;
    @FXML
    private TableColumn<MexaricFaktura,Double> satishQiymet;
    @FXML
    private TableColumn<MexaricFaktura,Double> mebleg;

    @FXML
    private TextField tarixTextField;
    @FXML
    private TextField debitorTextField;
    @FXML
    private TextField umumiMeblegTextField;
    @FXML
    private Button saveButton;
    private boolean isChanged = false;

    private double totalMebleg=0;
    private PrintBill printBill;
    private static ObservableList<MexaricFaktura> mexaricFakturaObservableList = FXCollections.observableArrayList();
    private static List<MexaricFaktura> mexaricFakturaList = new ArrayList<>();


    public void init(AdminClient client){
        printBill= new PrintBill();
        this.client=client;
        malNr.setCellValueFactory(new PropertyValueFactory<>("malNr"));
        barcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        malKodu.setCellValueFactory(new PropertyValueFactory<>("malKodu"));
        malAdi.setCellValueFactory(new PropertyValueFactory<>("mal"));
        miqdar.setCellValueFactory(new PropertyValueFactory<>("miqdar"));
        satishQiymet.setCellValueFactory(new PropertyValueFactory<>("satishQiymet"));
        mebleg.setCellValueFactory(new PropertyValueFactory<>("mebleg"));
        miqdar.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        satishQiymet.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        mebleg.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        initList();
        mexaricFakturaObservableList.addListener((ListChangeListener<MexaricFaktura>) change -> {
            isChanged = true;
            saveButton.setDisable(false);
        });
        mexaricFakturaTableView.setOnKeyPressed(this::handleDeleteKeyPressed);
    }

    public void initList(){
        debitorTextField.clear();
        tarixTextField.clear();
        umumiMeblegTextField.clear();
        mexaricFakturaList.clear();
        mexaricFakturaObservableList.clear();
        debitorTextField.setText(mexaric.getDebitor());
        tarixTextField.setText(mexaric.getTarix());
        totalMebleg = mexaric.getMebleg();
        umumiMeblegTextField.setText(String.valueOf(totalMebleg));
        client.sendMessage("GETMEXARICINFO");
        client.sendMessage(String.valueOf(mexaric.getId()));
        mexaricFakturaList = (List<MexaricFaktura>) client.objectReader();
        mexaricFakturaObservableList.addAll(mexaricFakturaList);
        mexaricFakturaTableView.setItems(mexaricFakturaObservableList);

    }

    public void setMexaric(Mexaric mexaric){
        this.mexaric=mexaric;
    }

    public void onEditTableColumn(TableColumn.CellEditEvent<MexaricFaktura, Double> event) {
        MexaricFaktura mexaricFaktura = event.getRowValue();
        int index = mexaricFakturaObservableList.indexOf(mexaricFaktura);
        if (event.getTableColumn() == miqdar) {
            double price = mexaricFaktura.getSatishQiymet();
            totalMebleg-=mexaricFaktura.getMebleg();
            mexaricFaktura.setMiqdar(event.getNewValue());
            double newMebleg = price * event.getNewValue();
            totalMebleg+=newMebleg;
            umumiMeblegTextField.setText(String.valueOf(Math.round(totalMebleg * 100.0) / 100.0));
            mexaricFaktura.setMebleg(newMebleg);
            mexaricFakturaObservableList.remove(index);
            mexaricFakturaObservableList.add(mexaricFaktura);
        }else if (event.getTableColumn() == satishQiymet) {
            double price = mexaricFaktura.getSatishQiymet();
            totalMebleg-=mexaricFaktura.getMebleg();
            mexaricFaktura.setSatishQiymet(event.getNewValue());
            double newMebleg = mexaricFaktura.getMiqdar() * event.getNewValue();
            totalMebleg+=newMebleg;
            umumiMeblegTextField.setText(String.valueOf(Math.round(totalMebleg * 100.0) / 100.0));
            mexaricFaktura.setMebleg(newMebleg);
            mexaricFakturaObservableList.remove(index);
            mexaricFakturaObservableList.add(mexaricFaktura);
        }else if (event.getTableColumn() == mebleg) {
            double price = mexaricFaktura.getMebleg();
            totalMebleg-=mexaricFaktura.getMebleg();
            mexaricFaktura.setMebleg(event.getNewValue());
            totalMebleg+=event.getNewValue();
            umumiMeblegTextField.setText(String.valueOf(Math.round(totalMebleg * 100.0) / 100.0));
            mexaricFakturaObservableList.remove(index);
            mexaricFakturaObservableList.add(mexaricFaktura);
        }
        mexaricFakturaTableView.refresh();
    }

    private void handleDeleteKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.BACK_SPACE) {
            MexaricFaktura selectedItem = mexaricFakturaTableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Təsdiq");
                alert.setHeaderText(null);
                alert.setContentText("Bu Məhsulu Silmək istədiyinizə əminsiniz?" +  "\nMal: " + selectedItem.getMal() + "\nMiqdar: " +
                        selectedItem.getMiqdar() + "\nMəbləğ: " + selectedItem.getMebleg());
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        totalMebleg-=selectedItem.getMebleg();
                        umumiMeblegTextField.setText(String.valueOf(totalMebleg));
                        mexaricFakturaObservableList.remove(selectedItem);
                        isChanged = true;
                        saveButton.setDisable(false);
                    }
                });
            }
        }
    }


    public void handlePrint() throws JRException, FileNotFoundException {
        Alert printBillAlert = new Alert(Alert.AlertType.NONE);
        printBillAlert.setTitle("Print Bill");
        printBillAlert.setHeaderText("Seçimlərdən birini seçin zəhmət olmasa:");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType buttonTypeCek = new ButtonType("Çek");
        ButtonType buttonTypeInvoice = new ButtonType("Invoice");

        printBillAlert.getButtonTypes().setAll(buttonTypeCancel, buttonTypeCek, buttonTypeInvoice);

        Optional<ButtonType> result = printBillAlert.showAndWait();

        if (result.isPresent()) {
            if (result.get() == buttonTypeCek) {
                printBill.printBill(mexaricFakturaList,tarixTextField.getText(),debitorTextField.getText());
            } else if (result.get() == buttonTypeInvoice) {
                printBill.printInvoice(mexaricFakturaList,tarixTextField.getText(),debitorTextField.getText());
            }
        }
    }



    public void createMalSiyahi(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mexaricInfoPane.getScene().getWindow());
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
            MexaricFaktura mexaricFaktura = new MexaricFaktura(controller.getSelectedItem().getId(),controller.getSelectedItem().getBarcode(),controller.getSelectedItem().getKod(),
                    controller.getSelectedItem().getAd(),0,controller.getSelectedItem().getSatish1(),0);

            mexaricFaktura.setMexaricId(mexaric.getId());
            mexaricFakturaObservableList.add(mexaricFaktura);
        }
        mexaricFakturaTableView.refresh();

    }

    public void handleSave(){
        try {
            client.sendMessage("DELETEMEXARICINFO");
            client.sendMessage(String.valueOf(mexaric.getId()));
            client.sendMessage(String.valueOf(mexaric.getDebitorId()));
            int result = Integer.parseInt(client.reader());
            if (result!=1){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Xəta");
                alert.setContentText("Error in DELETEMEXARICINFO");
                alert.showAndWait();
            }
            client.sendMessage("INSERTMEXARICFAKTURA");
            for (MexaricFaktura m : mexaricFakturaObservableList){
                client.sendMessage(m.getMalNr()+"/"+m.getMiqdar()+"/"+ m.getSatishQiymet() +"/"+
                        m.getMebleg()+"/" + mexaric.getId());
            }
            client.sendMessage("DONE");
            debitorTextField.clear();
            tarixTextField.clear();
            umumiMeblegTextField.clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/defter/AdminFXML/Mexaric.fxml"));
            pane = loader.load();
            MexaricController controller = loader.getController();
            controller.init(client);
            setNode(pane);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void handleBack(){
        try {
            debitorTextField.clear();
            tarixTextField.clear();
            umumiMeblegTextField.clear();
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
        mexaricInfoPane.getChildren().clear();
        mexaricInfoPane.getChildren().add(node);
        FadeTransition ft = new FadeTransition(Duration.millis(1000));
        ft.setNode(node);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();

    }
}
