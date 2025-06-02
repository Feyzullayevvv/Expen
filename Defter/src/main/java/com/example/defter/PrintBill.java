package com.example.defter;

import com.example.defter.Model.Debitor;
import com.example.defter.Model.MexaricFaktura;
import com.example.defter.Model.print;
import javafx.scene.control.DatePicker;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.*;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrintBill {
    public void printBill(List<MexaricFaktura> mexaricFakturaList, String datePicker, String debitor) throws FileNotFoundException, JRException {
        List<print> printList = new ArrayList<>();
        double totalMebleg=0.0;
        for (MexaricFaktura m: mexaricFakturaList){
            print print = new print(m.getMal(),m.getMiqdar(),m.getSatishQiymet(),m.getMebleg());
            printList.add(print);
            totalMebleg+=m.getMebleg();
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String formattedValue = decimalFormat.format(totalMebleg);

        JRBeanCollectionDataSource itemsJrBean = new JRBeanCollectionDataSource(printList);
        Map<String,Object> parameters = new HashMap<>();
        parameters.put("CollectionBeanParam", itemsJrBean);
        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = currentTime.format(timeFormatter);
        parameters.put("Date", String.valueOf(datePicker) + " " + formattedTime);
        parameters.put("customerName",debitor);
        parameters.put("totalMebleg",formattedValue);


        InputStream input = new FileInputStream(new File("DefterProject/posCek.jrxml"));

        JasperDesign jasperDesign = JRXmlLoader.load(input);

        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

        JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
        jasperViewer.setTitle("Printed Bill");
        jasperViewer.setVisible(true);



    }
    public void printInvoice(List<MexaricFaktura> mexaricFakturaList, String datePicker, String debitor) throws FileNotFoundException, JRException {
        List<print> printList = new ArrayList<>();
        double totalMebleg=0.0;
        for (MexaricFaktura m: mexaricFakturaList){
            print print = new print(m.getMal(),m.getMiqdar(),m.getSatishQiymet(),m.getMebleg());
            printList.add(print);
            totalMebleg+=m.getMebleg();
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String formattedValue = decimalFormat.format(totalMebleg);

        JRBeanCollectionDataSource itemsJrBean = new JRBeanCollectionDataSource(printList);
        Map<String,Object> parameters = new HashMap<>();
        parameters.put("MyCollectionBeanParam", itemsJrBean);
        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = currentTime.format(timeFormatter);
        parameters.put("tarix", String.valueOf(datePicker) + " " + formattedTime);
        parameters.put("debitor",debitor);
        parameters.put("total",formattedValue);

        InputStream input = new FileInputStream(new File("DefterProject/Invoice_A4.jrxml"));

        JasperDesign jasperDesign = JRXmlLoader.load(input);

        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

        JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
        jasperViewer.setTitle("Printed Bill");
        jasperViewer.setVisible(true);


    }


}
