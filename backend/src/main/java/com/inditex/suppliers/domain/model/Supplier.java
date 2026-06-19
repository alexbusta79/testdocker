package com.inditex.suppliers.domain.model;

import com.inditex.suppliers.domain.exception.BusinessException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import org.springframework.http.HttpStatus;

public class Supplier {
	private final Integer duns;
	private final String name;
	private final String country;
	private final Long annualTurnover;
	private final SustainabilityRating sustainabilityRating;
	private SupplierStatus status;

	public Supplier(Integer duns, String name, String country, Long annualTurnover, SustainabilityRating rating,
			SupplierStatus status) {
		this.duns = Objects.requireNonNull(duns);
		this.name = Objects.requireNonNull(name);
		this.country = Objects.requireNonNull(country).toUpperCase();
		this.annualTurnover = Objects.requireNonNull(annualTurnover);
		this.sustainabilityRating = Objects.requireNonNull(rating);
		this.status = Objects.requireNonNull(status);
	}

	public static Supplier fromAcceptedCandidate(Candidate c, SustainabilityRating rating) {
		SupplierStatus status = rating.isActiveRating() ? SupplierStatus.ACTIVE : SupplierStatus.ON_PROBATION;
		return new Supplier(c.duns(), c.name(), c.country(), c.annualTurnover(), rating, status);
	}

	public void ban() {
		if (status != SupplierStatus.ON_PROBATION)
			throw new BusinessException(HttpStatus.CONFLICT, "Supplier can not be banned");
		status = SupplierStatus.DISQUALIFIED;
	}

	public boolean isDisqualified() {
		return status == SupplierStatus.DISQUALIFIED;
	}

	public boolean isEligibleForRate(long rate) {
		return !isDisqualified() && annualTurnover > rate;
	}

	public BigDecimal score(boolean bonus) {
		BigDecimal score = BigDecimal.valueOf(annualTurnover).multiply(new BigDecimal("0.1"))
				.multiply(sustainabilityRating.constant());
		if (bonus)
			score = score.multiply(new BigDecimal("1.25"));
		return score.setScale(2, RoundingMode.HALF_UP);
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

	public SustainabilityRating sustainabilityRating() {
		return sustainabilityRating;
	}

	public SupplierStatus status() {
		return status;
	}
}
