package com.example.defter.Model;

import java.io.Serializable;
import java.text.DecimalFormat;

public class Total implements Serializable {
    private  Double kassa;

    public Total(Double kassa) {
        this.kassa = kassa;
    }

    public Double getKassa() {
        return Math.round(kassa * 100.0) / 100.0;
    }

    public void setKassa(Double kassa) {
        this.kassa = kassa;
    }
}
