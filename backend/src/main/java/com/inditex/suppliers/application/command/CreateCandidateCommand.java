package com.inditex.suppliers.application.command;

public record CreateCandidateCommand(Long annualTurnover, String country, Integer duns, String name) {
}
