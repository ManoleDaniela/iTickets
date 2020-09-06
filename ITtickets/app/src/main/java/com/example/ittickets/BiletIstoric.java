package com.example.ittickets;

public class BiletIstoric {

    private String d,data;
    private String linie, pret,tip;

    public BiletIstoric( String d,String data, String linie, String tip, String pret) {
        this.data=data;
        this.d = d;
        this.linie = linie;
        this.tip = tip;
        this.pret = pret;
    }

    public BiletIstoric() {
    }
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getLinie() {
        return linie;
    }

    public void setLinie(String linie) {
        this.linie = linie;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getPret() {
        return pret;
    }

    public void setPret(String pret) {
        this.pret = pret;
    }

    @Override
    public String toString() {
        return  this.data + " " + this.linie + " " + this.tip+" " +this.pret;
    }
}