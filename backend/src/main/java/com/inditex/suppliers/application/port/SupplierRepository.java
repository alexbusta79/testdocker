package com.inditex.suppliers.application.port;

import com.inditex.suppliers.domain.model.Supplier;
import java.util.List;
import java.util.Optional;

public interface SupplierRepository {
	Supplier save(Supplier supplier);

	Optional<Supplier> findByDuns(Integer duns);

	boolean existsByDuns(Integer duns);

	List<Supplier> findAll();
}
