package com.example.defter;

public class RoundUp {
    public static  String roundUp(double value){
        return String.valueOf(Math.round(value * 100.0) / 100.0);
    }
}
