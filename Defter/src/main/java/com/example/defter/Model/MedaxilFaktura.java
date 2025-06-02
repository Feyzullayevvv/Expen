package com.example.defter.Model;

import java.io.Serializable;
import java.text.DecimalFormat;

public class MedaxilFaktura implements Serializable {
    private int id;
    private int malNr;
    private Long barcode;
    private String malKodu;
    private String tarix;
    private String mal;
    private double miqdar;
    private double maya;
    private double mebleg;
    private int medaxilNr;


    public MedaxilFaktura(int id, int malNr, Long barcode, String malKodu, String mal, double miqdar, double maya, double mebleg, int medaxilNr) {
        this.id = id;
        this.malNr = malNr;
        this.barcode = barcode;
        this.malKodu = malKodu;
        this.mal = mal;
        this.miqdar = miqdar;
        this.maya = maya;
        this.mebleg = mebleg;
        this.medaxilNr= medaxilNr;
    }


    public MedaxilFaktura(int malNr, String mal, double miqdar, double maya, double mebleg) {
        this.malNr = malNr;
        this.mal=mal;
        this.miqdar = miqdar;
        this.maya = maya;
        this.mebleg = mebleg;

    }
    public MedaxilFaktura(int malNr,Long barcode, String malKodu, String mal, double miqdar, double maya, double mebleg) {
        this.malNr = malNr;
        this.barcode = barcode;
        this.malKodu = malKodu;
        this.mal=mal;
        this.miqdar = miqdar;
        this.maya = maya;
        this.mebleg = mebleg;

    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMalNr() {
        return malNr;
    }

    public void setMalNr(int malNr) {
        this.malNr = malNr;
    }

    public double getMiqdar() {
        return Math.round(miqdar * 100.0) / 100.0;
    }

    public void setMiqdar(double miqdar) {
        this.miqdar = miqdar;
    }

    public double getMaya() {
        return Math.round(maya * 100.0) / 100.0;
    }

    public void setMaya(double maya) {
        this.maya = maya;
    }

    public double getMebleg() {
        return Math.round(mebleg * 100.0) / 100.0;
    }

    public void setMebleg(double mebleg) {
        this.mebleg = mebleg;
    }

    public int getMedaxilNr() {
        return medaxilNr;
    }

    public void setMedaxilNr(int medaxilNr) {
        this.medaxilNr = medaxilNr;
    }
    public String getMal() {
        return mal;
    }

    public void setMal(String mal) {
        this.mal = mal;
    }
    public Long getBarcode() {
        return barcode;
    }

    public void setBarcode(Long barcode) {
        this.barcode = barcode;
    }

    public String getMalKodu() {
        return malKodu;
    }

    public void setMalKodu(String malKodu) {
        this.malKodu = malKodu;
    }

    public String getTarix() {
        return tarix;
    }

    public void setTarix(String tarix) {
        this.tarix = tarix;
    }
}
