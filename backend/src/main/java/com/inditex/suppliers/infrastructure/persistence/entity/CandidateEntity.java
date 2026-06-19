package com.inditex.suppliers.infrastructure.persistence.entity;

import com.inditex.suppliers.domain.model.*;
import jakarta.persistence.*;

@Entity
@Table(name = "candidates", indexes = @Index(name = "idx_candidates_duns_status", columnList = "duns,status"))
public class CandidateEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private Integer duns;
	@Column(nullable = false)
	private String name;
	@Column(nullable = false, length = 2)
	private String country;
	@Column(nullable = false)
	private Long annualTurnover;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private CandidateStatus status;

	protected CandidateEntity() {
	}

	public CandidateEntity(Integer duns, String name, String country, Long annualTurnover, CandidateStatus status) {
		this.duns = duns;
		this.name = name;
		this.country = country;
		this.annualTurnover = annualTurnover;
		this.status = status;
	}

	public static CandidateEntity fromDomain(Candidate c) {
		return new CandidateEntity(c.duns(), c.name(), c.country(), c.annualTurnover(), c.status());
	}

	public void updateFromDomain(Candidate c) {
		this.name = c.name();
		this.country = c.country();
		this.annualTurnover = c.annualTurnover();
		this.status = c.status();
	}

	public Candidate toDomain() {
		return new Candidate(duns, name, country, annualTurnover, status);
	}

	public Long getId() {
		return id;
	}

	public Integer getDuns() {
		return duns;
	}

	public CandidateStatus getStatus() {
		return status;
	}
}
