package com.example.defter.Admin;

import com.example.defter.Model.Kassa;
import com.example.defter.Model.Total;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class KassaController {
    @FXML
    private AnchorPane kassaPane;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private ChoiceBox<String> choiceBox;
    @FXML
    private TextField searchBar;
    @FXML
    private TableView<Kassa> kassaTableVIew;
    @FXML
    private TableColumn<Kassa, String> id;
    @FXML
    private TableColumn<Kassa, String> tarix;
    @FXML
    private TableColumn<Kassa, String> nov;
    @FXML
    private TableColumn<Kassa, String> qeyd;
    @FXML
    private TableColumn<Kassa, String> mebleg;
    @FXML
    private TextField total;
    @FXML
    private Button yeniButton;
    @FXML
    private ProgressBar progressbar;

    private List<Kassa> kassaList = new ArrayList<>();
    private ObservableList<Kassa> kassaObservableList = FXCollections.observableArrayList();
    private AdminClient client;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private AnchorPane pane;

    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    public void init(AdminClient client) {
        this.client = client;
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        tarix.setCellValueFactory(new PropertyValueFactory<>("tarix"));
        nov.setCellValueFactory(new PropertyValueFactory<>("nov"));
        qeyd.setCellValueFactory(new PropertyValueFactory<>("qeyd"));
        mebleg.setCellValueFactory(new PropertyValueFactory<>("mebleg"));

        startDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> showKassaEmeliyyatBetweenDates());
        endDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> showKassaEmeliyyatBetweenDates());

        choiceBox.getItems().addAll("Mədaxil", "Məxaric", "All");
        choiceBox.setValue("All");
        choiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                updateKassaEmeliyyat();
            }
        });

        initList();
    }

    public void initList() {
        setVisible(false);
        progressbar.setVisible(true);
        progressbar.setProgress(0.25);

        CompletableFuture.runAsync(() -> {
            client.sendMessage("GETKASSALIST");
            kassaList = (List<Kassa>) client.objectReader();
            Platform.runLater(() -> progressbar.setProgress(0.5)); // Update progress to 50%

            kassaObservableList.addAll(kassaList);

            Platform.runLater(() -> {
                kassaTableVIew.setItems(kassaObservableList);
                client.sendMessage("GETKASSATOTAL");
                Total total1 = (Total) client.itemReader();
                total.setText(String.valueOf(total1.getKassa()));

                // Set visibility to true after data is loaded
                setVisible(true);
                progressbar.setVisible(false);
            });
        }, executorService);
    }

    private void setVisible(boolean visible) {
        kassaTableVIew.setVisible(visible);
        startDatePicker.setVisible(visible);
        endDatePicker.setVisible(visible);
        choiceBox.setVisible(visible);
        searchBar.setVisible(visible);
        total.setVisible(visible);
        yeniButton.setVisible(visible);
    }

    public void showKassaEmeliyyatBetweenDates() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate != null && endDate != null) {
            try {
                Date startDateObj = dateFormatter.parse(startDate.toString());
                Date endDateObj = dateFormatter.parse(endDate.toString());

                List<Kassa> filteredSales = kassaList.stream()
                        .filter(kassa -> {
                            try {
                                Date paymentDate = dateFormatter.parse(kassa.getTarix());
                                return (paymentDate.equals(startDateObj) || paymentDate.after(startDateObj))
                                        && (paymentDate.before(endDateObj) || paymentDate.equals(endDateObj));
                            } catch (ParseException e) {
                                e.printStackTrace();
                                return false;
                            }
                        })
                        .collect(Collectors.toList());

                kassaObservableList.clear();
                kassaObservableList.addAll(filteredSales);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Xəta");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }

    public void handleKeyReleased(KeyEvent event) {
        if (event.getEventType() == KeyEvent.KEY_RELEASED) {
            String name = searchBar.getText();
            search(kassaObservableList, name);
        }
    }

    private void search(ObservableList<Kassa> kassaObservableList, String name) {
        kassaObservableList.clear();
        String lowercaseName = name.toLowerCase();
        for (int i = 0; i < kassaList.size(); i++) {
            if (kassaList.get(i).getQeyd().toLowerCase().contains(lowercaseName)) {
                kassaObservableList.add(kassaList.get(i));
            }
        }
    }

    public void updateKassaEmeliyyat() {
        if (choiceBox.getValue().equals("Mədaxil")) {
            kassaObservableList.clear();
            for (Kassa k : kassaList) {
                if (k.getNov().equals("Mədaxil")) {
                    kassaObservableList.add(k);
                }
            }
            kassaTableVIew.setItems(kassaObservableList);
        } else if (choiceBox.getValue().equals("Məxaric")) {
            kassaObservableList.clear();
            for (Kassa k : kassaList) {
                if (k.getNov().equals("Məxaric")) {
                    kassaObservableList.add(k);
                }
            }
            kassaTableVIew.setItems(kassaObservableList);
        } else {
            initList();
        }
    }

    public void createNewKassa() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/defter/AdminFXML/YeniKassaDebitorMedaxil.fxml"));
            pane = loader.load();
            YeniKassaDebitorMedaxil controller = loader.getController();
            controller.init(client);
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
        kassaPane.getChildren().clear();
        kassaPane.getChildren().add(node);
        FadeTransition ft = new FadeTransition(Duration.millis(1000));
        ft.setNode(node);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();
    }
    public ProgressBar getProgressBar() {
        return progressbar;
    }
}
