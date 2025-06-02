package com.example.defter.Model;

import java.io.Serializable;
import java.text.DecimalFormat;

public class Anbar implements Serializable {
    private int id;
    private  int malId;
    private String malKod;
    private Long barcode;
    private String malAdi;
    private double miqdar;
    private double mebleg;



    public Anbar(int id, int malId, String malKod,Long barcode, String malAdi, double miqdar, double mebleg) {
        this.id = id;
        this.malId = malId;
        this.malKod = malKod;
        this.malAdi = malAdi;
        this.barcode=barcode;
        this.miqdar = miqdar;
        this.mebleg = mebleg;
    }
    public Anbar( int malId, String malKod,Long barcode, String malAdi, double miqdar, double mebleg) {

        this.malId = malId;
        this.malKod = malKod;
        this.malAdi = malAdi;
        this.barcode=barcode;
        this.miqdar = miqdar;
        this.mebleg = mebleg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMalId() {
        return malId;
    }

    public void setMalId(int malId) {
        this.malId = malId;
    }

    public String getMalAdi() {
        return malAdi;
    }

    public void setMalAdi(String malAdi) {
        this.malAdi = malAdi;
    }

    public double getMiqdar() {
        return Math.round(miqdar * 100.0) / 100.0;
    }

    public void setMiqdar(double miqdar) {
        this.miqdar = miqdar;
    }

    public double getMebleg() {
        return Math.round(mebleg * 100.0) / 100.0;
    }

    public void setMebleg(double mebleg) {
        this.mebleg = mebleg;
    }
    public String getMalKod() {
        return malKod;
    }
    public void setMalKod(String malKod) {
        this.malKod = malKod;
    }

    public Long getBarcode() {
        return barcode;
    }

    public void setBarcode(Long barcode) {
        this.barcode = barcode;
    }
}
