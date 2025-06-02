package com.example.defter.Admin;

import com.example.defter.Model.User;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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

public class UserListController {
    @FXML
    private AnchorPane userPane;
    @FXML
    private TableView<User> userTableView;
    @FXML
    private TableColumn<User, String> id;
    @FXML
    private TableColumn<User, String> user;
    @FXML
    private TableColumn<User, String> password;
    @FXML
    private Button yeniButton;
    @FXML
    private ProgressBar progressbar;
    private AdminClient client;
    private List<User> userList = new ArrayList<>();
    private ObservableList<User> userObservableList = FXCollections.observableArrayList();

    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    public void init(AdminClient client) {
        this.client = client;
        setVisible(false);
        progressbar.setVisible(true);
        progressbar.setProgress(0.2);

        CompletableFuture.runAsync(() -> {
            client.sendMessage("GETUSERLIST");
            userList = (List<User>) client.objectReader();
            Platform.runLater(() -> progressbar.setProgress(0.5));

            userObservableList.addAll(userList);

            Platform.runLater(() -> {
                id.setCellValueFactory(new PropertyValueFactory<>("id"));
                user.setCellValueFactory(new PropertyValueFactory<>("name"));
                password.setCellValueFactory(new PropertyValueFactory<>("password"));

                userTableView.setItems(userObservableList);
                setVisible(true);
                progressbar.setVisible(false);
            });
        }, executorService);
    }

    private void setVisible(boolean visible) {
        userTableView.setVisible(visible);
        yeniButton.setVisible(visible);
    }

    public void createNewUser() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(userPane.getScene().getWindow());
        dialog.setTitle("Yeni User");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/example/defter/AdminFXML/YeniUser.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        YeniUser controller = fxmlLoader.getController();
        controller.init(client);

        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);

        controller.getUserNameTextField().textProperty().addListener((observable, oldValue, newValue) -> {
            updateOkButtonDisableProperty(okButton, controller.getUserNameTextField(), controller.getPasswordTextField());
        });
        controller.getPasswordTextField().textProperty().addListener((observable, oldValue, newValue) -> {
            updateOkButtonDisableProperty(okButton, controller.getUserNameTextField(), controller.getPasswordTextField());
        });

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            controller.insertUser();
            init(client);
        }
    }

    public void handleClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            if (userTableView.getSelectionModel().getSelectedItem() != null) {
                User selectedItem = userTableView.getSelectionModel().getSelectedItem();
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.initOwner(userPane.getScene().getWindow());
                dialog.setTitle("Admin");
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/example/defter/AdminFXML/UserEdit.fxml"));
                try {
                    dialog.getDialogPane().setContent(fxmlLoader.load());
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
                dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
                dialog.initStyle(StageStyle.UTILITY);
                dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
                UserEditController controller = fxmlLoader.getController();
                controller.init(client, selectedItem);

                Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
                okButton.setDisable(true);

                controller.getUserNameTextField().textProperty().addListener((observable, oldValue, newValue) -> {
                    updateOkButtonDisableProperty(okButton, controller.getUserNameTextField(), controller.getPasswordTextField());
                });
                controller.getPasswordTextField().textProperty().addListener((observable, oldValue, newValue) -> {
                    updateOkButtonDisableProperty(okButton, controller.getUserNameTextField(), controller.getPasswordTextField());
                });
                Optional<ButtonType> result = dialog.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    controller.updateUser();
                    init(client);
                }
            }
        }
    }

    private void updateOkButtonDisableProperty(Button okButton, TextField userName, TextField password) {
        if (userName.getText().isEmpty() || password.getText().isEmpty()) {
            okButton.setDisable(true);
        } else {
            okButton.setDisable(false);
        }
    }

    public ProgressBar getProgressbar() {
        return progressbar;
    }
}
