package com.inditex.suppliers.application.service;

import com.inditex.suppliers.domain.model.Supplier;
import java.math.BigDecimal;

public record PotentialSupplierScore(Supplier supplier, BigDecimal score) {
}
