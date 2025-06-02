package com.example.defter.Admin;

import com.example.defter.Model.Debitor;
import com.example.defter.Model.MedaxilFaktura;
import com.example.defter.Model.MexaricFaktura;
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

public class DebitorInfoController {
    @FXML
    private AnchorPane debitorInfoPane;
    @FXML
    private TableView<MexaricFaktura> mexaricTableView;
    @FXML
    private TableColumn<MexaricFaktura,String> tarix;
    @FXML
    private TableColumn<MexaricFaktura,String> malId;
    @FXML
    private TableColumn<MexaricFaktura,String> malKodu;
    @FXML
    private TableColumn<MexaricFaktura,String> barcode;
    @FXML
    private TableColumn<MexaricFaktura,String> malAdi;
    @FXML
    private TableColumn<MexaricFaktura,String> satishQiymet;
    @FXML
    private TableColumn<MexaricFaktura,String> miqdar;
    @FXML
    private TableColumn<MexaricFaktura,String> mebleg;
    @FXML
    private TableColumn<MexaricFaktura,String> mexaricId;
    @FXML
    private TextField debitorTextField;
    @FXML
    private TextField borcTextField;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    private AnchorPane pane;
    private Debitor debitor;
    private AdminClient client;
    private List<MexaricFaktura> mexaricFakturaList = new ArrayList<>();
    private ObservableList<MexaricFaktura> mexaricFakturaObservableList = FXCollections.observableArrayList();
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");


    public void init(AdminClient client){
        this.client=client;
        tarix.setCellValueFactory(new PropertyValueFactory<>("tarix"));
        malId.setCellValueFactory(new PropertyValueFactory<>("malNr"));
        malKodu.setCellValueFactory(new PropertyValueFactory<>("malKodu"));
        barcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        malAdi.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        satishQiymet.setCellValueFactory(new PropertyValueFactory<>("satishQiymet"));
        miqdar.setCellValueFactory(new PropertyValueFactory<>("miqdar"));
        mebleg.setCellValueFactory(new PropertyValueFactory<>("mebleg"));
        mexaricId.setCellValueFactory(new PropertyValueFactory<>("mexaricId"));
        initList();
        startDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> showMedaxilBetweenDates());
        endDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> showMedaxilBetweenDates());

    }

    public void initList(){
        debitorTextField.clear();
        borcTextField.clear();
        mexaricFakturaList.clear();
        mexaricFakturaObservableList.clear();
        client.sendMessage("GETDEBITORINFOMEXARIC");
        client.sendMessage(String.valueOf(debitor.getId()));
        debitorTextField.setText(debitor.getAd());
        borcTextField.setText(String.valueOf(debitor.getBorc()));
        mexaricFakturaList= (List<MexaricFaktura>) client.objectReader();
        mexaricFakturaObservableList.addAll(mexaricFakturaList);
        mexaricTableView.setItems(mexaricFakturaObservableList);
    }
    public void showMedaxilBetweenDates() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate != null && endDate != null) {
            try {
                Date startDateObj = dateFormatter.parse(startDate.toString());
                Date endDateObj = dateFormatter.parse(endDate.toString());

                List<MexaricFaktura> filteredSales = mexaricFakturaList.stream()
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

                mexaricFakturaObservableList.clear();

                mexaricFakturaObservableList.addAll(filteredSales);
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
            debitorTextField.clear();
            borcTextField.clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/defter/AdminFXML/Debitor.fxml"));
            pane = loader.load();
            DebitorController controller = loader.getController();
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
        debitorInfoPane.getChildren().clear();
        debitorInfoPane.getChildren().add(node);
        FadeTransition ft = new FadeTransition(Duration.millis(1000));
        ft.setNode(node);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();

    }

    public void setDebitor(Debitor debitor){
        this.debitor=debitor;
    }
}
