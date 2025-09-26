package com.girrajmedico.girrajmedico.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.girrajmedico.girrajmedico.model.dao.Medicine;
import com.girrajmedico.girrajmedico.service.MedicineService;

@RestController
@RequestMapping("/api")
public class MedicineController {

@Autowired
MedicineService medicineService;

	@GetMapping("/getAllMedicine")
    public ResponseEntity<?> getAllMedicine() {
    	
    	 return ResponseEntity.ok(medicineService.getAllMedicine());
        }

	@GetMapping("/url")
    public ResponseEntity<?> getAllMedicineUrl() {
    	return ResponseEntity.ok( "This is medicine List");
        }



}
