package com.example.defter.Model;

import java.io.Serializable;
import java.text.DecimalFormat;

public class print implements Serializable {
    private String mal;
    private double miqdar;
    private double satishQiymet;
    private double mebleg;

    public print(String mal, double miqdar, double satishQiymet, double mebleg) {
        this.mal = mal;
        this.miqdar = miqdar;
        this.satishQiymet = satishQiymet;
        this.mebleg = mebleg;
    }

    public String getMal() {
        return mal;
    }

    public void setMal(String mal) {
        this.mal = mal;
    }

    public double getMiqdar() {
        return Math.round(miqdar * 100.0) / 100.0;
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
}
