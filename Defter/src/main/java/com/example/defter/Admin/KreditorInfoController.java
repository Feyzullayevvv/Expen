package com.example.defter.Admin;

import com.example.defter.Model.Kreditor;
import com.example.defter.Model.Medaxil;
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
import javafx.util.Duration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class KreditorInfoController {
    @FXML
    private AnchorPane kreditorInfoPane;
    @FXML
    private TextField kreditorTextField;
    @FXML
    private TextField borcTextField;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private TableView<MedaxilFaktura> kreditorInfoTableView;
    @FXML
    private TableColumn<MedaxilFaktura,String> tarix;
    @FXML
    private TableColumn<MedaxilFaktura,String> malId;
    @FXML
    private TableColumn<MedaxilFaktura,String> malKodu;
    @FXML
    private TableColumn<MedaxilFaktura,String> barcode;
    @FXML
    private TableColumn<MedaxilFaktura,String> malAdi;
    @FXML
    private TableColumn<MedaxilFaktura,String> maya;
    @FXML
    private TableColumn<MedaxilFaktura,String> miqdar;
    @FXML
    private TableColumn<MedaxilFaktura,String> mebleg;
    @FXML
    private TableColumn<MedaxilFaktura,String> medaxilNr;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

    private List<MedaxilFaktura> medaxilFakturaList= new ArrayList<>();
    private ObservableList<MedaxilFaktura> medaxilFakturaObservableList = FXCollections.observableArrayList();
    private Kreditor kreditor;
    private AdminClient client;
    private AnchorPane pane;
    public void init(AdminClient client){
        System.out.println(kreditor.getAd());
        this.client=client;
        malId.setCellValueFactory(new PropertyValueFactory<>("malNr"));
        malKodu.setCellValueFactory(new PropertyValueFactory<>("malKodu"));
        tarix.setCellValueFactory(new PropertyValueFactory<>("tarix"));
        malAdi.setCellValueFactory(new PropertyValueFactory<>("mal"));
        barcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        maya.setCellValueFactory(new PropertyValueFactory<>("maya"));
        miqdar.setCellValueFactory(new PropertyValueFactory<>("miqdar"));
        mebleg.setCellValueFactory(new PropertyValueFactory<>("mebleg"));
        medaxilNr.setCellValueFactory(new PropertyValueFactory<>("medaxilNr"));

        initList();
        startDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> showMedaxilBetweenDates());
        endDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> showMedaxilBetweenDates());

    }
    public void initList(){
        medaxilFakturaList.clear();
        medaxilFakturaObservableList.clear();
        kreditorTextField.clear();
        borcTextField.clear();
        kreditorTextField.setText(kreditor.getAd());
        borcTextField.setText(String.valueOf(kreditor.getBorc()));
        client.sendMessage("GETKREDITORINFO");
        client.sendMessage(String.valueOf(kreditor.getId()));
        medaxilFakturaList = (List<MedaxilFaktura>) client.objectReader();
        medaxilFakturaObservableList.addAll(medaxilFakturaList);
        kreditorInfoTableView.setItems(medaxilFakturaObservableList);
    }
    public void showMedaxilBetweenDates() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate != null && endDate != null) {
            try {
                Date startDateObj = dateFormatter.parse(startDate.toString());
                Date endDateObj = dateFormatter.parse(endDate.toString());

                List<MedaxilFaktura> filteredSales = medaxilFakturaList.stream()
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

                medaxilFakturaObservableList.clear();

                medaxilFakturaObservableList.addAll(filteredSales);
            } catch (Exception e){
                System.out.println(e.getMessage());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Xəta");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }
    public void handleBack(){
        try {
            kreditorTextField.clear();
            borcTextField.clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/defter/AdminFXML/Kreditor.fxml"));
            pane = loader.load();
            KreditorController controller = loader.getController();
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
    private void setNode(Node node) {
        kreditorInfoPane.getChildren().clear();
        kreditorInfoPane.getChildren().add(node);
        FadeTransition ft = new FadeTransition(Duration.millis(1000));
        ft.setNode(node);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();

    }

    public void setKreditor(Kreditor kreditor){
        this.kreditor=kreditor;
    }
}
