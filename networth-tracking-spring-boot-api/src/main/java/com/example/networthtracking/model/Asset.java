package com.example.networthtracking.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Asset extends MoneyRecord {
	public static enum AssetType {
		CASH, INVESTMENT, LONG_TERM_ASSET, OTHER
	}

	private AssetType type;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	public AssetType getType() {
		return type;
	}

	public void setType(AssetType type) {
		this.type = type;
	}

	public Asset() {
		super();
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Asset(String name, double amount, AssetType type) {
		super(name, amount);
		this.type = type;
	}

	public Asset(long id, String name, double amount, AssetType type) {
		super(id, name, amount);
		this.type = type;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}

		if (!(obj instanceof Asset)) {
			return false;
		}

		Asset asset = (Asset) obj;

		return id == asset.getId() && name == asset.getName() && amount == asset.getAmount() && type == asset.getType();
	}
}
