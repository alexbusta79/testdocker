package com.inditex.suppliers.domain.model;

import com.inditex.suppliers.domain.exception.BusinessException;
import java.util.Objects;
import org.springframework.http.HttpStatus;

public class Candidate {
	private final Integer duns;
	private final String name;
	private final String country;
	private final Long annualTurnover;
	private CandidateStatus status;

	public Candidate(Integer duns, String name, String country, Long annualTurnover, CandidateStatus status) {
		this.duns = Objects.requireNonNull(duns);
		this.name = Objects.requireNonNull(name);
		this.country = Objects.requireNonNull(country).toUpperCase();
		this.annualTurnover = Objects.requireNonNull(annualTurnover);
		this.status = Objects.requireNonNull(status);
	}

	public static Candidate create(Integer duns, String name, String country, Long annualTurnover) {
		return new Candidate(duns, name, country, annualTurnover, CandidateStatus.PENDING);
	}

	public void accept() {
		if (status != CandidateStatus.PENDING)
			throw new BusinessException(HttpStatus.CONFLICT, "Candidate can not be accepted");
		status = CandidateStatus.ACCEPTED;
	}

	public void refuse() {
		if (status != CandidateStatus.PENDING)
			throw new BusinessException(HttpStatus.CONFLICT, "Candidate can not be refused");
		status = CandidateStatus.REFUSED;
	}

	public boolean isActiveCandidacy() {
		return status == CandidateStatus.PENDING;
	}

	public Integer duns() {
		return duns;
	}

	public String name() {
		return name;
	}

	public String country() {
		return country;
	}

	public Long annualTurnover() {
		return annualTurnover;
	}

	public CandidateStatus status() {
		return status;
	}
}
