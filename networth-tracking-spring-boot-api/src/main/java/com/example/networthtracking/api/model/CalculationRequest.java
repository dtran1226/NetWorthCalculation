package com.example.networthtracking.api.model;

import java.util.List;

public class CalculationRequest {
	private List<Double> assets;
	private List<Double> liabilities;

	public CalculationRequest() {
	}

	public CalculationRequest(List<Double> assets, List<Double> liabilities) {
		this.assets = assets;
		this.liabilities = liabilities;
	}

	public List<Double> getAssets() {
		return assets;
	}

	public void setAssets(List<Double> assets) {
		this.assets = assets;
	}
	
	public List<Double> getLiabilities() {
		return liabilities;
	}

	public void setLiabilities(List<Double> liabilities) {
		this.liabilities = liabilities;
	}
}