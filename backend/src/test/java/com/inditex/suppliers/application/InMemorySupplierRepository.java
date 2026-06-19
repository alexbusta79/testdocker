package com.inditex.suppliers.application;

import com.inditex.suppliers.application.port.SupplierRepository;
import com.inditex.suppliers.domain.model.Supplier;
import java.util.*;

class InMemorySupplierRepository implements SupplierRepository {
	private final Map<Integer, Supplier> data = new LinkedHashMap<>();

	@Override
	public Supplier save(Supplier supplier) {
		data.put(supplier.duns(), supplier);
		return supplier;
	}

	@Override
	public Optional<Supplier> findByDuns(Integer duns) {
		return Optional.ofNullable(data.get(duns));
	}

	@Override
	public boolean existsByDuns(Integer duns) {
		return data.containsKey(duns);
	}

	@Override
	public List<Supplier> findAll() {
		return new ArrayList<>(data.values());
	}
}
