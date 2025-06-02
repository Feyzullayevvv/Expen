package com.example.defter.Admin;

import com.example.defter.Model.Kreditor;
import com.example.defter.Model.Mal;
import com.example.defter.Model.MedaxilFaktura;
import com.example.defter.Model.MexaricFaktura;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.util.converter.DoubleStringConverter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class YeniMedaxil {
    private AdminClient client;
    @FXML
    private AnchorPane medaxilFakturaPane;
    @FXML
    private TableView<MedaxilFaktura> medaxilFakturaTableView;
    @FXML
    private TableColumn<MedaxilFaktura,String> malNr;
    @FXML
    private TableColumn<MedaxilFaktura,String> mal;
    @FXML
    private TableColumn<MedaxilFaktura,Double> miqdar;
    @FXML
    private TableColumn<MedaxilFaktura,Double> satishQiymet;
    @FXML
    private TableColumn<MedaxilFaktura,Double> mebleg;
    @FXML
    private TextField malTextField;
    @FXML
    private TextField miqdarTextField;
    @FXML
    private TextField meblegTextField;
    @FXML
    private TextField mayaTextField;
    @FXML
    private TextField kreditorTextField;

    @FXML
    private DatePicker datePicker;
    @FXML
    private Button elaveEtButton;
    @FXML
    private Button silButton;
    @FXML
    private Button bitirButton;

    @FXML
    private TextField totalTextField;

    private double total=0.0;

    private Kreditor selectedKreditor;
    private Mal selectedMal;
    private AnchorPane pane;

    private List<MedaxilFaktura> medaxilFakturaList = new ArrayList<>();
    private ObservableList<MedaxilFaktura> medaxilFakturaObservableList = FXCollections.observableArrayList();

    public void init(AdminClient client){
        totalTextField.setText("0.0");
        this.client=client;
        malNr.setCellValueFactory(new PropertyValueFactory<>("malNr"));
        mal.setCellValueFactory(new PropertyValueFactory<>("mal"));
        miqdar.setCellValueFactory(new PropertyValueFactory<>("miqdar"));
        satishQiymet.setCellValueFactory(new PropertyValueFactory<>("maya"));
        mebleg.setCellValueFactory(new PropertyValueFactory<>("mebleg"));
        datePicker.setValue(LocalDate.now());

        mayaTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateMeblegValue();
        });

        miqdarTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateMeblegValue();
        });
        miqdar.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        satishQiymet.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        mebleg.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        medaxilFakturaList.clear();
        medaxilFakturaObservableList.clear();
        elaveEtButton.setDisable(true);
        silButton.setDisable(true);
        bitirButton.setDisable(true);
        malTextField.textProperty().addListener(((observableValue, s, t1) -> updateElaveEtButtonState()));
        miqdarTextField.textProperty().addListener(((observableValue, s, t1) -> updateElaveEtButtonState()));
        mayaTextField.textProperty().addListener(((observableValue, s, t1) -> updateElaveEtButtonState()));
        kreditorTextField.textProperty().addListener(((observableValue, s, t1) -> updateElaveEtButtonState()));
        meblegTextField.textProperty().addListener(((observableValue, s, t1) -> updateElaveEtButtonState()));

    }

    public void initList(){
        malTextField.clear();
        miqdarTextField.clear();
        mayaTextField.clear();
        meblegTextField.clear();
        medaxilFakturaObservableList.clear();
        medaxilFakturaObservableList.addAll(medaxilFakturaList);
        medaxilFakturaTableView.setItems(medaxilFakturaObservableList);
        if (medaxilFakturaObservableList.isEmpty()){
            bitirButton.setDisable(true);
            silButton.setDisable(true);
        }else{
            bitirButton.setDisable(false);
            silButton.setDisable(false);
        }
    }

    public void onEditTableColumn(TableColumn.CellEditEvent<MedaxilFaktura, Double> event) {
        MedaxilFaktura medaxilFaktura = event.getRowValue();
        int index = medaxilFakturaObservableList.indexOf(medaxilFaktura);
        if (event.getTableColumn() == miqdar) {
            double price = medaxilFaktura.getMaya();
            total-=medaxilFaktura.getMebleg();
            medaxilFaktura.setMiqdar(event.getNewValue());
            double newMebleg = price * event.getNewValue();
            total+=newMebleg;
            totalTextField.setText(String.valueOf(Math.round(total * 100.0) / 100.0));
            medaxilFaktura.setMebleg(newMebleg);
            medaxilFakturaObservableList.remove(index);
            medaxilFakturaObservableList.add(medaxilFaktura);
        }
        medaxilFakturaTableView.refresh();
    }

    public void createMalSiyahiView(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(medaxilFakturaPane.getScene().getWindow());
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
            selectedMal = controller.getSelectedItem();
            malTextField.setText(controller.getSelectedItem().getAd());
            mayaTextField.setText(String.valueOf(controller.getSelectedItem().getMaya()));

        }
    }

    public void createKreditorDialog(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(medaxilFakturaPane.getScene().getWindow());
        dialog.setTitle("Kreditor seçmək");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/example/defter/AdminFXML/KreditorSiyahiDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        KreditorSechimiDialog controller = fxmlLoader.getController();
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        controller.init(client);

        Optional<ButtonType> result= dialog.showAndWait();
        if ((result.isPresent() && result.get()== ButtonType.OK) && controller.getSelectedItem()!=null) {
            selectedKreditor = controller.getSelectedItem();
            kreditorTextField.setText(selectedKreditor.getAd());
        }
    }


    private void updateMeblegValue() {
        if (areFieldsValid(mayaTextField) && areFieldsValid(miqdarTextField)) {
            double satishQiymet = Double.parseDouble(mayaTextField.getText());
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
        if (malTextField.getText().isEmpty() || miqdarTextField.getText().isEmpty() || mayaTextField.getText().isEmpty()
                || mebleg.getText().isEmpty() || !areFieldsValid(mayaTextField)
                || !areFieldsValid(miqdarTextField) || kreditorTextField.getText().isEmpty()) {
            elaveEtButton.setDisable(true);
        } else {
            elaveEtButton.setDisable(false);
        }
    }

    public void elaveEt(){
        MedaxilFaktura medaxilFaktura  = new MedaxilFaktura(selectedMal.getId(),selectedMal.getAd(),
                Double.parseDouble(miqdarTextField.getText()),Double.parseDouble(mayaTextField.getText()),Double.parseDouble(meblegTextField.getText()));
        medaxilFakturaList.add(medaxilFaktura);
        total+=medaxilFaktura.getMebleg();
        totalTextField.setText(String.valueOf(Math.round(total * 100.0) / 100.0));
        initList();
    }

    public void handleDelete(){
        MedaxilFaktura item = medaxilFakturaTableView.getSelectionModel().getSelectedItem();
        if (item!=null){
            try {
                medaxilFakturaList.remove(item);
                total-=item.getMebleg();
                totalTextField.setText(String.valueOf(Math.round(total * 100.0) / 100.0));
                initList();
            }catch (Exception e){
                System.out.println(e.getMessage());
                Alert  alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Xəta");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }

    public void clearFields(){
        medaxilFakturaList.clear();
        medaxilFakturaObservableList.clear();
        kreditorTextField.clear();
        malTextField.clear();
        mayaTextField.clear();
        miqdarTextField.clear();
        meblegTextField.clear();
    }



    public void handleBack(){
        try {
            clearFields();
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

    public void handleBitti(){
        client.sendMessage("INSERTMEDAXIL");
        client.sendMessage(String.valueOf(datePicker.getValue()));
        client.sendMessage(String.valueOf(selectedKreditor.getId()));
        int medaxilNr = Integer.parseInt(client.reader());
        client.sendMessage("INSERTMEDAXILFAKTURA");
        for (MedaxilFaktura m : medaxilFakturaObservableList){
            client.sendMessage(m.getMalNr()+"/"+m.getMiqdar()+"/"+ m.getMaya() +"/"+
                    m.getMebleg()+"/" + medaxilNr);
        }
        client.sendMessage("DONE");
        try {
            clearFields();
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
        medaxilFakturaPane.getChildren().clear();
        medaxilFakturaPane.getChildren().add(node);
        FadeTransition ft = new FadeTransition(Duration.millis(1000));
        ft.setNode(node);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();

    }




}
