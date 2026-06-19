package com.inditex.suppliers.infrastructure.persistence.adapter;

import com.inditex.suppliers.application.port.CandidateRepository;
import com.inditex.suppliers.domain.model.*;
import com.inditex.suppliers.infrastructure.persistence.entity.CandidateEntity;
import com.inditex.suppliers.infrastructure.persistence.repository.SpringDataCandidateRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class JpaCandidateRepositoryAdapter implements CandidateRepository {
	private final SpringDataCandidateRepository repository;

	public JpaCandidateRepositoryAdapter(SpringDataCandidateRepository repository) {
		this.repository = repository;
	}

	@Override
	public Candidate save(Candidate candidate) {
		Optional<CandidateEntity> existing = repository
				.findFirstByDunsAndStatus(candidate.duns(), CandidateStatus.PENDING)
				.or(() -> repository.findFirstByDunsAndStatus(candidate.duns(), CandidateStatus.ACCEPTED))
				.or(() -> repository.findFirstByDunsAndStatus(candidate.duns(), CandidateStatus.REFUSED));
		CandidateEntity entity = existing.orElseGet(() -> CandidateEntity.fromDomain(candidate));
		if (existing.isPresent())
			entity.updateFromDomain(candidate);
		return repository.save(entity).toDomain();
	}

	@Override
	public Optional<Candidate> findByDuns(Integer duns) {
		return repository.findFirstByDunsAndStatus(duns, CandidateStatus.PENDING).map(CandidateEntity::toDomain);
	}

	@Override
	public Optional<Candidate> findActiveByDuns(Integer duns) {
		return repository.findFirstByDunsAndStatus(duns, CandidateStatus.PENDING).map(CandidateEntity::toDomain);
	}

	@Override
	public boolean existsActiveByDuns(Integer duns) {
		return repository.existsByDunsAndStatus(duns, CandidateStatus.PENDING);
	}
}
