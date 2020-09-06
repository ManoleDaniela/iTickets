package com.example.ittickets;

public class Bilet {
    private String linie_bilet;
    private String tip_bilet, pret_bilet;

    public Bilet(String linie_bilet, String tip_bilet,String pret_bilet) {
        this.linie_bilet = linie_bilet;
        this.tip_bilet = tip_bilet;
        this.pret_bilet = pret_bilet;
    }

    public Bilet() {
    }

    public String getLinie_bilet() {
        return linie_bilet;
    }

    public void setLinie_bilet(String linie_bilet) {
        this.linie_bilet = linie_bilet;
    }

    public String getTip_bilet() {
        return tip_bilet;
    }

    public void setTip_bilet(String tip_bilet) {
        this.tip_bilet = tip_bilet;
    }

    public String getPret_bilet() {
        return pret_bilet;
    }

    public void setPret_bilet(String pret_bilet) {
        this.pret_bilet = pret_bilet;
    }

    @Override
    public String toString() {
        return this.linie_bilet + " " + this.tip_bilet+" " +this.pret_bilet;
    }
}
