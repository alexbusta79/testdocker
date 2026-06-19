package com.inditex.suppliers.application;

import static org.assertj.core.api.Assertions.*;

import com.inditex.suppliers.application.command.*;
import com.inditex.suppliers.application.service.SupplierLifecycleService;
import com.inditex.suppliers.domain.exception.BusinessException;
import com.inditex.suppliers.domain.model.SustainabilityRating;
import java.util.Comparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SupplierLifecycleServiceTest {
	InMemoryCandidateRepository candidates;
	InMemorySupplierRepository suppliers;
	SupplierLifecycleService service;

	@BeforeEach
	void setUp() {
		candidates = new InMemoryCandidateRepository();
		suppliers = new InMemorySupplierRepository();
		service = new SupplierLifecycleService(candidates, suppliers, country -> country.equals("KP"));
	}

	@Test
	void creates_accepts_and_exposes_active_supplier() {
		service.createCandidate(new CreateCandidateCommand(5_000_000L, "ES", 123456789, "Supplier A"));
		service.acceptCandidate(new AcceptCandidateCommand(123456789, SustainabilityRating.A));
		assertThat(service.getSupplier(123456789).status().apiValue()).isEqualTo("Active");
		assertThat(candidates.findActiveByDuns(123456789)).isEmpty();
	}

	@Test
	void refuses_and_allows_reapply() {
		service.createCandidate(new CreateCandidateCommand(900_000L, "ES", 222222222, "Low"));
		service.refuseCandidate(new RefuseCandidateCommand(222222222));
		var reapplied = service.createCandidate(new CreateCandidateCommand(2_000_000L, "ES", 222222222, "Low Again"));
		assertThat(reapplied.name()).isEqualTo("Low Again");
	}

	@Test
	void does_not_accept_banned_country_or_low_turnover() {
		service.createCandidate(new CreateCandidateCommand(5_000_000L, "KP", 333333333, "Banned"));
		assertThatThrownBy(() -> service.acceptCandidate(new AcceptCandidateCommand(333333333, SustainabilityRating.A)))
				.isInstanceOf(BusinessException.class);
		service.createCandidate(new CreateCandidateCommand(999_999L, "ES", 333333334, "Low"));
		assertThatThrownBy(() -> service.acceptCandidate(new AcceptCandidateCommand(333333334, SustainabilityRating.A)))
				.isInstanceOf(BusinessException.class);
	}

	@Test
	void probation_supplier_can_be_banned_and_can_not_reapply() {
		service.createCandidate(new CreateCandidateCommand(2_000_000L, "ES", 444444444, "Probation"));
		service.acceptCandidate(new AcceptCandidateCommand(444444444, SustainabilityRating.D));
		service.banSupplier(new BanSupplierCommand(444444444));
		assertThat(service.getSupplier(444444444).status().apiValue()).isEqualTo("Disqualified");
		assertThatThrownBy(
				() -> service.createCandidate(new CreateCandidateCommand(2_000_000L, "ES", 444444444, "Again")))
				.isInstanceOf(BusinessException.class);
	}

	@Test
	void potential_suppliers_excludes_disqualified_and_applies_bonus_and_sort() {
		service.createCandidate(new CreateCandidateCommand(2_000_000L, "ES", 111111111, "ES 200"));
		service.acceptCandidate(new AcceptCandidateCommand(111111111, SustainabilityRating.A));
		service.createCandidate(new CreateCandidateCommand(2_100_000L, "ES", 111111112, "ES 210"));
		service.acceptCandidate(new AcceptCandidateCommand(111111112, SustainabilityRating.A));
		service.createCandidate(new CreateCandidateCommand(2_500_000L, "ES", 111111113, "ES 250"));
		service.acceptCandidate(new AcceptCandidateCommand(111111113, SustainabilityRating.A));
		service.createCandidate(new CreateCandidateCommand(5_000_000L, "ES", 111111114, "Bad"));
		service.acceptCandidate(new AcceptCandidateCommand(111111114, SustainabilityRating.D));
		service.banSupplier(new BanSupplierCommand(111111114));
		var result = service.potentialSuppliers(1_500_000L, 10, 0).data();
		assertThat(result).extracting(i -> i.supplier().duns()).containsExactly(111111112, 111111111, 111111113);
		assertThat(result.getFirst().score()).isEqualByComparingTo("262500.00");
		assertThat(result).extracting(i -> i.score()).isSortedAccordingTo(Comparator.reverseOrder());
	}
}
