package com.inditex.suppliers.infrastructure.persistence.repository;

import com.inditex.suppliers.domain.model.CandidateStatus;
import com.inditex.suppliers.infrastructure.persistence.entity.CandidateEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataCandidateRepository extends JpaRepository<CandidateEntity, Long> {
	Optional<CandidateEntity> findFirstByDunsAndStatus(Integer duns, CandidateStatus status);

	boolean existsByDunsAndStatus(Integer duns, CandidateStatus status);
}
