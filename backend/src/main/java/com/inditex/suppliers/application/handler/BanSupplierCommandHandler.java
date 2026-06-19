package com.inditex.suppliers.application.handler;

import com.inditex.suppliers.application.command.BanSupplierCommand;
import com.inditex.suppliers.application.service.SupplierLifecycleService;

import org.springframework.stereotype.Component;

@Component
public class BanSupplierCommandHandler {
	private final SupplierLifecycleService service;

	public BanSupplierCommandHandler(SupplierLifecycleService service) {
		this.service = service;
	}

	public void handle(BanSupplierCommand command) {
		service.banSupplier(command);
	}
}
