package com.example.defter.Admin;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Optional;

public class MainController {
    private AdminClient client;
    @FXML
    private AnchorPane holdPane;
    private AnchorPane pane;
    @FXML
    private Button dashBoardButton;
    @FXML
    private Button anbarButton;
    @FXML
    private Button kassaButton;
    @FXML
    private Button medaxilButton;
    @FXML
    private Button satishButton;
    @FXML
    private Button kreditorButton;
    @FXML
    private Button malSiyahiButton;
    @FXML
    private Button userButton;
    @FXML
    private Button debitorButton;

    public void showLoggedINUI() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/com/example/defter/AdminFXML/MainPage.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            fxmlLoader.setController(this);
            Stage stage = new Stage();
            stage.setTitle("ExPen");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void createDashBoard() {
        try {
//            disableButtons();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/defter/AdminFXML/DashBoard.fxml"));
            pane = loader.load();
            DashboardController controller = loader.getController();
            controller.setClient(client);
            controller.init();
            setNode(pane);
//            ProgressBar progressBar = controller.getProgressBar();
//            progressBar.visibleProperty().addListener((observable, oldValue, newValue) -> {
//                if (!newValue) {
//                    enableButtons();
//                }
//            });
        } catch (Exception e) {
            showErrorAlert(e);
        }
    }

    public void createAnbarMenu() {
        try {
            disableButtons();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/defter/AdminFXML/Anbar.fxml"));
            pane = loader.load();
            AnbarController controller = loader.getController();
            controller.setClient(client);
            controller.init();
            setNode(pane);
            ProgressBar progressBar = controller.getProgressbar();
            progressBar.visibleProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue) {
                    enableButtons();
                }
            });
        } catch (Exception e) {
            showErrorAlert(e);
        }
    }

    public void createMalSiyahiMenu() {
        try {
            disableButtons();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/defter/AdminFXML/MalSiyahi.fxml"));
            pane = loader.load();
            MalSiyahiController controller = loader.getController();
            controller.setClient(client);
            controller.init();
            setNode(pane);
            ProgressBar progressBar = controller.getProgressbar();
            progressBar.visibleProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue) {
                    enableButtons();
                }
            });
        } catch (Exception e) {
            showErrorAlert(e);
        }
    }

    public void createKreditorSiyahi() {
        try {
            disableButtons();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/defter/AdminFXML/Kreditor.fxml"));
            pane = loader.load();
            KreditorController controller = loader.getController();
            controller.setClient(client);
            controller.init();
            setNode(pane);
            ProgressBar progressBar = controller.getProgressbar();
            progressBar.visibleProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue) {
                    enableButtons();
                }
            });
        } catch (Exception e) {
            showErrorAlert(e);
        }
    }

    public void createDebitorMenu() {
        try {
            disableButtons();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/defter/AdminFXML/Debitor.fxml"));
            pane = loader.load();
            DebitorController controller = loader.getController();
            controller.setClient(client);
            controller.init();
            setNode(pane);
            ProgressBar progressBar = controller.getProgressBar();
            progressBar.visibleProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue) {
                    enableButtons();
                }
            });
        } catch (Exception e) {
            showErrorAlert(e);
        }
    }

    public void createMedaxilMenu() {
        try {
            disableButtons();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/defter/AdminFXML/Medaxil.fxml"));
            pane = loader.load();
            MedaxilController controller = loader.getController();
            controller.init(client);
            setNode(pane);
            ProgressBar progressBar = controller.getProgressBar();
            progressBar.visibleProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue) {
                    enableButtons();
                }
            });
        } catch (Exception e) {
            showErrorAlert(e);
        }
    }

    public void createMexaricMenu() {
        try {
            disableButtons();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/defter/AdminFXML/Mexaric.fxml"));
            pane = loader.load();
            MexaricController controller = loader.getController();
            controller.init(client);
            setNode(pane);
            ProgressBar progressBar = controller.getProgressBar();
            progressBar.visibleProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue) {
                    enableButtons();
                }
            });
        } catch (Exception e) {
            showErrorAlert(e);
        }
    }

    public void createKassaMenu() {
        try {
            disableButtons();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/defter/AdminFXML/Kassa.fxml"));
            pane = loader.load();
            KassaController controller = loader.getController();
            controller.init(client);
            setNode(pane);
            ProgressBar progressBar = controller.getProgressBar();
            progressBar.visibleProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue) {
                    enableButtons();
                }
            });
        } catch (Exception e) {
            showErrorAlert(e);
        }
    }

    public void createUserMenu() {
        try {
            disableButtons();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/defter/AdminFXML/UserList.fxml"));
            pane = loader.load();
            UserListController controller = loader.getController();
            controller.init(client);
            setNode(pane);
            ProgressBar progressBar = controller.getProgressbar();
            progressBar.visibleProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue) {
                    enableButtons();
                }
            });
        } catch (Exception e) {
            showErrorAlert(e);
        }
    }

    private void setNode(Node node) {
        holdPane.getChildren().clear();
        holdPane.getChildren().add(node);
        FadeTransition ft = new FadeTransition(Duration.millis(1000));
        ft.setNode(node);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();
    }

    private void disableButtons() {
        dashBoardButton.setDisable(true);
        anbarButton.setDisable(true);
        kassaButton.setDisable(true);
        medaxilButton.setDisable(true);
        satishButton.setDisable(true);
        debitorButton.setDisable(true);
        kreditorButton.setDisable(true);
        malSiyahiButton.setDisable(true);
        userButton.setDisable(true);
    }

    private void enableButtons() {
        dashBoardButton.setDisable(false);
        anbarButton.setDisable(false);
        kassaButton.setDisable(false);
        medaxilButton.setDisable(false);
        satishButton.setDisable(false);
        kreditorButton.setDisable(false);
        malSiyahiButton.setDisable(false);
        userButton.setDisable(false);
        debitorButton.setDisable(false);
    }

    public void handleExit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Çıxmağa Əminsiniz?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            client.sendMessage("EXIT");
            System.exit(0);
        }
    }

    private void showErrorAlert(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        e.printStackTrace();
        alert.setHeaderText("Xəta");
        alert.setContentText(e.getMessage());
        alert.showAndWait();
        enableButtons();
    }

    public void setClient(AdminClient client) {
        this.client = client;
    }
}
