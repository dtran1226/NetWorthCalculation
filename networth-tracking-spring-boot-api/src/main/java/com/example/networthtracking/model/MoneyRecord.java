package com.example.networthtracking.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class MoneyRecord {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	protected long id;
	protected String name;
	protected double amount;

	public MoneyRecord() {
	}

	public MoneyRecord(String name, double amount) {
		this.name = name;
		this.amount = amount;
	}
	
	public MoneyRecord(long id, String name, double amount) {
		this.id = id;
		this.name = name;
		this.amount = amount;
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

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
}