package com.example.networthtracking.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Liability extends MoneyRecord {

	public static enum LiabilityType {
		SHORT_TERM_LIABILITY, LONG_TERM_DEBT
	}

	private LiabilityType type;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	public LiabilityType getType() {
		return type;
	}

	public void setType(LiabilityType type) {
		this.type = type;
	}

	public void setUser(User user) {
		this.user = user;
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

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}

		if (!(obj instanceof Liability)) {
			return false;
		}

		Liability liability = (Liability) obj;

		return id == liability.getId() && name == liability.getName() && amount == liability.getAmount()
				&& type == liability.getType();
	}
}
