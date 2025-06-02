package com.example.defter.Admin;

import com.example.defter.Model.Medaxil;
import com.example.defter.Model.MedaxilFaktura;
import com.example.defter.Model.MexaricFaktura;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MedaxilInfoController {
    @FXML
    private AnchorPane medaxilInfopane;
    @FXML
    private TableView<MedaxilFaktura> medaxilFakturaTableView;
    @FXML
    private TableColumn<MedaxilFaktura,String> malNr;
    @FXML
    private TableColumn<MedaxilFaktura,String> barcode;
    @FXML
    private TableColumn<MedaxilFaktura,String> malKodu;
    @FXML
    private TableColumn<MedaxilFaktura,String> malAdi;
    @FXML
    private TableColumn<MedaxilFaktura,Double> miqdar;
    @FXML
    private TableColumn<MedaxilFaktura,Double> satishQiymet;
    @FXML
    private TableColumn<MedaxilFaktura,Double> mebleg;

    @FXML
    private TextField tarixTextField;
    @FXML
    private TextField kreditorTextField;
    @FXML
    private TextField umumiMeblegTextField;
    @FXML
    private Button saveButton;
    private boolean isChanged = false;

    private double totalMebleg=0;
    private Medaxil medaxil;
    private AdminClient client;
    private AnchorPane pane;
    private static ObservableList<MedaxilFaktura> medaxilFakturaObservableList= FXCollections.observableArrayList();
    private static List<MedaxilFaktura> medaxilFakturaList= new ArrayList<>();


    public void init(AdminClient client){
        this.client=client;
        malNr.setCellValueFactory(new PropertyValueFactory<>("malNr"));
        barcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        malKodu.setCellValueFactory(new PropertyValueFactory<>("malKodu"));
        malAdi.setCellValueFactory(new PropertyValueFactory<>("mal"));
        miqdar.setCellValueFactory(new PropertyValueFactory<>("miqdar"));
        satishQiymet.setCellValueFactory(new PropertyValueFactory<>("maya"));
        mebleg.setCellValueFactory(new PropertyValueFactory<>("mebleg"));
        miqdar.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        satishQiymet.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        mebleg.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        initList();
        medaxilFakturaObservableList.addListener((ListChangeListener<MedaxilFaktura>) change -> {
            isChanged = true;
            saveButton.setDisable(false);
        });
        medaxilFakturaTableView.setOnKeyPressed(this::handleDeleteKeyPressed);

    }

    public void initList(){
        kreditorTextField.clear();
        tarixTextField.clear();
        umumiMeblegTextField.clear();
        medaxilFakturaList.clear();
        medaxilFakturaObservableList.clear();
        kreditorTextField.setText(medaxil.getKreditorAdi());
        tarixTextField.setText(medaxil.getTarix());
        totalMebleg = medaxil.getMebleg();
        umumiMeblegTextField.setText(String.valueOf(totalMebleg));
        client.sendMessage("GETMEDAXILFAKTURAINFO");
        client.sendMessage(String.valueOf(medaxil.getId()));
        medaxilFakturaList = (List<MedaxilFaktura>) client.objectReader();
        medaxilFakturaObservableList.addAll(medaxilFakturaList);
        medaxilFakturaTableView.setItems(medaxilFakturaObservableList);

    }

    public void onEditTableColumn(TableColumn.CellEditEvent<MedaxilFaktura, Double> event) {
        MedaxilFaktura medaxilFaktura = event.getRowValue();
        int index = medaxilFakturaObservableList.indexOf(medaxilFaktura);
        if (event.getTableColumn() == miqdar) {
            double price = medaxilFaktura.getMaya();
            totalMebleg-=medaxilFaktura.getMebleg();
            medaxilFaktura.setMiqdar(event.getNewValue());
            double newMebleg = price * event.getNewValue();
            totalMebleg+=newMebleg;
            umumiMeblegTextField.setText(String.valueOf(Math.round(totalMebleg * 100.0) / 100.0));
            medaxilFaktura.setMebleg(newMebleg);
            medaxilFakturaObservableList.remove(index);
            medaxilFakturaObservableList.add(medaxilFaktura);
        }else if (event.getTableColumn() == satishQiymet) {
            double price = medaxilFaktura.getMaya();
            totalMebleg+=medaxilFaktura.getMebleg();
            medaxilFaktura.setMaya(event.getNewValue());
            double newMebleg = medaxilFaktura.getMiqdar() * event.getNewValue();
            totalMebleg+=newMebleg;
            umumiMeblegTextField.setText(String.valueOf(Math.round(totalMebleg * 100.0) / 100.0));
            medaxilFaktura.setMebleg(newMebleg);
            medaxilFakturaObservableList.remove(index);
            medaxilFakturaObservableList.add(medaxilFaktura);
        }else if (event.getTableColumn() == mebleg) {
            totalMebleg-=medaxilFaktura.getMebleg();
            medaxilFaktura.setMebleg(event.getNewValue());
            totalMebleg+=event.getNewValue();
            umumiMeblegTextField.setText(String.valueOf(Math.round(totalMebleg * 100.0) / 100.0));
            medaxilFakturaObservableList.remove(index);
            medaxilFakturaObservableList.add(medaxilFaktura);
        }
        medaxilFakturaTableView.refresh();
    }
    private void handleDeleteKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.BACK_SPACE) {
            MedaxilFaktura selectedItem = medaxilFakturaTableView.getSelectionModel().getSelectedItem();
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
                        medaxilFakturaObservableList.remove(selectedItem);
                        isChanged = true;
                        saveButton.setDisable(false);
                    }
                });
            }
        }
    }

    public void createMalSiyahi(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(medaxilInfopane.getScene().getWindow());
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
        if ((result.isPresent() && result.get()== ButtonType.OK) && controller.getSelectedItem()!=null) {
            MedaxilFaktura medaxilFaktura = new MedaxilFaktura(controller.getSelectedItem().getId(),controller.getSelectedItem().getBarcode(),controller.getSelectedItem().getKod(),
                    controller.getSelectedItem().getAd(),0,controller.getSelectedItem().getMaya(),0);
            medaxilFaktura.setMedaxilNr(medaxil.getId());
            medaxilFakturaObservableList.add(medaxilFaktura);
        }
        medaxilFakturaTableView.refresh();
    }

    public void handleSave(){
        client.sendMessage("DELETEMEDAXILINFO");
        client.sendMessage(String.valueOf(medaxil.getId()));
        client.sendMessage(String.valueOf(medaxil.getKreditorId()));
        int result  = Integer.parseInt(client.reader());
        if (result!=1){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Xəta");
            alert.setContentText("Error in DELETEMEDAXILINFO");
            alert.showAndWait();
        }
        client.sendMessage("INSERTMEDAXILFAKTURA");
        for (MedaxilFaktura m : medaxilFakturaObservableList){
            client.sendMessage(m.getMalNr()+"/"+m.getMiqdar()+"/"+ m.getMaya() +"/"+
                    m.getMebleg()+"/" + medaxil.getId());
        }
        client.sendMessage("DONE");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/defter/AdminFXML/Medaxil.fxml"));
            pane = loader.load();
            MedaxilController controller = loader.getController();
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
        try {
            kreditorTextField.clear();
            tarixTextField.clear();
            umumiMeblegTextField.clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/defter/AdminFXML/Medaxil.fxml"));
            pane = loader.load();
            MedaxilController controller = loader.getController();
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
        medaxilInfopane.getChildren().clear();
        medaxilInfopane.getChildren().add(node);
        FadeTransition ft = new FadeTransition(Duration.millis(1000));
        ft.setNode(node);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();

    }


    public void setMedaxil(Medaxil medaxil){
        this.medaxil=medaxil;
    }
}
