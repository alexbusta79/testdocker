package com.inditex.suppliers.infrastructure.web;

import com.inditex.suppliers.application.command.BanSupplierCommand;
import com.inditex.suppliers.application.handler.BanSupplierCommandHandler;
import com.inditex.suppliers.application.service.SupplierLifecycleService;
import com.inditex.suppliers.infrastructure.web.dto.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/suppliers")
public class SupplierController {
	private final BanSupplierCommandHandler banHandler;
	private final SupplierLifecycleService service;

	public SupplierController(BanSupplierCommandHandler banHandler, SupplierLifecycleService service) {
		this.banHandler = banHandler;
		this.service = service;
	}

	@GetMapping("/{duns}")
	public SupplierDto getSupplier(@PathVariable @Min(100000000) @Max(999999999) Integer duns) {
		return SupplierDto.fromDomain(service.getSupplier(duns));
	}

	@PostMapping("/{duns}/ban")
	public ResponseEntity<Void> banSupplier(@PathVariable @Min(100000000) @Max(999999999) Integer duns) {
		banHandler.handle(new BanSupplierCommand(duns));
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/potential")
	public PotentialSuppliersDto potentialSuppliers(@RequestParam @Min(250) Long rate,
			@RequestParam(defaultValue = "10") @Min(1) @Max(10) Integer limit,
			@RequestParam(defaultValue = "0") @Min(0) Integer offset) {
		return PotentialSuppliersDto.fromDomain(service.potentialSuppliers(rate, limit, offset));
	}
}
