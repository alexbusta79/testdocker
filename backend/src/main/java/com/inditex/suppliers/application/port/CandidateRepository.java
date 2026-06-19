package com.inditex.suppliers.application.port;

import com.inditex.suppliers.domain.model.Candidate;
import java.util.Optional;

public interface CandidateRepository {
	Candidate save(Candidate candidate);

	Optional<Candidate> findByDuns(Integer duns);

	Optional<Candidate> findActiveByDuns(Integer duns);

	boolean existsActiveByDuns(Integer duns);
}
