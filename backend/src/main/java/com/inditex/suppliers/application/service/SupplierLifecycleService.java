package com.inditex.suppliers.application.service;

import com.inditex.suppliers.application.command.*;
import com.inditex.suppliers.application.port.*;
import com.inditex.suppliers.domain.exception.BusinessException;
import com.inditex.suppliers.domain.model.*;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SupplierLifecycleService {
	private static final long MIN_ACCEPTANCE_TURNOVER = 1_000_000L;
	private final CandidateRepository candidateRepository;
	private final SupplierRepository supplierRepository;
	private final CountryInformationPort countryPort;

	public SupplierLifecycleService(CandidateRepository candidateRepository, SupplierRepository supplierRepository,
			CountryInformationPort countryPort) {
		this.candidateRepository = candidateRepository;
		this.supplierRepository = supplierRepository;
		this.countryPort = countryPort;
	}

	@Transactional
	public Candidate createCandidate(CreateCandidateCommand command) {
		if (candidateRepository.existsActiveByDuns(command.duns()))
			throw new BusinessException(HttpStatus.CONFLICT, "Candidate already exists");
		var existingSupplier = supplierRepository.findByDuns(command.duns());
		if (existingSupplier.isPresent()) {
			if (existingSupplier.get().isDisqualified())
				throw new BusinessException(HttpStatus.CONFLICT, "Supplier banned");
			throw new BusinessException(HttpStatus.CONFLICT, "Supplier already exists");
		}
		return candidateRepository
				.save(Candidate.create(command.duns(), command.name(), command.country(), command.annualTurnover()));
	}

	@Transactional(readOnly = true)
	public Candidate getCandidate(Integer duns) {
		return candidateRepository.findActiveByDuns(duns)
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Candidate not found"));
	}

	@Transactional
	public void acceptCandidate(AcceptCandidateCommand command) {
		Candidate candidate = candidateRepository.findActiveByDuns(command.duns())
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Candidate not found"));
		if (supplierRepository.existsByDuns(command.duns()))
			throw new BusinessException(HttpStatus.CONFLICT, "Candidate can not be accepted");
		if (countryPort.isBanned(candidate.country()))
			throw new BusinessException(HttpStatus.CONFLICT, "Candidate can not be accepted. Country is banned");
		if (candidate.annualTurnover() < MIN_ACCEPTANCE_TURNOVER)
			throw new BusinessException(HttpStatus.CONFLICT, "Candidate can not be accepted");
		Supplier supplier = Supplier.fromAcceptedCandidate(candidate, command.sustainabilityRating());
		supplierRepository.save(supplier);
		candidate.accept();
		candidateRepository.save(candidate);
	}

	@Transactional
	public void refuseCandidate(RefuseCandidateCommand command) {
		Candidate candidate = candidateRepository.findActiveByDuns(command.duns())
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Candidate not found"));
		candidate.refuse();
		candidateRepository.save(candidate);
	}

	@Transactional(readOnly = true)
	public Supplier getSupplier(Integer duns) {
		return supplierRepository.findByDuns(duns)
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Supplier not found"));
	}

	@Transactional
	public void banSupplier(BanSupplierCommand command) {
		Supplier supplier = getSupplier(command.duns());
		supplier.ban();
		supplierRepository.save(supplier);
	}

	@Transactional(readOnly = true)
	public PagedPotentialSuppliers potentialSuppliers(long rate, int limit, int offset) {
		List<Supplier> allNonDisqualified = supplierRepository.findAll().stream().filter(s -> !s.isDisqualified())
				.toList();
		List<Supplier> eligible = allNonDisqualified.stream().filter(s -> s.isEligibleForRate(rate)).toList();
		Map<String, Set<Long>> lowestByCountry = allNonDisqualified.stream()
				.collect(Collectors.groupingBy(Supplier::country,
						Collectors.mapping(Supplier::annualTurnover, Collectors.toCollection(TreeSet::new))));
		Map<String, Set<Long>> twoLowestByCountry = new HashMap<>();
		lowestByCountry.forEach((country, turnovers) -> twoLowestByCountry.put(country,
				turnovers.stream().limit(2).collect(Collectors.toSet())));
		List<PotentialSupplierScore> sorted = eligible.stream()
				.map(s -> new PotentialSupplierScore(s,
						s.score(twoLowestByCountry.getOrDefault(s.country(), Set.of()).contains(s.annualTurnover()))))
				.sorted(Comparator.comparing(PotentialSupplierScore::score).reversed()
						.thenComparing(p -> p.supplier().duns()))
				.toList();
		int safeOffset = Math.min(offset, sorted.size());
		int safeTo = Math.min(safeOffset + limit, sorted.size());
		return new PagedPotentialSuppliers(sorted.subList(safeOffset, safeTo), limit, offset, sorted.size());
	}
}
