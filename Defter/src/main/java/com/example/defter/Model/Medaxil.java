package com.example.defter.Model;

import java.io.Serializable;
import java.text.DecimalFormat;

public class Medaxil implements Serializable {
    private int id;
    private String tarix;
    private int kreditorId;
    private String kreditorAdi;
    private double mebleg;

    public Medaxil(int id, String tarix, int kreditorId, String kreditorAdi, double mebleg) {
        this.id = id;
        this.tarix = tarix;
        this.kreditorId = kreditorId;
        this.kreditorAdi = kreditorAdi;
        this.mebleg = mebleg;
    }
    public Medaxil(int id, String tarix, int kreditorId, double mebleg) {
        this.id = id;
        this.tarix = tarix;
        this.kreditorId = kreditorId;

        this.mebleg = mebleg;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTarix() {
        return tarix;
    }

    public void setTarix(String tarix) {
        this.tarix = tarix;
    }

    public int getKreditorId() {
        return kreditorId;
    }

    public void setKreditorId(int kreditorId) {
        this.kreditorId = kreditorId;
    }

    public String getKreditorAdi() {
        return kreditorAdi;
    }

    public void setKreditorAdi(String kreditorAdi) {
        this.kreditorAdi = kreditorAdi;
    }

    public double getMebleg() {
        return Math.round(mebleg * 100.0) / 100.0;
    }

    public void setMebleg(double mebleg) {
        this.mebleg = mebleg;
    }


}
