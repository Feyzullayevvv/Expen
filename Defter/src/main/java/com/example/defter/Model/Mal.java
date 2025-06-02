package com.example.defter.Model;

import java.io.Serializable;
import java.text.DecimalFormat;

public class Mal implements Serializable {
    private int id;
    private long barcode;
    private String kod;
    private String ad;
    private double maya;
    private double satish1;
    private double satish2;


    public Mal(int id, Long barcode, String kod, String ad, double maya, double satish1, double satish2) {
        this.id = id;
        this.barcode = barcode;
        this.kod = kod;
        this.ad = ad;
        this.maya = maya;
        this.satish1 = satish1;
        this.satish2 = satish2;
    }
    public Mal(Long barcode, String kod, String ad, double maya, double satish1, double satish2){
        this.barcode = barcode;
        this.kod = kod;
        this.ad = ad;
        this.maya = maya;
        this.satish1 = satish1;
        this.satish2 = satish2;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getBarcode() {
        return barcode;
    }

    public void setBarcode(long barcode) {
        this.barcode = barcode;
    }

    public String getKod() {
        return kod;
    }

    public void setKod(String kod) {
        this.kod = kod;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public double getMaya() {
         return Math.round(maya * 100.0) / 100.0;
    }

    public void setMaya(double maya) {
        this.maya = maya;
    }

    public double getSatish1() {
        return Math.round(satish1 * 100.0) / 100.0;
    }

    public void setSatish1(double satish1) {
        this.satish1 = satish1;
    }

    public double getSatish2() {
        return Math.round(satish2 * 100.0) / 100.0;
    }

    public void setSatish2(double satish2) {
        this.satish2 = satish2;
    }


}
