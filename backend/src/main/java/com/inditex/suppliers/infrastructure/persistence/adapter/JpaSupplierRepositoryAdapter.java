package com.inditex.suppliers.infrastructure.persistence.adapter;

import com.inditex.suppliers.application.port.SupplierRepository;
import com.inditex.suppliers.domain.model.Supplier;
import com.inditex.suppliers.infrastructure.persistence.entity.SupplierEntity;
import com.inditex.suppliers.infrastructure.persistence.repository.SpringDataSupplierRepository;
import java.util.*;
import org.springframework.stereotype.Repository;

@Repository
public class JpaSupplierRepositoryAdapter implements SupplierRepository {
	private final SpringDataSupplierRepository repository;

	public JpaSupplierRepositoryAdapter(SpringDataSupplierRepository repository) {
		this.repository = repository;
	}

	@Override
	public Supplier save(Supplier supplier) {
		SupplierEntity entity = repository.findById(supplier.duns())
				.orElseGet(() -> SupplierEntity.fromDomain(supplier));
		if (repository.existsById(supplier.duns()))
			entity.updateFromDomain(supplier);
		return repository.save(entity).toDomain();
	}

	@Override
	public Optional<Supplier> findByDuns(Integer duns) {
		return repository.findById(duns).map(SupplierEntity::toDomain);
	}

	@Override
	public boolean existsByDuns(Integer duns) {
		return repository.existsById(duns);
	}

	@Override
	public List<Supplier> findAll() {
		return repository.findAll().stream().map(SupplierEntity::toDomain).toList();
	}
}
