package com.example.defter.Admin;

import com.example.defter.Model.Kreditor;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
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
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KreditorController {
    private AdminClient client;
    private List<Kreditor> kreditorList = new ArrayList<>();
    private ObservableList<Kreditor> kreditorObservableList = FXCollections.observableArrayList();
    @FXML
    private TableView<Kreditor> kreditorTableView;
    @FXML
    private TableColumn<Kreditor, String> id;
    @FXML
    private TableColumn<Kreditor, String> ad;
    @FXML
    private TableColumn<Kreditor, String> borc;
    @FXML
    private AnchorPane kreditorPane;
    @FXML
    private TextField searchBar;
    private AnchorPane pane;
    @FXML
    private Button yeniButton;
    @FXML
    private ProgressBar progressbar;

    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    public void setClient(AdminClient client) {
        this.client = client;
    }

    public void init() {
        setVisible(false);
        progressbar.setVisible(true);
        progressbar.setProgress(0.25);

        CompletableFuture.runAsync(() -> {
            client.sendMessage("GETKREDITORLIST");
            kreditorList = (List<Kreditor>) client.objectReader();
            Platform.runLater(() -> progressbar.setProgress(0.5)); // Update progress to 50%

            kreditorObservableList.addAll(kreditorList);

            Platform.runLater(() -> {
                id.setCellValueFactory(new PropertyValueFactory<>("id"));
                ad.setCellValueFactory(new PropertyValueFactory<>("ad"));
                borc.setCellValueFactory(new PropertyValueFactory<>("borc"));

                kreditorTableView.setItems(kreditorObservableList);

                // Set visibility to true after data is loaded
                setVisible(true);
                progressbar.setVisible(false);
            });
        }, executorService);
    }

    private void setVisible(boolean visible) {
        kreditorTableView.setVisible(visible);
        searchBar.setVisible(visible);
        yeniButton.setVisible(visible);
    }

    public void createNewKreditor() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(kreditorPane.getScene().getWindow());
        dialog.setTitle("Yeni Kreditor");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/example/defter/AdminFXML/YeniKreditor.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        YeniKreditor controller = fxmlLoader.getController();
        controller.init(client);

        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);

        controller.getKreditorTextField().textProperty().addListener((observable, oldValue, newValue) -> {
            updateOkButtonDisableProperty(okButton, controller.getBorcTextField(), controller.getKreditorTextField());
        });
        controller.getBorcTextField().textProperty().addListener((observable, oldValue, newValue) -> {
            updateOkButtonDisableProperty(okButton, controller.getBorcTextField(), controller.getKreditorTextField());
        });

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            controller.insertDebitor();
            init();
        }
    }

    private void updateOkButtonDisableProperty(Button okButton, TextField borcTextField, TextField kreditorTextField) {
        if (borcTextField.getText().isEmpty() || kreditorTextField.getText().isEmpty() || !areFieldsValid(borcTextField)) {
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
            double itemValue = Double.parseDouble(itemPrice);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void handleKeyReleased(KeyEvent event) {
        if (event.getEventType() == KeyEvent.KEY_RELEASED) {
            String name = searchBar.getText();
            search(kreditorObservableList, name);
        }
    }

    private void search(ObservableList<Kreditor> kreditorObservableList, String name) {
        kreditorObservableList.clear();
        String lowercaseName = name.toLowerCase();
        for (int i = 0; i < kreditorList.size(); i++) {
            if (kreditorList.get(i).getAd().toLowerCase().contains(lowercaseName)) {
                kreditorObservableList.add(kreditorList.get(i));
            }
        }
    }

    public void handleClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            if (kreditorTableView.getSelectionModel().getSelectedItem() != null) {
                Kreditor selectedItem = kreditorTableView.getSelectionModel().getSelectedItem();
                KreditorInfoController kreditorInfoController = new KreditorInfoController();
                kreditorInfoController.setKreditor(selectedItem);
                createKreditorInfo(kreditorInfoController);
            }
        }
    }

    public void createKreditorInfo(KreditorInfoController controller) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/defter/AdminFXML/KreditorInfo.fxml"));
            loader.setController(controller);
            Parent root = loader.load();
            controller.init(client);
            setNode(root);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Xəta");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void setNode(Node node) {
        kreditorPane.getChildren().clear();
        kreditorPane.getChildren().add(node);
        FadeTransition ft = new FadeTransition(Duration.millis(1000));
        ft.setNode(node);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();
    }

    public ProgressBar getProgressbar() {
        return progressbar;
    }
}