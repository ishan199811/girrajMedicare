package com.girrajmedico.girrajmedico.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.girrajmedico.girrajmedico.model.request.OrderRequest;
import com.girrajmedico.girrajmedico.service.OrderService;

@RestController
@RequestMapping("/user")
public class OrderController {

	
	@Autowired
	OrderService orderService;
	
	
	@GetMapping("/getUserOrder")
	public ResponseEntity<?> getAllUserOrder(){
		
		return ResponseEntity.ok(orderService.getOrderOfUser());
	}
	
	@PostMapping("/placeOrder")
	public ResponseEntity<?> placeUserOrder(@RequestBody OrderRequest items){
		
		return ResponseEntity.ok(orderService.palceOrder(items));
	}
	
	
}
