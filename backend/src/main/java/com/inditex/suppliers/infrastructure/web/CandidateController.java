package com.inditex.suppliers.infrastructure.web;

import com.inditex.suppliers.application.command.*;
import com.inditex.suppliers.application.handler.*;
import com.inditex.suppliers.application.service.SupplierLifecycleService;
import com.inditex.suppliers.infrastructure.web.dto.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/candidates")
public class CandidateController {
	private final CreateCandidateCommandHandler createHandler;
	private final AcceptCandidateCommandHandler acceptHandler;
	private final RefuseCandidateCommandHandler refuseHandler;
	private final SupplierLifecycleService service;

	public CandidateController(CreateCandidateCommandHandler createHandler, AcceptCandidateCommandHandler acceptHandler,
			RefuseCandidateCommandHandler refuseHandler, SupplierLifecycleService service) {
		this.createHandler = createHandler;
		this.acceptHandler = acceptHandler;
		this.refuseHandler = refuseHandler;
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<CandidateDto> addCandidate(@Valid @RequestBody CandidateDto request) {
		var created = createHandler.handle(new CreateCandidateCommand(request.annualTurnover(), request.country(),
				request.duns(), request.name()));
		return ResponseEntity.status(HttpStatus.CREATED).body(CandidateDto.fromDomain(created));
	}

	@GetMapping("/{duns}")
	public CandidateDto getCandidate(@PathVariable @Min(100000000) @Max(999999999) Integer duns) {
		return CandidateDto.fromDomain(service.getCandidate(duns));
	}

	@PostMapping("/{duns}/accept")
	public ResponseEntity<Void> acceptCandidate(@PathVariable @Min(100000000) @Max(999999999) Integer duns,
			@Valid @RequestBody CandidateAcceptDto request) {
		acceptHandler.handle(new AcceptCandidateCommand(duns, request.sustainabilityRating()));
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/{duns}/refuse")
	public ResponseEntity<Void> refuseCandidate(@PathVariable @Min(100000000) @Max(999999999) Integer duns) {
		refuseHandler.handle(new RefuseCandidateCommand(duns));
		return ResponseEntity.noContent().build();
	}
}
