package com.inditex.suppliers.application.service;

import java.util.List;

public record PagedPotentialSuppliers(List<PotentialSupplierScore> data, int limit, int offset, int total) {
}
