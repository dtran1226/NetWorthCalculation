package com.example.thuct.networthtracking.model;

public class Asset extends MoneyRecord {
    public enum AssetType {
        CASH, INVESTMENT, LONG_TERM_ASSET, OTHER
    }

    private AssetType type;

    public AssetType getType() {
        return type;
    }

    public void setType(AssetType type) {
        this.type = type;
    }

    public Asset() {
        super();
    }

    public Asset(String name, double amount, AssetType type) {
        super(name, amount);
        this.type = type;
    }

    public Asset(long id, String name, double amount, AssetType type) {
        super(id, name, amount);
        this.type = type;
    }
}
