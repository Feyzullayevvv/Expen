package com.example.defter;

import com.example.defter.Data.Data;
import com.example.defter.Model.Mal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Script {
    private BufferedReader reader;
    private FileReader handleReader;
    private static final String pathData = "/Users/muhammadfeyzullayev/Desktop/malsiyahi_cleaned_filled.csv";

    public void open(){
        try {
            handleReader = new FileReader(pathData);
            reader = new BufferedReader(handleReader);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        Script script = new Script();
        script.open();
       List<Mal> list =  script.readMal();
        Data data = new Data();
        data.open();
       for (Mal m: list){
           data.insertNewMal(m.getBarcode(),m.getKod(),m.getAd(),m.getMaya(),m.getSatish1(),m.getSatish2());
       }
       data.close();
    }

    public List<Mal> readMal() {
        try {
            List<Mal> mals = new ArrayList<>();
            String line;
            reader.readLine(); // Skip the header line
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                String[] split = parseCSVLine(line);
                Long barcode = Long.parseLong(split[0].trim());
                String kod = split[1].trim();
                String ad = split[2].trim();
                double maya = Double.parseDouble(split[3].replace("\"", "").replace(",", ".").trim());
                double satish1 = Double.parseDouble(split[4].replace("\"", "").replace(",", ".").trim());
                double satish2 = Double.parseDouble(split[5].replace("\"", "").replace(",", ".").trim());

                Mal mal = new Mal(barcode, kod, ad, maya, satish1, satish2);
                mals.add(mal);
            }
            return mals;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String[] parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        boolean inQuotes = false;

        for (char ch : line.toCharArray()) {
            if (ch == '"') {
                inQuotes = !inQuotes; // toggle state
            } else if (ch == ',' && !inQuotes) {
                result.add(currentField.toString().trim());
                currentField.setLength(0);
            } else {
                currentField.append(ch);
            }
        }
        result.add(currentField.toString().trim());

        return result.toArray(new String[0]);
    }
}
