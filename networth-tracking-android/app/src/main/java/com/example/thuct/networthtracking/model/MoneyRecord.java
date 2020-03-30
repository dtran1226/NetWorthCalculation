package com.example.thuct.networthtracking.model;

public class MoneyRecord {
    protected String name;
    protected double amount;

    public MoneyRecord() {
    }

    public MoneyRecord(String name, double amount) {
        this.name = name;
        this.amount = amount;
    }

    public MoneyRecord(long id, String name, double amount) {
        this.name = name;
        this.amount = amount;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
