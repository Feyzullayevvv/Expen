package com.example.defter;

import com.example.defter.Data.Data;
import com.example.defter.Model.Anbar;
import com.example.defter.Model.Medaxil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



public class ExcelScript {
    private static List<Anbar> anbarList= new ArrayList<>();

    public static void main(String[] args) {
        Data data = new Data();
        data.open();
        anbarList = data.queryAnbar();
        data.close();
        handlePrintButtonAction();
    }

    public  static void handlePrintButtonAction() {
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
                headerRow.createCell(3).setCellValue("Miqdar");
                headerRow.createCell(4).setCellValue("Mebleg");


                int rowIndex = 1;
                for (Anbar anbar: anbarList) {
                    Row row = sheet.createRow(rowIndex++);
                    row.createCell(0).setCellValue(anbar.getId());
                    row.createCell(1).setCellValue(anbar.getMalId());
                    row.createCell(2).setCellValue(anbar.getMalAdi());
                    row.createCell(3).setCellValue(anbar.getMiqdar());
                    row.createCell(4).setCellValue(anbar.getMebleg());

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
}
