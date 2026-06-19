package com.inditex.suppliers.infrastructure.persistence.repository;

import com.inditex.suppliers.infrastructure.persistence.entity.SupplierEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataSupplierRepository extends JpaRepository<SupplierEntity, Integer> {
}
