package com.inditex.suppliers.domain.model;

import java.math.BigDecimal;

public enum SustainabilityRating {
	A("1.00"), B("0.75"), C("0.50"), D("0.25"), E("0.10");

	private final BigDecimal constant;

	SustainabilityRating(String constant) {
		this.constant = new BigDecimal(constant);
	}

	public BigDecimal constant() {
		return constant;
	}

	public boolean isActiveRating() {
		return this == A || this == B;
	}
}
