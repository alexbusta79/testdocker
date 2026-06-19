package com.inditex.suppliers.infrastructure.web;

import java.io.IOException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OpenApiResourceController {
	@GetMapping(value = "/openapi/supplier-api.yaml", produces = "application/yaml")
	public Resource supplierApi() throws IOException {
		return new ClassPathResource("openapi/supplier-api.yaml");
	}

	@GetMapping(value = "/openapi/country-api.yaml", produces = "application/yaml")
	public Resource countryApi() throws IOException {
		return new ClassPathResource("openapi/country-api.yaml");
	}
}
