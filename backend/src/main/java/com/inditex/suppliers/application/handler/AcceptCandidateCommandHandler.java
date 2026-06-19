package com.inditex.suppliers.application.handler;

import com.inditex.suppliers.application.command.AcceptCandidateCommand;
import com.inditex.suppliers.application.service.SupplierLifecycleService;

import org.springframework.stereotype.Component;

@Component
public class AcceptCandidateCommandHandler {
	private final SupplierLifecycleService service;

	public AcceptCandidateCommandHandler(SupplierLifecycleService service) {
		this.service = service;
	}

	public void handle(AcceptCandidateCommand command) {
		service.acceptCandidate(command);
	}
}
