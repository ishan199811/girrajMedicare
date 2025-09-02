package com.girrajmedico.girrajmedico.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.girrajmedico.girrajmedico.model.dao.CartMaster;
import com.girrajmedico.girrajmedico.model.dao.Medicine;
import com.girrajmedico.girrajmedico.model.dao.User;
import com.girrajmedico.girrajmedico.repository.CartMasterRepository;
import com.girrajmedico.girrajmedico.repository.MedicineRepository;
import com.girrajmedico.girrajmedico.repository.UserRepository;

@Service
public class CartService {

	
	@Autowired
	CartMasterRepository cartRepository;

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	MedicineRepository medicineRepository;
	
	
	public ResponseEntity<?> saveProductInCart(Long id, int quantity) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	    if (authentication != null && authentication.isAuthenticated()) {
	        User user = userRepository.getUserByUsername(authentication.getName());
	        Optional<Medicine> medicineOpt = medicineRepository.findById(id);

	        if (medicineOpt.isEmpty()) {
	            return ResponseEntity.badRequest().body("Invalid medicine ID");
	        }

	        Medicine medicine = medicineOpt.get();
	        CartMaster existingCart = cartRepository.findByLoginIdAndMedicine(user, medicine);

	        if (existingCart != null) {
	            // If already in cart, just update the quantity
	            existingCart.setQuantity(existingCart.getQuantity() + quantity);
	            cartRepository.save(existingCart);
	            return ResponseEntity.ok("Medicine quantity updated in your cart");
	        } else {
	            // Create new cart item
	            CartMaster cart = new CartMaster();
	            cart.setLoginId(user);
	            cart.setMedicine(medicine);
	            cart.setQuantity(quantity);
	           
	            
	            cart.setTotal(medicine.getPrice() * quantity);
	            cart.setDiscountedPrice(medicine.getDescountPrice()*quantity);
	            cartRepository.save(cart);
	            return ResponseEntity.ok("Medicine added to your cart");
	        }
	    }

	    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to add items to the cart");
	}

	public ResponseEntity<?> getAllProductOfUser() {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	    if (authentication != null && authentication.isAuthenticated() &&
	        !authentication.getPrincipal().equals("anonymousUser")) {

	        User user = userRepository.getUserByUsername(authentication.getName());

	        List<CartMaster> cartList = cartRepository.findAllByLoginId(user);

	        return ResponseEntity.ok(cartList);
	    } else {
	        return ResponseEntity
	                .status(HttpStatus.UNAUTHORIZED)
	                .body("You need to be logged in to view your cart.");
	    }
	}

	public boolean deleteCartProductById(Long id) {
		cartRepository.deleteById(id);
		return false;
	}

	public ResponseEntity<?> getProductDetail(Long id) {
		// TODO Auto-generated method stub
		Optional<Medicine> medicine=medicineRepository.findById(id);
		
		return ResponseEntity.ok(medicine.get());
	}




}
