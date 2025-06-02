package com.example.defter.Admin;

import com.example.defter.Model.Mexaric;
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

public class MexaricController {
    private AdminClient client;
    @FXML
    private TableView<Mexaric> mexaricTableView;
    @FXML
    private TableColumn<Mexaric, String> id;
    @FXML
    private TableColumn<Mexaric, String> tarix;
    @FXML
    private TableColumn<Mexaric, String> debitor;
    @FXML
    private TableColumn<Mexaric, String> mebleg;
    @FXML
    private TextField searchBar;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private ToggleButton toggleButton;
    @FXML
    private AnchorPane mexaricPane;
    @FXML
    private ProgressBar progressbar;
    @FXML
    private Button yeniButton;
    private AnchorPane pane;
    private List<Mexaric> mexaricList = new ArrayList<>();
    private ObservableList<Mexaric> mexaricObservableList = FXCollections.observableArrayList();
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    public void init(AdminClient client) {
        this.client = client;
        setVisible(false);
        progressbar.setVisible(true);
        progressbar.setProgress(0.25);

        CompletableFuture.runAsync(() -> {
            client.sendMessage("GETMEXARICLIST");
            mexaricList = (List<Mexaric>) client.objectReader();
            Platform.runLater(() -> progressbar.setProgress(0.5));

            mexaricObservableList.addAll(mexaricList);

            Platform.runLater(() -> {
                id.setCellValueFactory(new PropertyValueFactory<>("id"));
                tarix.setCellValueFactory(new PropertyValueFactory<>("tarix"));
                debitor.setCellValueFactory(new PropertyValueFactory<>("debitor"));
                mebleg.setCellValueFactory(new PropertyValueFactory<>("mebleg"));

                mexaricTableView.setItems(mexaricObservableList);

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
                mexaricTableView.getSortOrder().clear();
                mexaricTableView.getSortOrder().add(tarix);
                mexaricTableView.sort();
            });
        }, executorService);

        startDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> showMexaricBetweenDates());
        endDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> showMexaricBetweenDates());
        toggleButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            showTodaysSales(newValue);
        });
    }

    public void initList() {
        setVisible(false);
        progressbar.setVisible(true);
        progressbar.setProgress(0);

        CompletableFuture.runAsync(() -> {
            client.sendMessage("GETMEXARICLIST");
            mexaricList = (List<Mexaric>) client.objectReader();
            Platform.runLater(() -> progressbar.setProgress(0.5));
            mexaricObservableList.clear();
            mexaricObservableList.addAll(mexaricList);

            Platform.runLater(() -> {
                mexaricTableView.setItems(mexaricObservableList);

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
                mexaricTableView.getSortOrder().clear();
                mexaricTableView.getSortOrder().add(tarix);
                mexaricTableView.sort();
            });
        }, executorService);
    }

    private void setVisible(boolean visible) {
        mexaricTableView.setVisible(visible);
        searchBar.setVisible(visible);
        startDatePicker.setVisible(visible);
        endDatePicker.setVisible(visible);
        toggleButton.setVisible(visible);
        yeniButton.setVisible(visible);
    }

    public void showMexaricBetweenDates() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate != null && endDate != null) {
            try {
                Date startDateObj = dateFormatter.parse(startDate.toString());
                Date endDateObj = dateFormatter.parse(endDate.toString());

                List<Mexaric> filteredSales = mexaricList.stream()
                        .filter(mexaric -> {
                            try {
                                Date paymentDate = dateFormatter.parse(mexaric.getTarix());
                                return (paymentDate.equals(startDateObj) || paymentDate.after(startDateObj))
                                        && (paymentDate.before(endDateObj) || paymentDate.equals(endDateObj));
                            } catch (ParseException e) {
                                e.printStackTrace();
                                return false;
                            }
                        })
                        .collect(Collectors.toList());

                mexaricObservableList.clear();
                mexaricObservableList.addAll(filteredSales);
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
            mexaricObservableList.clear();
            if (showTodaySales) {
                List<Mexaric> filteredSales = mexaricList.stream()
                        .filter(mexaric -> {
                            try {
                                Date saleDate = dateFormatter.parse(mexaric.getTarix());
                                LocalDate saleLocalDate = saleDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                                return saleLocalDate.isEqual(today);
                            } catch (ParseException e) {
                                e.printStackTrace();
                                return false;
                            }
                        })
                        .collect(Collectors.toList());

                mexaricObservableList.addAll(filteredSales);
            } else {
                mexaricObservableList.addAll(mexaricList);
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
            search(mexaricObservableList, name);
        }
    }

    private void search(ObservableList<Mexaric> mexaricObservableList, String name) {
        mexaricObservableList.clear();
        String lowercaseName = name.toLowerCase();
        for (int i = 0; i < mexaricList.size(); i++) {
            if (mexaricList.get(i).getDebitor().toLowerCase().contains(lowercaseName)) {
                mexaricObservableList.add(mexaricList.get(i));
            }
        }
    }

    public void createNewMexaric() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/defter/AdminFXML/YeniMexaric.fxml"));
            pane = loader.load();
            YeniMexaric controller = loader.getController();
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
            if (mexaricTableView.getSelectionModel().getSelectedItem() != null) {
                Mexaric selectedItem = mexaricTableView.getSelectionModel().getSelectedItem();
                MexaricInfoController mexaricInfoController = new MexaricInfoController();

                mexaricInfoController.setMexaric(selectedItem);
                createMexaricInfo(mexaricInfoController);
            }
        }
    }

    public void deleteItem(Mexaric mexaric) {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Məxarici silmək");
            alert.setHeaderText("Tarix: " + mexaric.getTarix() + "\nDebitor : " + mexaric.getDebitor());
            alert.setContentText("Əminsiniz?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && (result.get() == ButtonType.OK)) {
                client.sendMessage("DELETEMEXARIC");
                client.sendMessage(String.valueOf(mexaric.getId()));
                client.sendMessage(String.valueOf(mexaric.getDebitorId()));
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

    @FXML
    public void handleKeyPressed(KeyEvent keyEvent) {
        Mexaric selectedItem = mexaricTableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            if (keyEvent.getCode().equals(KeyCode.BACK_SPACE)) {
                deleteItem(selectedItem);
            }
        }
    }

    public void createMexaricInfo(MexaricInfoController controller) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/defter/AdminFXML/MexaricInfo.fxml"));
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
        mexaricPane.getChildren().clear();
        mexaricPane.getChildren().add(node);
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
