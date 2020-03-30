package com.example.thuct.networthtracking.model;

import java.util.List;

public class User {
    private long id;
    private String name;
    private List<Asset> assets;
    private List<Liability> liabilities;

    public User() {
    }

    public User(String name, List<Asset> assets, List<Liability> liabilities) {
        this.name = name;
        this.assets = assets;
        this.liabilities = liabilities;
    }

    public User(long id, String name, List<Asset> assets, List<Liability> liabilities) {
        this.id = id;
        this.name = name;
        this.assets = assets;
        this.liabilities = liabilities;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Asset> getAssets() {
        return assets;
    }

    public void setAssets(List<Asset> assets) {
        this.assets = assets;
    }

    public List<Liability> getLiabilities() {
        return liabilities;
    }

    public void setLiabilities(List<Liability> liabilities) {
        this.liabilities = liabilities;
    }
}