package com.example.defter.Admin;

import com.example.defter.Model.Mal;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
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
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MalSiyahiController {
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
    @FXML
    private AnchorPane malSiyahiPane;

    @FXML
    private TextField malAdiSearchBar;
    @FXML
    private TextField malKoduSearchBar;
    @FXML
    private TextField malBarcoduSearchBar;
    @FXML
    private ProgressBar progressbar;
    @FXML
    private Button yeniButton;
    private String malAdiSearchText = "";
    private String malKoduSearchText = "";
    private String malBarcodeSearchText = "";

    private AnchorPane pane;

    private AdminClient client;
    private List<Mal> malList = new ArrayList<>();
    private ObservableList<Mal> malObservableList = FXCollections.observableArrayList();
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    public void init() {
        setVisible(false);
        progressbar.setVisible(true);
        progressbar.setProgress(0);
        CompletableFuture.runAsync(() -> {
            Platform.runLater(() -> progressbar.setProgress(0.25));
            client.sendMessage("GETMALLIST");
            malList = (List<Mal>) client.objectReader();
            malObservableList.addAll(malList);
            Platform.runLater(() -> progressbar.setProgress(0.5));
            Platform.runLater(() -> {
                id.setCellValueFactory(new PropertyValueFactory<>("id"));
                barcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
                kod.setCellValueFactory(new PropertyValueFactory<>("kod"));
                malAdi.setCellValueFactory(new PropertyValueFactory<>("ad"));
                maya.setCellValueFactory(new PropertyValueFactory<>("maya"));
                Platform.runLater(() -> progressbar.setProgress(0.75));
                satish1.setCellValueFactory(new PropertyValueFactory<>("satish1"));
                satish2.setCellValueFactory(new PropertyValueFactory<>("satish2"));
                Platform.runLater(() -> progressbar.setProgress(1));
                malTableView.setItems(malObservableList);
                setVisible(true);
                progressbar.setVisible(false);
            });
        }, executorService);
    }

    private void setVisible(boolean visible) {
        malTableView.setVisible(visible);
        malAdiSearchBar.setVisible(visible);
        malKoduSearchBar.setVisible(visible);
        malBarcoduSearchBar.setVisible(visible);
        yeniButton.setVisible(visible);

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

    public void handleYeniMal(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/defter/AdminFXML/YeniMalSiyahi.fxml"));
            pane = loader.load();
            YeniMalSiyahiController controller = loader.getController();
            controller.setClient(client);
            controller.init();
            setNode(pane);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            e.printStackTrace();
            alert.setHeaderText("Xəta");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void setNode(Node node) {
        malSiyahiPane.getChildren().clear();
        malSiyahiPane.getChildren().add(node);
        FadeTransition ft = new FadeTransition(Duration.millis(1000));
        ft.setNode(node);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();
    }

    public void handleClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            if (malTableView.getSelectionModel().getSelectedItem() != null) {
                Mal selectedItem = malTableView.getSelectionModel().getSelectedItem();
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.initOwner(malSiyahiPane.getScene().getWindow());
                dialog.setTitle("Redaktə Et");
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/example/defter/AdminFXML/MalSiyahiDialog.fxml"));
                try {
                    dialog.getDialogPane().setContent(fxmlLoader.load());
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
                dialog.getDialogPane().getButtonTypes().add(ButtonType.APPLY);
                dialog.initStyle(StageStyle.UTILITY);
                dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
                MalSiyahiDialog controller = fxmlLoader.getController();
                controller.init(client, selectedItem);

                Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.APPLY);
                okButton.setDisable(true);

                controller.getBarcodeTextField().textProperty().addListener((observable, oldValue, newValue) -> {
                    updateOkButtonDisableProperty(okButton, controller.getBarcodeTextField(), controller.getKodTextField(), controller.getAdTextField(),
                            controller.getMayaTextField(), controller.getSatish1TextField(), controller.getSatish2TextField());
                });
                controller.getKodTextField().textProperty().addListener((observable, oldValue, newValue) -> {
                    updateOkButtonDisableProperty(okButton, controller.getBarcodeTextField(), controller.getKodTextField(), controller.getAdTextField(),
                            controller.getMayaTextField(), controller.getSatish1TextField(), controller.getSatish2TextField());
                });
                controller.getAdTextField().textProperty().addListener((observable, oldValue, newValue) -> {
                    updateOkButtonDisableProperty(okButton, controller.getBarcodeTextField(), controller.getKodTextField(), controller.getAdTextField(),
                            controller.getMayaTextField(), controller.getSatish1TextField(), controller.getSatish2TextField());
                });
                controller.getMayaTextField().textProperty().addListener((observable, oldValue, newValue) -> {
                    updateOkButtonDisableProperty(okButton, controller.getBarcodeTextField(), controller.getKodTextField(), controller.getAdTextField(),
                            controller.getMayaTextField(), controller.getSatish1TextField(), controller.getSatish2TextField());
                });
                controller.getSatish1TextField().textProperty().addListener((observable, oldValue, newValue) -> {
                    updateOkButtonDisableProperty(okButton, controller.getBarcodeTextField(), controller.getKodTextField(), controller.getAdTextField(),
                            controller.getMayaTextField(), controller.getSatish1TextField(), controller.getSatish2TextField());
                });
                controller.getSatish2TextField().textProperty().addListener((observable, oldValue, newValue) -> {
                    updateOkButtonDisableProperty(okButton, controller.getBarcodeTextField(), controller.getKodTextField(), controller.getAdTextField(),
                            controller.getMayaTextField(), controller.getSatish1TextField(), controller.getSatish2TextField());
                });
                Optional<ButtonType> result = dialog.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.APPLY) {
                    controller.updateMal();
                }
                init();
            }
        }
    }

    private void updateOkButtonDisableProperty(Button okButton, TextField barcode, TextField kod, TextField ad, TextField maya, TextField satish1, TextField satish2) {
        if (barcode.getText().isEmpty() || !areFieldsValid(maya) || !areFieldsValid(satish1) || !areFieldsValid(satish2) || kod.getText().isEmpty() ||
                ad.getText().isEmpty() || maya.getText().isEmpty() || satish1.getText().isEmpty() || satish2.getText().isEmpty()) {
            okButton.setDisable(true);
        } else {
            okButton.setDisable(false);
        }
    }

    public boolean areFieldsValid(TextField n) {
        String itemPrice = n.getText();
        if (itemPrice.isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(itemPrice);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void setClient(AdminClient client) {
        this.client = client;
    }

    public ProgressBar getProgressbar() {
        return progressbar;
    }
}
