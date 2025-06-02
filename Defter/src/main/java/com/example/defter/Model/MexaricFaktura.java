package com.example.defter.Model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

public class MexaricFaktura implements Serializable {
    private int id;
    private int malNr;
    private double miqdar;
    private String tarix;
    private Long barcode;
    private String malKodu;
    private String mal;
    private double satishQiymet;
    private double mebleg;
    private int mexaricId;


    public MexaricFaktura(int id, int malNr, Long barcode, String malKodu, String mal, double miqdar, double satishQiymet, double mebleg,int mexaricId) {
        this.id = id;
        this.malNr = malNr;
        this.barcode=barcode;
        this.malKodu=malKodu;
        this.mal=mal;
        this.miqdar = miqdar;
        this.satishQiymet = satishQiymet;
        this.mebleg = mebleg;
        this.mexaricId = mexaricId;
    }
    public MexaricFaktura( int malNr,String mal, double miqdar, double satishQiymet, double mebleg) {

        this.malNr = malNr;
        this.miqdar = miqdar;
        this.mal=mal;
        this.satishQiymet = satishQiymet;
        this.mebleg = mebleg;

    }

    public MexaricFaktura(int malNr,Long barcode, String malKodu, String mal, double miqdar, double satishQiymet, double mebleg) {
        this.malNr = malNr;
        this.barcode=barcode;
        this.malKodu=malKodu;
        this.miqdar = miqdar;
        this.mal=mal;
        this.satishQiymet = satishQiymet;
        this.mebleg = mebleg;
    }

    public String getMal() {
        return mal;
    }

    public void setMal(String mal) {
        this.mal = mal;
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
        return miqdar;
    }

    public void setMiqdar(double miqdar) {
        this.miqdar = miqdar;
    }

    public double getSatishQiymet() {
        return Math.round(satishQiymet * 100.0) / 100.0;
    }

    public void setSatishQiymet(double satishQiymet) {
        this.satishQiymet = satishQiymet;
    }

    public double getMebleg() {
        return Math.round(mebleg * 100.0) / 100.0;
    }

    public void setMebleg(double mebleg) {
        this.mebleg = mebleg;
    }

    public int getMexaricId() {
        return mexaricId;
    }

    public void setMexaricId(int mexaricId) {
        this.mexaricId = mexaricId;
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
