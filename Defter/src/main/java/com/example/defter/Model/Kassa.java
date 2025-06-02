package com.example.defter.Model;

import java.io.Serializable;
import java.text.DecimalFormat;

public class Kassa implements Serializable {
    private int id;
    private String nov;
    private String tarix;
    private String qeyd;

    private  double mebleg;

    public Kassa(int id, String nov, String tarix, String qeyd, double mebleg) {
        this.id = id;
        this.nov = nov;
        this.tarix = tarix;
        this.qeyd = qeyd;
        this.mebleg = mebleg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNov() {
        return nov;
    }

    public void setNov(String nov) {
        this.nov = nov;
    }


    public double getMebleg() {
        return Math.round(mebleg * 100.0) / 100.0;
    }

    public void setMebleg(double mebleg) {
        this.mebleg = mebleg;
    }

    public String getTarix() {
        return tarix;
    }

    public void setTarix(String tarix) {
        this.tarix = tarix;
    }

    public String getQeyd() {
        return qeyd;
    }

    public void setQeyd(String qeyd) {
        this.qeyd = qeyd;
    }
}
