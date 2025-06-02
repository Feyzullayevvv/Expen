package com.example.defter.Admin;

import com.example.defter.Model.Anbar;
import com.example.defter.Model.Mal;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AnbarController {
    private AdminClient client;
    private List<Anbar> anbarList = new ArrayList<>();
    private ObservableList<Anbar> anbarObservableList = FXCollections.observableArrayList();
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
    private TextField malAdiSearchBar;
    @FXML
    private TextField malKoduSearchBar;
    @FXML
    private TextField malBarcoduSearchBar;
    @FXML
    private AnchorPane anbarPane;
    @FXML
    private Button yeniButton;
    @FXML
    private ProgressBar progressbar;
    private String malAdiSearchText = "";
    private String malKoduSearchText = "";
    private String malBarcodeSearchText = "";
    private AnchorPane pane;

    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    public void setClient(AdminClient client) {
        this.client = client;
    }

    public void init() {
        setVisible(false);
        progressbar.setVisible(true);
        progressbar.setProgress(0.2);

        CompletableFuture.runAsync(() -> {
            client.sendMessage("GETANBARLIST");
            anbarList = (List<Anbar>) client.objectReader();
            Platform.runLater(() -> progressbar.setProgress(0.5));

            anbarObservableList.addAll(anbarList);

            Platform.runLater(() -> {
                malId.setCellValueFactory(new PropertyValueFactory<>("malId"));
                malKod.setCellValueFactory(new PropertyValueFactory<>("malKod"));
                mal.setCellValueFactory(new PropertyValueFactory<>("malAdi"));
                barcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
                miqdar.setCellValueFactory(new PropertyValueFactory<>("miqdar"));
                mebleg.setCellValueFactory(new PropertyValueFactory<>("mebleg"));

                anbarTableView.setItems(anbarObservableList);
                setVisible(true);
                progressbar.setVisible(false);
            });
        }, executorService);
    }

    private void setVisible(boolean visible) {
        anbarTableView.setVisible(visible);
        malAdiSearchBar.setVisible(visible);
        malKoduSearchBar.setVisible(visible);
        malBarcoduSearchBar.setVisible(visible);
        yeniButton.setVisible(visible);
    }

    public void handleKeyReleased(KeyEvent event) {
        if (event.getEventType() == KeyEvent.KEY_RELEASED) {
            if (event.getSource() == malAdiSearchBar) {
                malAdiSearchText = malAdiSearchBar.getText();
            } else if (event.getSource() == malKoduSearchBar) {
                malKoduSearchText = malKoduSearchBar.getText();
            } else if (event.getSource() == malBarcoduSearchBar) {
                malBarcodeSearchText = malBarcoduSearchBar.getText();
            }

            searchByProperties();
        }
    }

    private void searchByProperties() {
        anbarObservableList.clear();

        String lowercaseAdi = malAdiSearchText.toLowerCase();
        String lowercaseKod = malKoduSearchText.toLowerCase();
        String lowercaseBarcode = malBarcodeSearchText.toLowerCase();

        for (Anbar mal : anbarList) {
            String adi = getPropertyByReflection(mal, "malAdi");
            String kod = getPropertyByReflection(mal, "malKod");
            String barcode = getPropertyByReflection(mal, "barcode");

            boolean matchesAdi = adi != null && adi.toLowerCase().contains(lowercaseAdi);
            boolean matchesKod = kod != null && kod.toLowerCase().contains(lowercaseKod);
            boolean matchesBarcode = barcode != null && barcode.toLowerCase().contains(lowercaseBarcode);

            if (matchesAdi && matchesKod && matchesBarcode) {
                anbarObservableList.add(mal);
            }
        }
    }

    public void handleClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            if (anbarTableView.getSelectionModel().getSelectedItem() != null) {
                Anbar selectedItem = anbarTableView.getSelectionModel().getSelectedItem();
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.initOwner(anbarPane.getScene().getWindow());
                dialog.setTitle("Admin");
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/example/defter/AdminFXML/AnbarEdit.fxml"));
                try {
                    dialog.getDialogPane().setContent(fxmlLoader.load());
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
                dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
                dialog.initStyle(StageStyle.UTILITY);
                dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
                AnbarEditController controller = fxmlLoader.getController();
                controller.init(client, selectedItem);

                Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
                okButton.setDisable(true);

                controller.getMiqdar().textProperty().addListener((observable, oldValue, newValue) -> {
                    updateOkButtonDisableProperty(okButton, controller.getMebleg(), controller.getMiqdar());
                });

                controller.getMebleg().textProperty().addListener((observable, oldValue, newValue) -> {
                    updateOkButtonDisableProperty(okButton, controller.getMebleg(), controller.getMiqdar());
                });
                Optional<ButtonType> result = dialog.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    controller.updateAnbarItem();
                }
                init();
            }
        }
    }

    private void updateOkButtonDisableProperty(Button okButton, TextField meblegText, TextField miqdarTextField) {
        if (miqdarTextField.getText().isEmpty() || !areFieldsValid(miqdarTextField) || !areFieldsValid(meblegText) || meblegText.getText().isEmpty()) {
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

    public void createInsertAnbar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/defter/AdminFXML/YeniAnbarItem.fxml"));
            pane = loader.load();
            YeniAnbarItemController controller = loader.getController();
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

    private String getPropertyByReflection(Anbar mal, String propertyName) {
        try {
            Field field = Anbar.class.getDeclaredField(propertyName);
            field.setAccessible(true);
            Object value = field.get(mal);
            return value != null ? value.toString() : null;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setNode(Node node) {
        anbarPane.getChildren().clear();
        anbarPane.getChildren().add(node);
        FadeTransition ft = new FadeTransition(Duration.millis(1000));
        ft.setNode(node);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();
    }

    public void handlePrintButtonAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Table Data");

                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("id");
                headerRow.createCell(1).setCellValue("MalId");
                headerRow.createCell(2).setCellValue("Mal Adi");
                headerRow.createCell(3).setCellValue("satish 1");
                headerRow.createCell(4).setCellValue("satish 2");
                headerRow.createCell(5).setCellValue("Maya");
                headerRow.createCell(6).setCellValue("Miqdar");
                headerRow.createCell(7).setCellValue("Mebleg");
                headerRow.createCell(8).setCellValue("Barcode");


                int rowIndex = 1;
                for (Anbar anbar: anbarObservableList) {
                    Row row = sheet.createRow(rowIndex++);
                    row.createCell(0).setCellValue(anbar.getId());
                    row.createCell(1).setCellValue(anbar.getMalId());
                    row.createCell(2).setCellValue(anbar.getMalAdi());
                    client.sendMessage("GETMAL");
                    client.sendMessage(String.valueOf(anbar.getMalId()));
                    Mal mal = (Mal) client.itemReader();
                    row.createCell(3).setCellValue(mal.getSatish1());
                    row.createCell(4).setCellValue(mal.getSatish2());
                    row.createCell(5).setCellValue(mal.getMaya());
                    row.createCell(6).setCellValue(anbar.getMiqdar());
                    row.createCell(7).setCellValue(anbar.getMebleg());
                    row.createCell(8).setCellValue(mal.getBarcode());


                }

                for (int i = 0; i < 4; i++) {
                    sheet.autoSizeColumn(i);
                }

                try (FileOutputStream fileOut = new FileOutputStream(file)) {
                    workbook.write(fileOut);
                }

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Print");
                alert.setHeaderText(null);
                alert.setContentText("Table data printed to Excel successfully.");
                alert.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("An error occurred while printing table data to Excel.");
                alert.showAndWait();
            }
        }
    }

    public ProgressBar getProgressbar() {
        return progressbar;
    }
}
