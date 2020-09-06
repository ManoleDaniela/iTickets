package com.example.ittickets;

public class BiletulMeu {
    private String id;
    private String linie, pret,tip;

    public BiletulMeu(String id, String linie, String tip,String pret) {
        this.id = id;
        this.linie = linie;
        this.tip = tip;
        this.pret = pret;
    }

    public BiletulMeu() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        return this.id + " " + this.linie + " " + this.tip+" " +this.pret;
    }
}