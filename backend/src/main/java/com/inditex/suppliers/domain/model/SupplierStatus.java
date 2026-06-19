package com.inditex.suppliers.domain.model;

public enum SupplierStatus {
	ACTIVE, ON_PROBATION, DISQUALIFIED;

	public String apiValue() {
		return this == DISQUALIFIED ? "Disqualified" : "Active";
	}
}
