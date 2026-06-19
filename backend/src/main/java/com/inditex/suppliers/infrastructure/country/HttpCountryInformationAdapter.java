package com.inditex.suppliers.infrastructure.country;

import com.inditex.suppliers.application.port.CountryInformationPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class HttpCountryInformationAdapter implements CountryInformationPort {
	private final RestClient restClient;
	private final boolean failOpen;

	public HttpCountryInformationAdapter(@Value("${external.country-service.base-url}") String baseUrl,
			@Value("${external.country-service.fail-open:false}") boolean failOpen) {
		this.restClient = RestClient.builder().baseUrl(baseUrl).build();
		this.failOpen = failOpen;
	}

	@Override
	public boolean isBanned(String country) {
		try {
			CountryResponse response = restClient.get().uri("/countries/{country}", country).retrieve()
					.body(CountryResponse.class);
			return response != null && response.isBanned();
		} catch (RestClientException ex) {
			if (failOpen)
				return false;
			throw new CountryServiceUnavailableException();
		}
	}

	public record CountryResponse(String name, boolean isBanned) {
	}
}
