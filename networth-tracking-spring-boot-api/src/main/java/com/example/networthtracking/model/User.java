package com.example.networthtracking.model;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name;
	@OneToMany(mappedBy = "user", orphanRemoval = true, cascade = { CascadeType.ALL })
	private List<Asset> assets;
	@OneToMany(mappedBy = "user", orphanRemoval = true, cascade = { CascadeType.ALL })
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