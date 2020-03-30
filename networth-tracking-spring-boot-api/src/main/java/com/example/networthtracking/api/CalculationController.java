package com.example.networthtracking.api;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.networthtracking.api.model.CalculationRequest;
import com.example.networthtracking.api.model.CalculationResponse;
import com.example.networthtracking.service.CalculationService;

@RestController
@RequestMapping("calculate")
public class CalculationController {
	// Singleton service to handle calculation logics
	private final CalculationService calculationService;

	@Autowired
	public CalculationController(CalculationService calculationService) {
		this.calculationService = calculationService;
	}

	/*
	 * Return a response containing total assets, total liabilities and net worth
	 */
	@PostMapping
	public CalculationResponse getCalculationResponse(@Valid @NotNull @RequestBody CalculationRequest calculationRequest) {
		// Calculate total assets
		List<Double> assets = calculationRequest.getAssets();
		double totalAssets = calculationService.getTotal(assets);
		// Calculate total liabilities
		List<Double> liabilities = calculationRequest.getLiabilities();
		double totalLiabilities = calculationService.getTotal(liabilities);
		return new CalculationResponse(totalAssets, totalLiabilities, totalAssets - totalLiabilities);
	}
}