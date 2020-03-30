package com.example.thuct.networthtracking.model;

public class CalculationResponse {
    private double totalAssets;
    private double totalLiabilities;
    private double netWorth;

    public CalculationResponse() {
    }

    public CalculationResponse(double totalAssets, double totalLiabilities, double netWorth) {
        this.totalAssets = totalAssets;
        this.totalLiabilities = totalLiabilities;
        this.netWorth = netWorth;
    }

    public double getTotalAssets() {
        return totalAssets;
    }

    public void setTotalAssets(double totalAssets) {
        this.totalAssets = totalAssets;
    }

    public double getTotalLiabilities() {
        return totalLiabilities;
    }

    public void setTotalLiabilities(double totalLiabilities) {
        this.totalLiabilities = totalLiabilities;
    }

    public double getNetWorth() {
        return netWorth;
    }

    public void setNetWorth(double netWorth) {
        this.netWorth = netWorth;
    }
}
