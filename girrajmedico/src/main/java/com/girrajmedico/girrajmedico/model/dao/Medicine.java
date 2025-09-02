package com.girrajmedico.girrajmedico.model.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class Medicine {
	 @Id
	    @Column(name = "medicine_id")
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	
	String medicineName;
	
	double price;
	
	String medineType;
	
	String description;
	
	double discountPercentage;
	
	double descountPrice;
	public double getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(double discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	public double getDescountPrice() {
		return descountPrice;
	}

	public void setDescountPrice(double descountPrice) {
		this.descountPrice = descountPrice;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMedicineName() {
		return medicineName;
	}

	public void setMedicineName(String medicineName) {
		this.medicineName = medicineName;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getMedineType() {
		return medineType;
	}

	public void setMedineType(String medineType) {
		this.medineType = medineType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public double getTotalPiece() {
		return totalPiece;
	}

	public void setTotalPiece(double totalPiece) {
		this.totalPiece = totalPiece;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	double totalPiece;
	
	String expiryDate;
	
	
	
	
	

}
