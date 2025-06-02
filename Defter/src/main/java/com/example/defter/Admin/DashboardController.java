package com.example.defter.Admin;

import com.example.defter.Model.*;
import com.example.defter.RoundUp;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.control.ProgressBar;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DashboardController {
    @FXML
    private TableView<Anbar> anbarTableView;
    @FXML
    private TableColumn<Anbar, String> malId;
    @FXML
    private TableColumn<Anbar, String> malKod;
    @FXML
    private TableColumn<Anbar, String> barcode;
    @FXML
    private TableColumn<Anbar, String> mal;
    @FXML
    private TableColumn<Anbar, String> miqdar;
    @FXML
    private TableColumn<Anbar, String> mebleg;
    @FXML
    private Pane firstPane;
    @FXML
    private Pane qaliqPane;
    @FXML
    private TextField anbarQaliq;
    @FXML
    private ImageView calculationButton;
    @FXML
    private ImageView generalInformationButton;
    @FXML
    private TextField satishTextField;
    @FXML
    private TextField satishSayTextField;
    @FXML
    private TextField medaxilTextField;
    @FXML
    private TextField medaxilSayTextField;
    @FXML
    private TextField menfeetTextField;
    @FXML
    private DatePicker beginDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private StackPane loadingPane;
    @FXML
    private ProgressBar progressBar;

    private List<Anbar> anbarList = Collections.synchronizedList(new ArrayList<>());
    private List<Mexaric> mexaricList = Collections.synchronizedList(new ArrayList<>());
    private List<Medaxil> medaxilList = Collections.synchronizedList(new ArrayList<>());
    private List<Mal> malList = Collections.synchronizedList(new ArrayList<>());
    private List<MexaricFaktura> mexaricFakturaList = Collections.synchronizedList(new ArrayList<>());
    private Map<Integer, List<MexaricFaktura>> mexaricFakturaMap = new HashMap<>();
    private Map<Integer, Mal> malMap = new HashMap<>();
    private AdminClient client;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private ObservableList<Anbar> anbarObservableList = FXCollections.observableArrayList();

    public double totalAnbar = 0;
    public double totalMexaricPrice = 0;
    public double totalMedaxilPrice = 0;
    public double totalMenfeetPrice = 0;
    public double totalMexaric = 0;
    public double totalMedaxil = 0;

    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    public void init() {
        anbarTableView.setVisible(false);
        showLoadingScreen(true);
        progressBar.setProgress(0);
//        CompletableFuture<Void> fetchDataFuture = CompletableFuture.allOf(
//                fetchDataAsync("GETANBARLIST", new ArrayList<>(anbarList)),
//                fetchDataAsync("GETMEXARICLIST", new ArrayList<>(mexaricList)),
//                fetchDataAsync("GETMEDAXILLIST", new ArrayList<>(medaxilList)),
//                fetchDataAsync("GETMALLIST", new ArrayList<>(malList)),
//                fetchDataAsync("GETALLMEXARICINFO", new ArrayList<>(mexaricFakturaList))
//        );
        progressBar.setProgress(0.2);
        client.sendMessage("GETANBARLIST");
        anbarList = (List<Anbar>) client.objectReader();
        client.sendMessage("GETMEXARICLIST");
        mexaricList = (List<Mexaric>) client.objectReader();
        client.sendMessage("GETMEDAXILLIST");
        medaxilList = (List<Medaxil>) client.objectReader();
        client.sendMessage("GETMALLIST");
        malList = (List<Mal>) client.objectReader();
        client.sendMessage("GETALLMEXARICINFO");
        mexaricFakturaList = (List<MexaricFaktura>) client.objectReader();
        progressBar.setVisible(false);
//        fetchDataFuture.thenRun(() -> {
//            Platform.runLater(() -> {
        for (MexaricFaktura mexaricFaktura : mexaricFakturaList) {
            mexaricFakturaMap
                    .computeIfAbsent(mexaricFaktura.getMexaricId(), k -> new ArrayList<>())
                    .add(mexaricFaktura);
        }
        for (Mal mal : malList) {
            malMap.put(mal.getId(), mal);
        }
        for (Anbar a : anbarList) {
            totalAnbar += a.getMebleg();
        }
        anbarQaliq.setText(RoundUp.roundUp(totalAnbar));
        generalInformation();
        hideLoadingScreen();
//            });
//        });

        malId.setCellValueFactory(new PropertyValueFactory<>("malId"));
        malKod.setCellValueFactory(new PropertyValueFactory<>("malKod"));
        mal.setCellValueFactory(new PropertyValueFactory<>("malAdi"));
        barcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        miqdar.setCellValueFactory(new PropertyValueFactory<>("miqdar"));
        mebleg.setCellValueFactory(new PropertyValueFactory<>("mebleg"));
    }

    private <T> CompletableFuture<Void> fetchDataAsync(String request, List<T> targetList) {
        return CompletableFuture.runAsync(() -> {
            client.sendMessage(request);
            List<T> dataList = (List<T>) client.objectReader();
            synchronized (targetList) {
                targetList.clear();
                targetList.addAll(dataList);
            }
            synchronized (this) {
                switch (request) {
                    case "GETANBARLIST":
                        anbarList.clear();
                        anbarList.addAll((List<Anbar>) targetList);
                        break;
                    case "GETMEXARICLIST":
                        mexaricList.clear();
                        mexaricList.addAll((List<Mexaric>) targetList);
                        break;
                    case "GETMEDAXILLIST":
                        medaxilList.clear();
                        medaxilList.addAll((List<Medaxil>) targetList);
                        break;
                    case "GETMALLIST":
                        malList.clear();
                        malList.addAll((List<Mal>) targetList);
                        break;
                    case "GETALLMEXARICINFO":
                        mexaricFakturaList.clear();
                        mexaricFakturaList.addAll((List<MexaricFaktura>) targetList);
                        break;
                }
            }
        }, executorService);
    }

    public void danger() {
        showLoadingScreen(true);
        CompletableFuture.runAsync(() -> {
            firstPane.setVisible(false);
            anbarTableView.setVisible(true);
            anbarObservableList.clear();
            LocalDate endDate = LocalDate.now();
            LocalDate beginDate = endDate.minusDays(30);

            for (Anbar anbar : anbarList) {
                if (anbar.getMiqdar() < 20) {
                    boolean wasSoldInLast30Days = mexaricList.stream()
                            .map(mexaric -> LocalDate.parse(mexaric.getTarix(), dateFormatter))
                            .anyMatch(mexaricDate -> (mexaricDate.isEqual(beginDate) || mexaricDate.isAfter(beginDate)) &&
                                    (mexaricDate.isEqual(endDate) || mexaricDate.isBefore(endDate)));
                    if (wasSoldInLast30Days) {
                        anbarObservableList.add(anbar);
                    }
                }
            }
            hideLoadingScreen();
            Platform.runLater(() -> {
                anbarTableView.setItems(anbarObservableList);

            });
        }, executorService);
    }

    public void generalInformation() {
        progressBar.setVisible(false);
        anbarTableView.setVisible(false);
        firstPane.setVisible(true);
        LocalDate today = LocalDate.now();
        LocalDate oneMonthAgo = today.minus(1, ChronoUnit.MONTHS);
        endDatePicker.setValue(today);
        beginDatePicker.setValue(oneMonthAgo);
        beginDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                performCalculations();
            }
        });

        endDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                performCalculations();
            }
        });
        performCalculations();
    }

    private void performCalculations() {
        resetFields();

        LocalDate beginDate = beginDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        for (Mexaric mexaric : mexaricList) {
            LocalDate mexaricDate = LocalDate.parse(mexaric.getTarix(), dateFormatter);
            if ((mexaricDate.isEqual(beginDate) || mexaricDate.isAfter(beginDate)) && (mexaricDate.isEqual(endDate) || mexaricDate.isBefore(endDate))) {
                totalMexaricPrice += mexaric.getMebleg();
                totalMexaric++;

                List<MexaricFaktura> relatedFakturas = mexaricFakturaMap.get(mexaric.getId());
                if (relatedFakturas != null) {
                    for (MexaricFaktura faktura : relatedFakturas) {
                        Mal mal = malMap.get(faktura.getMalNr());
                        if (mal != null) {
                            totalMenfeetPrice += ((faktura.getSatishQiymet() - mal.getMaya()) * faktura.getMiqdar());
                        }
                    }
                }
            }
        }

        for (Medaxil medaxil : medaxilList) {
            LocalDate medaxilDate = LocalDate.parse(medaxil.getTarix(), dateFormatter);
            if ((medaxilDate.isEqual(beginDate) || medaxilDate.isAfter(beginDate)) &&
                    (medaxilDate.isEqual(endDate) || medaxilDate.isBefore(endDate))) {
                totalMedaxilPrice += medaxil.getMebleg();
                totalMedaxil++;
            }
        }

        Platform.runLater(() -> {
            setFields();
            menfeetTextField.setText(RoundUp.roundUp(totalMenfeetPrice));
            hideLoadingScreen();
        });

    }

    public void resetFields() {
        totalMedaxilPrice = 0;
        totalMedaxil = 0;
        totalMexaric = 0;
        totalMexaricPrice = 0;
        totalMenfeetPrice = 0;
    }

    public void setFields() {
        satishTextField.setText(RoundUp.roundUp(totalMexaricPrice));
        satishSayTextField.setText(String.valueOf(totalMexaric));
        medaxilTextField.setText(RoundUp.roundUp(totalMedaxilPrice));
        medaxilSayTextField.setText(String.valueOf(totalMedaxil));
    }

    public void setClient(AdminClient client) {
        this.client = client;
    }

    private void showLoadingScreen(boolean show) {
        loadingPane.setVisible(show);
        progressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
    }

    private void hideLoadingScreen() {
        loadingPane.setVisible(false);
        progressBar.setVisible(false);
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }
}
