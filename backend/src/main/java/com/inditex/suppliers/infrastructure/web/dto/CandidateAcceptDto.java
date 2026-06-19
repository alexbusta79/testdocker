package com.inditex.suppliers.infrastructure.web.dto;

import com.inditex.suppliers.domain.model.SustainabilityRating;
import jakarta.validation.constraints.NotNull;

public record CandidateAcceptDto(@NotNull SustainabilityRating sustainabilityRating) {
}
