package com.example.networthtracking.service;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CalculationService {

	/*
	 * Calculate the sum of a list of elements of type 'double'
	 */
	public double getTotal(List<Double> list) {
		double total = 0;
		if (list != null && list.size() > 0) {
			total = list.stream().mapToDouble(Double::doubleValue).sum();
		}
		return total;
	}
}