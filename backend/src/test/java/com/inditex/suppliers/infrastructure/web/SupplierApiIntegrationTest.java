package com.inditex.suppliers.infrastructure.web;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.inditex.suppliers.application.port.CountryInformationPort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SupplierApiIntegrationTest {
	@Autowired
	MockMvc mvc;
	@MockBean
	CountryInformationPort countryPort;

	@Test
	void full_candidate_accept_get_supplier_flow() throws Exception {
		when(countryPort.isBanned("ES")).thenReturn(false);
		mvc.perform(post("/candidates").contentType("application/json")
				.content("{\"annualTurnover\":5000000,\"country\":\"ES\",\"duns\":123456789,\"name\":\"Supplier A\"}"))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.duns").value(123456789))
				.andExpect(jsonPath("$.name").value("Supplier A"));
		mvc.perform(get("/candidates/123456789")).andExpect(status().isOk())
				.andExpect(jsonPath("$.country").value("ES"));
		mvc.perform(post("/candidates/123456789/accept").contentType("application/json")
				.content("{\"sustainabilityRating\":\"A\"}")).andExpect(status().isNoContent())
				.andExpect(content().string(""));
		mvc.perform(get("/suppliers/123456789")).andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("Active"))
				.andExpect(jsonPath("$.sustainabilityRating").value("A"));
	}

	@Test
	void potential_supplier_response_has_data_and_pagination() throws Exception {
		when(countryPort.isBanned("ES")).thenReturn(false);
		createAndAccept(111111111, 2_000_000, "ES 200");
		createAndAccept(111111112, 2_100_000, "ES 210");
		createAndAccept(111111113, 2_500_000, "ES 250");
		mvc.perform(get("/suppliers/potential?rate=1500000&limit=2&offset=0")).andExpect(status().isOk())
				.andExpect(jsonPath("$.data", hasSize(2))).andExpect(jsonPath("$.pagination.limit").value(2))
				.andExpect(jsonPath("$.pagination.offset").value(0))
				.andExpect(jsonPath("$.pagination.total", greaterThanOrEqualTo(3)));
	}

	@Test
	void validation_and_conflict_errors_match_contract() throws Exception {
		mvc.perform(get("/suppliers/potential?rate=100")).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.info").exists());
		mvc.perform(get("/suppliers/999999999")).andExpect(status().isNotFound());
	}

	private void createAndAccept(int duns, long turnover, String name) throws Exception {
		mvc.perform(post("/candidates").contentType("application/json").content("{\"annualTurnover\":" + turnover
				+ ",\"country\":\"ES\",\"duns\":" + duns + ",\"name\":\"" + name + "\"}"))
				.andExpect(status().isCreated());
		mvc.perform(post("/candidates/" + duns + "/accept").contentType("application/json")
				.content("{\"sustainabilityRating\":\"A\"}")).andExpect(status().isNoContent());
	}
}
