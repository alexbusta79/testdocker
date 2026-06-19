package com.inditex.suppliers.infrastructure.web.dto;

import com.inditex.suppliers.application.service.PagedPotentialSuppliers;
import java.util.List;

public record PotentialSuppliersDto(List<PotentialSupplierDto> data, PaginationDto pagination) {
	public static PotentialSuppliersDto fromDomain(PagedPotentialSuppliers page) {
		return new PotentialSuppliersDto(page.data().stream().map(PotentialSupplierDto::fromDomain).toList(),
				new PaginationDto(page.limit(), page.offset(), page.total()));
	}
}
