package com.example.defter.Model;

import java.io.Serializable;
import java.text.DecimalFormat;

public class Debitor implements Serializable {
    private int id;
    private String ad;
    private Double borc;
    private String nomre;

    public Debitor(int id, String ad, String nomre, Double borc) {
        this.id = id;
        this.ad = ad;
        this.borc = borc;
        this.nomre = nomre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public Double getBorc() {
        return Math.round(borc * 100.0) / 100.0;
    }

    public String getNomre() {
        return nomre;
    }

    public void setNomre(String nomre) {
        this.nomre = nomre;
    }

    public void setBorc(Double borc) {
        this.borc = borc;
    }
}
