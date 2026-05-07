package com.girrajmedico.girrajmedico.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CartService {

    @Autowired
    CartMasterRepository cartRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MedicineRepository medicineRepository;
    
    
 // 2. Add this line manually. This creates the 'log' variable.
    private static final Logger log = LoggerFactory.getLogger(CartService.class);

    public ResponseEntity<?> saveProductInCart(Long medicineId, int quantity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName())) {
            
            String username = authentication.getName();
            User user = userRepository.getUserByUsername(username);

            if (user == null) {
                log.warn("Unauthorized cart access attempt: User '{}' not found in database", username);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User account not found.");
            }

            Optional<Medicine> medicineOpt = medicineRepository.findById(medicineId);
            if (medicineOpt.isEmpty()) {
                log.error("Cart Update Failed: Medicine ID {} does not exist. User: {}", medicineId, username);
                return ResponseEntity.badRequest().body("Invalid medicine ID");
            }

            Medicine medicine = medicineOpt.get();
            CartMaster existingCart = cartRepository.findByLoginIdAndMedicine(user, medicine);

            if (existingCart != null) {
                log.info("Updating existing cart item for user: {}. Medicine: {}, Adding quantity: {}", 
                         username, medicine.getMedicineName(), quantity);
                         
                existingCart.setQuantity(existingCart.getQuantity() + quantity);
                existingCart.setTotal(medicine.getPrice() * existingCart.getQuantity());
                existingCart.setDiscountedPrice(medicine.getDescountPrice() * existingCart.getQuantity());
                
                cartRepository.save(existingCart);
                return ResponseEntity.ok("Medicine quantity updated in your cart");
            } else {
                log.info("Creating new cart entry for user: {}. Medicine: {}, Quantity: {}", 
                         username, medicine.getMedicineName(), quantity);
                         
                CartMaster cart = new CartMaster();
                cart.setLoginId(user); 
                cart.setMedicine(medicine);
                cart.setQuantity(quantity);
                cart.setTotal(medicine.getPrice() * quantity);
                cart.setDiscountedPrice(medicine.getDescountPrice() * quantity);
                
                cartRepository.save(cart);
                return ResponseEntity.ok("Medicine added to your cart");
            }
        }

        log.warn("Unauthenticated request to save product to cart for Medicine ID: {}", medicineId);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in");
    }

    public ResponseEntity<?> getAllProductOfUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.debug("Fetched {} cart items for user: {}",authentication.getName() );
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName())) {

            String username = authentication.getName();
            User user = userRepository.getUserByUsername(username);
           
            if (user == null) {
                log.error("User context found but user '{}' missing from database", username);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found.");
            }

            List<CartMaster> cartList = cartRepository.findAllByLoginId(user);
            log.debug("Fetched {} cart items for user: {}", cartList.size(), username);

            return ResponseEntity.ok(cartList);
        } else {
            log.info("Guest user attempted to view cart products");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please log in to view cart.");
        }
    }

    public boolean deleteCartProductById(Long id) {
        log.info("Deleting cart item with ID: {}", id);
        try {
            cartRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            log.error("Failed to delete cart item ID {}: {}", id, e.getMessage());
            return false;
        }
    }

    public ResponseEntity<?> getProductDetail(Long id) {
        log.debug("Fetching medicine details for ID: {}", id);
        Optional<Medicine> medicine = medicineRepository.findById(id);
        
        if (medicine.isPresent()) {
            return ResponseEntity.ok(medicine.get());
        } else {
            log.warn("Product details requested for non-existent ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }
}