package com.inditex.suppliers.application.command;

import com.inditex.suppliers.domain.model.SustainabilityRating;

public record AcceptCandidateCommand(Integer duns, SustainabilityRating sustainabilityRating) {
}
