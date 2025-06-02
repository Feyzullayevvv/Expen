package com.example.defter.Admin;

import com.example.defter.Model.Medaxil;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class MedaxilController {
    private AdminClient client;
    @FXML
    private TableView<Medaxil> medaxilTableView;
    @FXML
    private TableColumn<Medaxil, String> id;
    @FXML
    private TableColumn<Medaxil, String> tarix;
    @FXML
    private TableColumn<Medaxil, String> kreditor;
    @FXML
    private TableColumn<Medaxil, String> mebleg;
    @FXML
    private TextField searchBar;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private ToggleButton toggleButton;
    @FXML
    private AnchorPane medaxilPane;
    @FXML
    private ProgressBar progressbar;
    @FXML
    private Button yeniButton;

    private AnchorPane pane;
    private List<Medaxil> medaxilList = new ArrayList<>();
    private ObservableList<Medaxil> medaxilObservableList = FXCollections.observableArrayList();
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    public void init(AdminClient client) {
        this.client = client;
        medaxilList.clear();
        medaxilObservableList.clear();
        setVisible(false);
        progressbar.setVisible(true);
        progressbar.setProgress(0.25);

        CompletableFuture.runAsync(() -> {
            client.sendMessage("GETMEDAXILLIST");
            medaxilList = (List<Medaxil>) client.objectReader();
            Platform.runLater(() -> progressbar.setProgress(0.5));

            medaxilObservableList.addAll(medaxilList);

            Platform.runLater(() -> {
                id.setCellValueFactory(new PropertyValueFactory<>("id"));
                tarix.setCellValueFactory(new PropertyValueFactory<>("tarix"));
                kreditor.setCellValueFactory(new PropertyValueFactory<>("kreditorAdi"));
                mebleg.setCellValueFactory(new PropertyValueFactory<>("mebleg"));

                medaxilTableView.setItems(medaxilObservableList);

                setVisible(true);
                progressbar.setVisible(false);

                tarix.setSortType(TableColumn.SortType.ASCENDING);
                tarix.setComparator((tarix1, tarix2) -> {
                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date date1 = dateFormat.parse(tarix1);
                        Date date2 = dateFormat.parse(tarix2);
                        return date1.compareTo(date2);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return 0;
                    }
                });
                medaxilTableView.getSortOrder().clear();
                medaxilTableView.getSortOrder().add(tarix);
                medaxilTableView.sort();
            });
        }, executorService);

        startDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> showMedaxilBetweenDates());
        endDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> showMedaxilBetweenDates());
        toggleButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            showTodaysSales(newValue);
        });
    }

    public void initList() {
        setVisible(false);
        progressbar.setVisible(true);
        progressbar.setProgress(0);

        CompletableFuture.runAsync(() -> {
            client.sendMessage("GETMEDAXILLIST");
            medaxilList = (List<Medaxil>) client.objectReader();
            Platform.runLater(() -> progressbar.setProgress(0.5)); // Update progress to 50%

            medaxilObservableList.clear();
            medaxilObservableList.addAll(medaxilList);

            Platform.runLater(() -> {
                medaxilTableView.setItems(medaxilObservableList);

                // Set visibility to true after data is loaded
                setVisible(true);
                progressbar.setVisible(false);

                // Set up sorting
                tarix.setSortType(TableColumn.SortType.ASCENDING);
                tarix.setComparator((tarix1, tarix2) -> {
                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date date1 = dateFormat.parse(tarix1);
                        Date date2 = dateFormat.parse(tarix2);
                        return date1.compareTo(date2);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return 0;
                    }
                });
                medaxilTableView.getSortOrder().clear();
                medaxilTableView.getSortOrder().add(tarix);
                medaxilTableView.sort();
            });
        }, executorService);
    }

    private void setVisible(boolean visible) {
        medaxilTableView.setVisible(visible);
        searchBar.setVisible(visible);
        startDatePicker.setVisible(visible);
        endDatePicker.setVisible(visible);
        toggleButton.setVisible(visible);
        yeniButton.setVisible(visible);
    }

    public void showMedaxilBetweenDates() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate != null && endDate != null) {
            try {
                Date startDateObj = dateFormatter.parse(startDate.toString());
                Date endDateObj = dateFormatter.parse(endDate.toString());

                List<Medaxil> filteredSales = medaxilList.stream()
                        .filter(medaxil -> {
                            try {
                                Date paymentDate = dateFormatter.parse(medaxil.getTarix());
                                return (paymentDate.equals(startDateObj) || paymentDate.after(startDateObj))
                                        && (paymentDate.before(endDateObj) || paymentDate.equals(endDateObj));
                            } catch (ParseException e) {
                                e.printStackTrace();
                                return false;
                            }
                        })
                        .collect(Collectors.toList());

                medaxilObservableList.clear();
                medaxilObservableList.addAll(filteredSales);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Xəta");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }

    public void showTodaysSales(boolean showTodaySales) {
        try {
            LocalDate today = LocalDate.now();
            medaxilObservableList.clear();
            if (showTodaySales) {
                List<Medaxil> filteredSales = medaxilList.stream()
                        .filter(medaxil -> {
                            try {
                                Date saleDate = dateFormatter.parse(medaxil.getTarix());
                                LocalDate saleLocalDate = saleDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                                return saleLocalDate.isEqual(today);
                            } catch (ParseException e) {
                                e.printStackTrace();
                                return false;
                            }
                        })
                        .collect(Collectors.toList());

                medaxilObservableList.addAll(filteredSales);
            } else {
                medaxilObservableList.addAll(medaxilList);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Xəta");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void handleKeyReleased(KeyEvent event) {
        if (event.getEventType() == KeyEvent.KEY_RELEASED) {
            String name = searchBar.getText();
            search(medaxilObservableList, name);
        }
    }

    private void search(ObservableList<Medaxil> medaxilObservableList, String name) {
        medaxilObservableList.clear();
        String lowercaseName = name.toLowerCase();
        for (int i = 0; i < medaxilList.size(); i++) {
            if (medaxilList.get(i).getKreditorAdi().toLowerCase().contains(lowercaseName)) {
                medaxilObservableList.add(medaxilList.get(i));
            }
        }
    }

    public void createNewMedaxil() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/defter/AdminFXML/YeniMedaxil.fxml"));
            pane = loader.load();
            YeniMedaxil controller = loader.getController();
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

    public void handleClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            if (medaxilTableView.getSelectionModel().getSelectedItem() != null) {
                Medaxil selectedItem = medaxilTableView.getSelectionModel().getSelectedItem();
                MedaxilInfoController medaxilinFoController = new MedaxilInfoController();
                medaxilinFoController.setMedaxil(selectedItem);
                createMedaxilInfo(medaxilinFoController);
            }
        }
    }

    public void createMedaxilInfo(MedaxilInfoController controller) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/defter/AdminFXML/MedaxilInfo.fxml"));
            loader.setController(controller);
            Parent root = loader.load();
            controller.init(client);
            setNode(root);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Xəta");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void setNode(Node node) {
        medaxilPane.getChildren().clear();
        medaxilPane.getChildren().add(node);
        FadeTransition ft = new FadeTransition(Duration.millis(1000));
        ft.setNode(node);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();
    }

    @FXML
    public void handleKeyPressed(KeyEvent keyEvent) {
        Medaxil selectedItem = medaxilTableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            if (keyEvent.getCode().equals(KeyCode.BACK_SPACE)) {
                deleteItem(selectedItem);
            }
        }
    }

    public void deleteItem(Medaxil medaxil) {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Mədaxili silmək");
            alert.setHeaderText("Kreditor adı: " + medaxil.getKreditorAdi() + "\n Məbləğ: " + medaxil.getMebleg());
            alert.setContentText("Əminsiniz?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && (result.get() == ButtonType.OK)) {
                client.sendMessage("DELETEMEDAXIL");
                client.sendMessage(String.valueOf(medaxil.getId()));
                client.sendMessage(String.valueOf(medaxil.getKreditorId()));
                initList();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Xəta");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    public ProgressBar getProgressBar() {
        return progressbar;
    }
}
