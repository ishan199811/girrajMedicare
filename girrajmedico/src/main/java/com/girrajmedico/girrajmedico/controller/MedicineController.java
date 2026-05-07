package com.girrajmedico.girrajmedico.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.girrajmedico.girrajmedico.service.MedicineService;

@RestController
@RequestMapping("/api")
public class MedicineController {

	@Autowired
	MedicineService medicineService;

	@GetMapping("/getAllMedicine")
	public ResponseEntity<?> getAllMedicine(
			@RequestParam(defaultValue = "0") int page, 
			@RequestParam(defaultValue = "50") int size) {
		
		// Pass the page and size down to the paginated service
		return medicineService.getAllMedicine(page, size);
	}

	@GetMapping("/filterMedicine")
	public ResponseEntity<?> filterMedicines(
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String composition,
			@RequestParam(required = false) Double minPrice,
			@RequestParam(required = false) Double maxPrice,
			@RequestParam(defaultValue = "0") int page, 
			@RequestParam(defaultValue = "50") int size) {
		
		return medicineService.filterMedicines(name, composition, minPrice, maxPrice, page, size);
	}
	
	@GetMapping("/url")
	public ResponseEntity<?> getAllMedicineUrl() {
		return ResponseEntity.ok("This is medicine List");
	}

}