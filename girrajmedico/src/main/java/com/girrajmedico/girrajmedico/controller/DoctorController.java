package com.girrajmedico.girrajmedico.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.girrajmedico.girrajmedico.model.request.DoctorDetailRequest;
import com.girrajmedico.girrajmedico.service.CartService;
import com.girrajmedico.girrajmedico.service.DoctorService;

@RestController
@RequestMapping("/doctor")
public class DoctorController {

	@Autowired
	DoctorService doctorService;
	
	@Autowired
	CartService cartService;
	
	@PostMapping("/saveDoctorDetail")
	ResponseEntity<?> saveDocDetail(@RequestBody DoctorDetailRequest doctor) {
		return ResponseEntity.ok(doctorService.saveDoctorDetail(doctor));
	}
	

}
