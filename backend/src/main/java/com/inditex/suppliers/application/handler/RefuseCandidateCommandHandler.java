package com.inditex.suppliers.application.handler;

import com.inditex.suppliers.application.command.RefuseCandidateCommand;
import com.inditex.suppliers.application.service.SupplierLifecycleService;

import org.springframework.stereotype.Component;

@Component
public class RefuseCandidateCommandHandler {
	private final SupplierLifecycleService service;

	public RefuseCandidateCommandHandler(SupplierLifecycleService service) {
		this.service = service;
	}

	public void handle(RefuseCandidateCommand command) {
		service.refuseCandidate(command);
	}
}
