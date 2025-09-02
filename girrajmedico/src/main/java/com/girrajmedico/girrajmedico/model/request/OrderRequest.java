package com.girrajmedico.girrajmedico.model.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;

public class OrderRequest {

	
	 String paymentMode;
		
	 double subTotal;
	
    double discount;
    
double otherCharge;
	
	String fullAddress;
	
    @Column(name = "totalPrice")
    private @NotNull double totalPrice;
	
	public String getFullAddress() {
		return fullAddress;
	}

	public void setFullAddress(String fullAddress) {
		this.fullAddress = fullAddress;
	}

	

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
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

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	
}
