package com.girrajmedico.girrajmedico.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.girrajmedico.girrajmedico.service.CartService;

@RestController
@RequestMapping("/user")
public class CartController {

	@Autowired
	CartService cartService;
	
	@PostMapping("/saveProductInCart")
	public ResponseEntity<?> addProducttoCart(@RequestParam Long id,@RequestParam int quantity ){
		return ResponseEntity.ok(cartService.saveProductInCart(id , quantity));
	}
	
	@GetMapping("/getAllCartProduct")
	public ResponseEntity<?> addProducttoCart(){
		return ResponseEntity.ok(cartService.getAllProductOfUser());
	}
	
	// ✅ Delete Product from cart
	@DeleteMapping("deleteCartProduct/{id}")
	public ResponseEntity<?> deleteCartProduct(@PathVariable Long id) {
	    boolean deleted = cartService.deleteCartProductById(id);
	    if (deleted) {
	        return ResponseEntity.ok("Product deleted successfully");
	    } else {
	        return ResponseEntity.status(404).body("Error in deleting product");
	    }
	}
	
	
	
}
