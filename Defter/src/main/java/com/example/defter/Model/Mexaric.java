package com.example.defter.Model;

import java.io.Serializable;
import java.text.DecimalFormat;

public class Mexaric implements Serializable {
    private int id;
    private String tarix;
    private int debitorId;
    private String debitor;
    private double mebleg;

    public Mexaric(int id, String tarix, int debitorId,String debitor ,double mebleg) {
        this.id = id;
        this.tarix = tarix;
        this.debitorId = debitorId;
        this.debitor= debitor;
        this.mebleg = mebleg;
    }
    public Mexaric(int id, String tarix, int debitorId,double mebleg) {
        this.id = id;
        this.tarix = tarix;
        this.debitorId = debitorId;

        this.mebleg = mebleg;
    }

    public String getDebitor() {
        return debitor;
    }

    public void setDebitor(String debitor) {
        this.debitor = debitor;
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

    public int getDebitorId() {
        return debitorId;
    }

    public void setDebitorId(int debitorId) {
        this.debitorId = debitorId;
    }

    public double getMebleg() {
        return Math.round(mebleg * 100.0) / 100.0;
    }

    public void setMebleg(double mebleg) {
        this.mebleg = mebleg;
    }
}
