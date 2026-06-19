package com.inditex.suppliers.infrastructure.web.dto;

import com.inditex.suppliers.domain.model.Candidate;
import jakarta.validation.constraints.*;

public record CandidateDto(@NotNull @Min(0) Long annualTurnover, @NotBlank @Size(min = 2, max = 2) String country,
		@NotNull @Min(100000000) @Max(999999999) Integer duns, @NotBlank String name) {
	public static CandidateDto fromDomain(Candidate candidate) {
		return new CandidateDto(candidate.annualTurnover(), candidate.country(), candidate.duns(), candidate.name());
	}
}
