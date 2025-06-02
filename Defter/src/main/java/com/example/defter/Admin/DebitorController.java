package com.example.defter.Admin;

import com.example.defter.Model.Debitor;
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
import javafx.scene.input.MouseButton;
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

public class DebitorController {
    private AdminClient client;
    @FXML
    private TableView<Debitor> debitorTableView;
    @FXML
    private TableColumn<Debitor, String> id;
    @FXML
    private TableColumn<Debitor, String> ad;
    @FXML
    private TableColumn<Debitor, String> borc;
    @FXML
    private TableColumn<Debitor, String> nomre;
    @FXML
    private AnchorPane debitorPane;
    @FXML
    private TextField searchBar;
    @FXML
    private Button yeniButton;
    @FXML
    private ProgressBar progressBar;

    private List<Debitor> debitorList = new ArrayList<>();
    private ObservableList<Debitor> debitorObservableList = FXCollections.observableArrayList();
    private ContextMenu contextMenu;
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    public void init() {
        debitorList.clear();
        debitorObservableList.clear();
        setVisible(false);
        progressBar.setVisible(true);
        progressBar.setProgress(0.25);

        CompletableFuture.runAsync(() -> {
            client.sendMessage("GETDEBITORLIST");
            debitorList = (List<Debitor>) client.objectReader();
            Platform.runLater(() -> progressBar.setProgress(0.5));

            debitorObservableList.addAll(debitorList);

            Platform.runLater(() -> {
                id.setCellValueFactory(new PropertyValueFactory<>("id"));
                ad.setCellValueFactory(new PropertyValueFactory<>("ad"));
                borc.setCellValueFactory(new PropertyValueFactory<>("borc"));
                nomre.setCellValueFactory(new PropertyValueFactory<>("nomre"));
                debitorTableView.setItems(debitorObservableList);
                setVisible(true);
                progressBar.setVisible(false);

                contextMenu = new ContextMenu();
                MenuItem editItem = new MenuItem("Edit");
                editItem.setOnAction(event -> showEditDialog());
                contextMenu.getItems().add(editItem);

                debitorTableView.setOnMouseClicked(event -> {
                    if (event.getButton() == MouseButton.SECONDARY && debitorTableView.getSelectionModel().getSelectedItem() != null) {
                        contextMenu.show(debitorTableView, event.getScreenX(), event.getScreenY());
                    } else {
                        contextMenu.hide();
                    }
                    if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                        handleClick(event);
                    }
                });
            });
        }, executorService);
    }

    private void setVisible(boolean visible) {
        debitorTableView.setVisible(visible);
        searchBar.setVisible(visible);
        yeniButton.setVisible(visible);
    }

    public void showEditDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(debitorPane.getScene().getWindow());
        dialog.setTitle("Edit Debitor");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/example/defter/AdminFXML/EditDebitor.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        EditDebitorController controller = fxmlLoader.getController();
        controller.init(client, debitorTableView.getSelectionModel().getSelectedItem());

        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);

        controller.getDebitorTextField().textProperty().addListener((observable, oldValue, newValue) -> {
            updateOkButtonDisableProperty(okButton, controller.getBorcTextField(), controller.getDebitorTextField(), controller.getNomreTextField());
        });
        controller.getBorcTextField().textProperty().addListener((observable, oldValue, newValue) -> {
            updateOkButtonDisableProperty(okButton, controller.getBorcTextField(), controller.getDebitorTextField(), controller.getNomreTextField());
        });
        controller.getNomreTextField().textProperty().addListener((observable, oldValue, newValue) -> {
            updateOkButtonDisableProperty(okButton, controller.getBorcTextField(), controller.getDebitorTextField(), controller.getNomreTextField());
        });

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            controller.insertDebitor();
            init();
        }
    }

    public void createNewDebitor() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(debitorPane.getScene().getWindow());
        dialog.setTitle("Yeni Debitor");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/example/defter/AdminFXML/YeniDebitor.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        YeniDebitor controller = fxmlLoader.getController();
        controller.init(client);

        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);

        controller.getDebitorTextField().textProperty().addListener((observable, oldValue, newValue) -> {
            updateOkButtonDisableProperty(okButton, controller.getBorcTextField(), controller.getDebitorTextField(), controller.getNomreTextField());
        });
        controller.getBorcTextField().textProperty().addListener((observable, oldValue, newValue) -> {
            updateOkButtonDisableProperty(okButton, controller.getBorcTextField(), controller.getDebitorTextField(), controller.getNomreTextField());
        });
        controller.getNomreTextField().textProperty().addListener((observable, oldValue, newValue) -> {
            updateOkButtonDisableProperty(okButton, controller.getBorcTextField(), controller.getDebitorTextField(), controller.getNomreTextField());
        });

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            controller.insertDebitor();
            init();
        }
    }

    public void handleClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            if (debitorTableView.getSelectionModel().getSelectedItem() != null) {
                Debitor selectedItem = debitorTableView.getSelectionModel().getSelectedItem();
                DebitorInfoController kreditorInfoController = new DebitorInfoController();
                kreditorInfoController.setDebitor(selectedItem);
                createDebitorInfo(kreditorInfoController);
            }
        }
    }

    public void createDebitorInfo(DebitorInfoController controller) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/defter/AdminFXML/DebitorInfo.fxml"));
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

    private void updateOkButtonDisableProperty(Button okButton, TextField borcTextField, TextField debitorTextField, TextField nomreTextField) {
        if (borcTextField.getText().isEmpty() || debitorTextField.getText().isEmpty() || !areFieldsValid(borcTextField) || nomreTextField.getText().isEmpty()) {
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

    public void handleKeyReleased(KeyEvent event) {
        if (event.getEventType() == KeyEvent.KEY_RELEASED) {
            String name = searchBar.getText();
            search(debitorObservableList, name);
        }
    }

    private void search(ObservableList<Debitor> debitorObservableList, String name) {
        debitorObservableList.clear();
        String lowercaseName = name.toLowerCase();
        for (int i = 0; i < debitorList.size(); i++) {
            if (debitorList.get(i).getAd().toLowerCase().contains(lowercaseName)) {
                debitorObservableList.add(debitorList.get(i));
            }
        }
    }

    private void setNode(Node node) {
        debitorPane.getChildren().clear();
        debitorPane.getChildren().add(node);
        FadeTransition ft = new FadeTransition(Duration.millis(1000));
        ft.setNode(node);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();
    }

    public void setClient(AdminClient client) {
        this.client = client;
    }
    public ProgressBar getProgressBar() {
        return progressBar;
    }
}
