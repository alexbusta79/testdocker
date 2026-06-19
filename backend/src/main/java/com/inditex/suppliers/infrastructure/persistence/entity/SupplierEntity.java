package com.inditex.suppliers.infrastructure.persistence.entity;

import com.inditex.suppliers.domain.model.*;
import jakarta.persistence.*;

@Entity
@Table(name = "suppliers")
public class SupplierEntity {
	@Id
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
	private SustainabilityRating sustainabilityRating;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private SupplierStatus status;

	protected SupplierEntity() {
	}

	public SupplierEntity(Integer duns, String name, String country, Long annualTurnover,
			SustainabilityRating sustainabilityRating, SupplierStatus status) {
		this.duns = duns;
		this.name = name;
		this.country = country;
		this.annualTurnover = annualTurnover;
		this.sustainabilityRating = sustainabilityRating;
		this.status = status;
	}

	public static SupplierEntity fromDomain(Supplier s) {
		return new SupplierEntity(s.duns(), s.name(), s.country(), s.annualTurnover(), s.sustainabilityRating(),
				s.status());
	}

	public void updateFromDomain(Supplier s) {
		this.name = s.name();
		this.country = s.country();
		this.annualTurnover = s.annualTurnover();
		this.sustainabilityRating = s.sustainabilityRating();
		this.status = s.status();
	}

	public Supplier toDomain() {
		return new Supplier(duns, name, country, annualTurnover, sustainabilityRating, status);
	}

	public Integer getDuns() {
		return duns;
	}
}
