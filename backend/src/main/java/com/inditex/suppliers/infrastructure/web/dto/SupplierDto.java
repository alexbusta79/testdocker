package com.inditex.suppliers.infrastructure.web.dto;

import com.inditex.suppliers.domain.model.Supplier;

public record SupplierDto(Long annualTurnover, String country, Integer duns, String name, String status,
		String sustainabilityRating) {
	public static SupplierDto fromDomain(Supplier supplier) {
		return new SupplierDto(supplier.annualTurnover(), supplier.country(), supplier.duns(), supplier.name(),
				supplier.status().apiValue(), supplier.sustainabilityRating().name());
	}
}
