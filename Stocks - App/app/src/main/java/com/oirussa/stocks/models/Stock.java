package com.oirussa.stocks.models;

public class Stock {

    private String name;
    private String price;
    private String change;
    private String pChange;

    public Stock(String name, String price, String change, String pChange) {
        this.name = name;
        this.price = price;
        this.change = change;
        this.pChange = pChange;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getpChange() {
        return pChange;
    }

    public void setpChange(String pChange) {
        this.pChange = pChange;
    }
}
