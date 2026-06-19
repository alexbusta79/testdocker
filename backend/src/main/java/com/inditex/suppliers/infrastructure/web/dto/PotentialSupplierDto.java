package com.inditex.suppliers.infrastructure.web.dto;

import com.inditex.suppliers.application.service.PotentialSupplierScore;
import java.math.BigDecimal;

public record PotentialSupplierDto(Long annualTurnover, String country, Integer duns, String name, String status,
		String sustainabilityRating, BigDecimal score) {
	public static PotentialSupplierDto fromDomain(PotentialSupplierScore item) {
		var supplier = item.supplier();
		return new PotentialSupplierDto(supplier.annualTurnover(), supplier.country(), supplier.duns(), supplier.name(),
				supplier.status().apiValue(), supplier.sustainabilityRating().name(), item.score());
	}
}
