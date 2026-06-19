package com.inditex.suppliers.application;

import com.inditex.suppliers.application.port.CandidateRepository;
import com.inditex.suppliers.domain.model.*;
import java.util.*;

class InMemoryCandidateRepository implements CandidateRepository {
	private final List<Candidate> data = new ArrayList<>();

	@Override
	public Candidate save(Candidate candidate) {
		data.removeIf(c -> c.duns().equals(candidate.duns()) && c.status() != CandidateStatus.REFUSED);
		data.add(candidate);
		return candidate;
	}

	@Override
	public Optional<Candidate> findByDuns(Integer duns) {
		return data.stream().filter(c -> c.duns().equals(duns)).findFirst();
	}

	@Override
	public Optional<Candidate> findActiveByDuns(Integer duns) {
		return data.stream().filter(c -> c.duns().equals(duns) && c.status() == CandidateStatus.PENDING).findFirst();
	}

	@Override
	public boolean existsActiveByDuns(Integer duns) {
		return findActiveByDuns(duns).isPresent();
	}
}
