package com.girrajmedico.girrajmedico.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.girrajmedico.girrajmedico.model.dao.User;

@RestController
public class TestController {
	
	@GetMapping("/test")
    public ResponseEntity<?> getTest() {
    	
    	 return ResponseEntity.ok("Testing is done");
        }

}
