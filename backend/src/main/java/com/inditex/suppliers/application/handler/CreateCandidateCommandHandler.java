package com.inditex.suppliers.application.handler;

import com.inditex.suppliers.application.command.CreateCandidateCommand;
import com.inditex.suppliers.application.service.SupplierLifecycleService;
import com.inditex.suppliers.domain.model.Candidate;

import org.springframework.stereotype.Component;

@Component
public class CreateCandidateCommandHandler {
	private final SupplierLifecycleService service;

	public CreateCandidateCommandHandler(SupplierLifecycleService service) {
		this.service = service;
	}

	public Candidate handle(CreateCandidateCommand command) {
		return service.createCandidate(command);
	}
}
