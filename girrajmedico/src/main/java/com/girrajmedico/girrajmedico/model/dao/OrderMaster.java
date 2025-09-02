package com.girrajmedico.girrajmedico.model.dao;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;


@Entity
public class OrderMaster {

	
	@Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false)
User user;
	
	@OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
   private List<OrderItem> orderItems;
	
	 String paymentMode;
	
	 String orderStatus;
	 
	 String fullAddress;
	 
	 
	 public String getFullAddress() {
		return fullAddress;
	}


	public void setFullAddress(String fullAddress) {
		this.fullAddress = fullAddress;
	}


	double subTotal;
		
	    double discount;
		
		double otherCharge;
		
	    @Column(name = "totalPrice")
	    private @NotNull double totalPrice;
		
	    String orderDate;
	 
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public List<OrderItem> getOrderItems() {
		return orderItems;
	}


	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}


	public double getTotalPrice() {
		return totalPrice;
	}


	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}


	
	 public String getOrderStatus() {
		return orderStatus;
	}


	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}


	
	
	public String getOrderDate() {
		return orderDate;
	}


	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}


	public double getSubTotal() {
		return subTotal;
	}


	public void setSubTotal(double subTotal) {
		this.subTotal = subTotal;
	}


	public double getDiscount() {
		return discount;
	}


	public void setDiscount(double discount) {
		this.discount = discount;
	}


	public double getOtherCharge() {
		return otherCharge;
	}


	public void setOtherCharge(double otherCharge) {
		this.otherCharge = otherCharge;
	}


	public String getPaymentMode() {
	return paymentMode;
}


public void setPaymentMode(String paymentMode) {
	this.paymentMode = paymentMode;
}
}
