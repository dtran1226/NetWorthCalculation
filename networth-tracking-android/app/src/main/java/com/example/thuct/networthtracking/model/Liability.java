package com.example.thuct.networthtracking.model;

public class Liability extends MoneyRecord {

    public enum LiabilityType {
        SHORT_TERM_LIABILITY, LONG_TERM_DEBT
    }

    private LiabilityType type;

    public LiabilityType getType() {
        return type;
    }

    public void setType(LiabilityType type) {
        this.type = type;
    }

    public Liability() {
        super();
    }

    public Liability(String name, double amount, LiabilityType type) {
        super(name, amount);
        this.type = type;
    }

    public Liability(long id, String name, double amount, LiabilityType type) {
        super(id, name, amount);
        this.type = type;
    }
}