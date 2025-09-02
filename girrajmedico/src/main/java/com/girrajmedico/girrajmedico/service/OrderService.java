package com.girrajmedico.girrajmedico.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.girrajmedico.girrajmedico.model.dao.CartMaster;
import com.girrajmedico.girrajmedico.model.dao.Medicine;
import com.girrajmedico.girrajmedico.model.dao.OrderItem;
import com.girrajmedico.girrajmedico.model.dao.OrderMaster;
import com.girrajmedico.girrajmedico.model.dao.User;
import com.girrajmedico.girrajmedico.model.request.OrderRequest;
import com.girrajmedico.girrajmedico.repository.CartMasterRepository;
import com.girrajmedico.girrajmedico.repository.OrderItemRepository;
import com.girrajmedico.girrajmedico.repository.OrderMasterRepository;
import com.girrajmedico.girrajmedico.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderService {
	
	@Autowired
    private UserRepository userRepository;

@Autowired
OrderMasterRepository orderMasterRepository;

@Autowired
OrderItemRepository orderItemRepository;

@Autowired
private CartMasterRepository cartMasterRepository;

@Autowired
MedicineService  medicineService; 

public ResponseEntity<?> getOrderOfUser(){
	
	 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
     if (authentication != null && authentication.isAuthenticated()) {
        User user=userRepository.getUserByUsername(authentication.getName());
     	List<OrderMaster> orders=orderMasterRepository.findByUser(user);
     	List<OrderMaster> sortedOrders = orders.stream()
     		    .sorted(Comparator.comparing(OrderMaster::getOrderDate).reversed())
     		    .collect(Collectors.toList());
     	return ResponseEntity.ok(sortedOrders);	
    
     }
     else {
      	return ResponseEntity.ok("No order done yet");	
            	 
     }
	
	
}


@Transactional // Add transactional annotation to ensure atomicity
public ResponseEntity<?> palceOrder(OrderRequest orderRequest) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated()) {
        OrderMaster orderMaster = new OrderMaster();
        User user = userRepository.getUserByUsername(authentication.getName());
        orderMaster.setUser(user);
        orderMaster.setDiscount(orderRequest.getDiscount());
        orderMaster.setOtherCharge(orderRequest.getOtherCharge());
        orderMaster.setPaymentMode(orderRequest.getPaymentMode());
        orderMaster.setTotalPrice(orderRequest.getTotalPrice());
        orderMaster.setSubTotal(orderRequest.getSubTotal());
        orderMaster.setOrderDate(currentDateTime());
        orderMaster.setOrderStatus("Placed");
        orderMaster.setFullAddress(orderRequest.getFullAddress());
        
        orderMasterRepository.save(orderMaster);

        List<CartMaster> cart = cartMasterRepository.findByLoginId(user);
        for (CartMaster cartMaster : cart) {
            Medicine medicine = cartMaster.getMedicine();
            // Check if an order item with the same medicine already exists for this order
            OrderItem existingOrderItem = orderItemRepository.findByOrderAndMedicine(orderMaster, medicine);

            if (existingOrderItem != null) {
                // If it exists, update the quantity
                existingOrderItem.setQuantity(existingOrderItem.getQuantity() + cartMaster.getQuantity());
                existingOrderItem.setPrice(cartMaster.getDiscountedPrice()* cartMaster.getQuantity() ); // You might want to recalculate price based on the new quantity
                orderItemRepository.save(existingOrderItem);
            } else {
                // If it doesn't exist, create a new order item
                OrderItem newItem = new OrderItem();
                newItem.setMedicine(medicine);
                newItem.setPrice(cartMaster.getDiscountedPrice());
                newItem.setQuantity(cartMaster.getQuantity());
                newItem.setOrder(orderMaster);
                medicineService.updateMedicineQuantity(medicine.getId(), cartMaster.getQuantity());
                orderItemRepository.save(newItem);
            }
            cartMasterRepository.delete(cartMaster);
        }
        return ResponseEntity.ok("Order Placed Successfully");
    }

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
}
	public void saveOrderItem(OrderItem orderItem) {
		orderItemRepository.save(orderItem);
	}

	public static String currentDateTime() {
		// Get current date and time
        LocalDateTime now = LocalDateTime.now();

        // Print full date and time
        System.out.println("Current Date and Time: " + now);

        // Extract year
        int year = now.getYear();
        System.out.println("Current Year: " + year);

        // Format custom date-time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formatted = now.format(formatter);
        return formatted;
	}


	public ResponseEntity<?> getAllOrder() {
		// TODO Auto-generated method stub
		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	     if (authentication != null && authentication.isAuthenticated()) {
	        User user=userRepository.getUserByUsername(authentication.getName());
	     
	    		 List<OrderMaster> orders=orderMasterRepository.findAll();
	    		 
	    		 List<OrderMaster> sortedOrders = orders.stream()
	    				    .sorted(Comparator.comparing(OrderMaster::getOrderDate, Comparator.reverseOrder()))
	    				    .collect(Collectors.toList());

	 	     	return ResponseEntity.ok(sortedOrders);	
	                	 
	 	     }
	    return ResponseEntity.ok("Err fetching Orders");
	    
	}

	
	
	
}
