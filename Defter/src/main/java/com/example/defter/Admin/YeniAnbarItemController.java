package com.example.defter.Admin;

import com.example.defter.Model.Anbar;
import com.example.defter.Model.Mal;
import com.example.defter.Model.MedaxilFaktura;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class YeniAnbarItemController {
    @FXML
    private AnchorPane yeniAnbarPane;
    @FXML
    private TableView<Anbar> anbarTableView;
    @FXML
    private TableColumn<Anbar,String> malId;
    @FXML
    private TableColumn<Anbar,String > malKod;
    @FXML
    private TableColumn<Anbar,String> barcode;
    @FXML
    private TableColumn<Anbar,String> mal;
    @FXML
    private TableColumn<Anbar,String> miqdar;
    @FXML
    private TableColumn<Anbar,String> mebleg;
    @FXML
    private TextField malTextField;
    @FXML
    private TextField miqdarTextField;
    @FXML
    private TextField meblegTextField;
    @FXML
    private Button silButton;
    @FXML
    private Button elaveEtButton;
    @FXML
    private Button bitirButton;
    private Mal selectedMal;
    private AdminClient client;
    private AnchorPane pane;
    private List<Anbar> anbarList = new ArrayList<>();
    private ObservableList<Anbar> anbarObservableList = FXCollections.observableArrayList();

    public void init(AdminClient client){
        this.client=client;
        malId.setCellValueFactory(new PropertyValueFactory<>("malId"));
        malKod.setCellValueFactory(new PropertyValueFactory<>("malKod"));
        mal.setCellValueFactory(new PropertyValueFactory<>("malAdi"));
        barcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        miqdar.setCellValueFactory(new PropertyValueFactory<>("miqdar"));
        mebleg.setCellValueFactory(new PropertyValueFactory<>("mebleg"));
        silButton.setDisable(true);
        bitirButton.setDisable(true);
        bitirButton.setDisable(true);
        malTextField.textProperty().addListener(((observableValue, s, t1) -> updateElaveEtButtonState()));
        miqdarTextField.textProperty().addListener(((observableValue, s, t1) -> updateElaveEtButtonState()));
        meblegTextField.textProperty().addListener(((observableValue, s, t1) -> updateElaveEtButtonState()));

    }

    public void initList(){
        malTextField.clear();
        miqdarTextField.clear();
        meblegTextField.clear();
        anbarObservableList.clear();
        anbarObservableList.addAll(anbarList);
        anbarTableView.setItems(anbarObservableList);
        if (anbarObservableList.isEmpty()){
            bitirButton.setDisable(true);
            silButton.setDisable(true);
        }else {
            bitirButton.setDisable(false);
            silButton.setDisable(false);
        }
    }
    public void createMalSiyahiView(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(yeniAnbarPane.getScene().getWindow());
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
        }

    }
    private void updateElaveEtButtonState() {
        if (malTextField.getText().isEmpty() || miqdarTextField.getText().isEmpty()
                || mebleg.getText().isEmpty()
                || !areFieldsValid(miqdarTextField) || !areFieldsValid(meblegTextField)) {
            elaveEtButton.setDisable(true);
        } else {
            elaveEtButton.setDisable(false);
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
    public void elaveEt(){
        Anbar anbar = new Anbar(selectedMal.getId(),selectedMal.getKod(),
                selectedMal.getBarcode(),selectedMal.getAd(),Double.parseDouble(miqdarTextField.getText()),
                Double.parseDouble(meblegTextField.getText()));
        anbarList.add(anbar);
        initList();
    }
    public void handleDelete(){
        Anbar item = anbarTableView.getSelectionModel().getSelectedItem();
        if (item!=null){
            try {
                anbarList.remove(item);
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
    private void setNode(Node node) {
        yeniAnbarPane.getChildren().clear();
        yeniAnbarPane.getChildren().add(node);
        FadeTransition ft = new FadeTransition(Duration.millis(1000));
        ft.setNode(node);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();

    }

    public void handleBitti(){
        clearFields();
        client.sendMessage("INSERTANBAR");
        for (Anbar a: anbarObservableList){
            client.sendMessage(a.getMalId() +  "/" + a.getMiqdar() + "/" + a.getMebleg());
        }
        client.sendMessage("DONE");
        createAnbarMenu();
    }
    private void clearFields(){
        malTextField.clear();
        meblegTextField.clear();
        miqdarTextField.clear();
    }

    public void handleBack(){
        clearFields();
        createAnbarMenu();
    }
    public void createAnbarMenu(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/defter/AdminFXML/Anbar.fxml"));
            pane = loader.load();
            AnbarController controller = loader.getController();
            controller.setClient(client);
            controller.init();
            setNode(pane);
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            e.printStackTrace();
            alert.setHeaderText("Xəta");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
