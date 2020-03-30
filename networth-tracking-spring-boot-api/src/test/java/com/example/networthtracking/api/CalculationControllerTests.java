package com.example.networthtracking.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.example.networthtracking.api.model.CalculationRequest;
import com.example.networthtracking.api.model.CalculationResponse;

public class CalculationControllerTests {
	private static RestTemplate restTemplate;
	private static CalculationRequest calculationRequest;
	private static CalculationResponse calculationResponse;
	private static HttpHeaders requestHeaders;
	final String NETWORTH_CALCULATION_API_URL = "http://localhost:8080/calculate";
	final String BAD_REQUEST = "Bad Request";

	@BeforeAll
	private static void init() {
		restTemplate = new RestTemplate();
		calculationRequest = new CalculationRequest();
		calculationResponse = new CalculationResponse();
		requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
		requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	}

	@Test()
	public void testGetCalculationResponseWithNullCalculationRequest() {
		// Set calculation request to null
		calculationRequest = null;
		// Set up RestTemplate to call getCalculationResponse API
		HttpEntity<CalculationRequest> requestEntity = new HttpEntity<>(calculationRequest, requestHeaders);
		// Test the response/exception
		Exception exception = assertThrows(HttpClientErrorException.class,
				() -> restTemplate.exchange(NETWORTH_CALCULATION_API_URL, HttpMethod.POST, requestEntity, CalculationResponse.class));
		assertTrue(exception.getMessage().contains(BAD_REQUEST));
		// Re-initialize a calculationRequest to use for other tests
		calculationRequest = new CalculationRequest();
	}

	@Test
	public void testGetCalculationResponseWithNullLiabilitiesAndAssetsLists() {
		// Add different values to calculation request for different test cases
		calculationRequest.setAssets(null);
		calculationRequest.setLiabilities(null);
		// Set up RestTemplate to call getCalculationResponse API
		HttpEntity<CalculationRequest> requestEntity = new HttpEntity<>(calculationRequest, requestHeaders);
		ResponseEntity<CalculationResponse> responseEntity = restTemplate.exchange(NETWORTH_CALCULATION_API_URL, HttpMethod.POST, requestEntity,
				CalculationResponse.class);
		// Test the response
		assertNotNull(responseEntity);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		calculationResponse = responseEntity.getBody();
		assertNotNull(calculationResponse);
		assertEquals(0, calculationResponse.getTotalAssets());
		assertEquals(0, calculationResponse.getTotalLiabilities());
		assertEquals(0, calculationResponse.getNetWorth());
	}

	@Test
	public void testGetCalculationResponseWithEmptyLiabilitiesAndAssetsLists() {
		// Add different values to calculation request for different test cases
		calculationRequest.setAssets(new ArrayList<Double>());
		calculationRequest.setLiabilities(new ArrayList<Double>());
		// Set up RestTemplate to call getCalculationResponse API
		HttpEntity<CalculationRequest> requestEntity = new HttpEntity<>(calculationRequest, requestHeaders);
		ResponseEntity<CalculationResponse> responseEntity = restTemplate.exchange(NETWORTH_CALCULATION_API_URL, HttpMethod.POST, requestEntity,
				CalculationResponse.class);
		// Test the response
		assertNotNull(responseEntity);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		calculationResponse = responseEntity.getBody();
		assertNotNull(calculationResponse);
		assertEquals(0, calculationResponse.getTotalAssets());
		assertEquals(0, calculationResponse.getTotalLiabilities());
		assertEquals(0, calculationResponse.getNetWorth());
	}

	@Test
	public void testGetCalculationResponseWithEmptyLiabilitiesList() {
		// Add different values to calculation request for different test cases
		List<Double> assets = Arrays.asList(1500D, 1200D, 350D);
		calculationRequest.setAssets(assets);
		calculationRequest.setLiabilities(new ArrayList<Double>());
		// Set up RestTemplate to call getCalculationResponse API
		HttpEntity<CalculationRequest> requestEntity = new HttpEntity<>(calculationRequest, requestHeaders);
		ResponseEntity<CalculationResponse> responseEntity = restTemplate.exchange(NETWORTH_CALCULATION_API_URL, HttpMethod.POST, requestEntity,
				CalculationResponse.class);
		// Test the response
		assertNotNull(responseEntity);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		calculationResponse = responseEntity.getBody();
		assertNotNull(calculationResponse);
		assertEquals(3050, calculationResponse.getTotalAssets());
		assertEquals(0, calculationResponse.getTotalLiabilities());
		assertEquals(3050, calculationResponse.getNetWorth());
	}

	@Test
	public void testGetCalculationResponseWithEmptyAssetsList() {
		// Add different values to calculation request for different test cases
		calculationRequest.setAssets(new ArrayList<Double>());
		List<Double> liabilities = Arrays.asList(1500D, 1200D, 350D);
		calculationRequest.setLiabilities(liabilities);
		// Set up RestTemplate to call getCalculationResponse API
		HttpEntity<CalculationRequest> requestEntity = new HttpEntity<>(calculationRequest, requestHeaders);
		ResponseEntity<CalculationResponse> responseEntity = restTemplate.exchange(NETWORTH_CALCULATION_API_URL, HttpMethod.POST, requestEntity,
				CalculationResponse.class);
		// Test the response
		assertNotNull(responseEntity);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		calculationResponse = responseEntity.getBody();
		assertNotNull(calculationResponse);
		assertEquals(0, calculationResponse.getTotalAssets());
		assertEquals(3050, calculationResponse.getTotalLiabilities());
		assertEquals(-3050, calculationResponse.getNetWorth());
	}

	@Test
	public void testGetCalculationResponseWithSuitalbeLiabilitiesAndAssetsLists() {
		// Add different values to calculation request for different test cases
		List<Double> assets = Arrays.asList(1500D, 1200D, 350D);
		List<Double> liabilities = Arrays.asList(140D, 2200D, 30D);
		// Add different values to calculation request for different test cases
		calculationRequest.setAssets(assets);
		calculationRequest.setLiabilities(liabilities);
		// Set up RestTemplate to call getCalculationResponse API
		HttpEntity<CalculationRequest> requestEntity = new HttpEntity<>(calculationRequest, requestHeaders);
		ResponseEntity<CalculationResponse> responseEntity = restTemplate.exchange(NETWORTH_CALCULATION_API_URL, HttpMethod.POST, requestEntity,
				CalculationResponse.class);
		// Test the response
		assertNotNull(responseEntity);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		calculationResponse = responseEntity.getBody();
		assertEquals(3050, calculationResponse.getTotalAssets());
		assertEquals(2370, calculationResponse.getTotalLiabilities());
		assertEquals(680, calculationResponse.getNetWorth());
	}
	
	@AfterAll
	private static void destroy() {
		restTemplate = null;
		calculationRequest = null;
		calculationResponse = null;
		requestHeaders = null;
	}
}