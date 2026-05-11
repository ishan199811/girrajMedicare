package com.girrajmedico.girrajmedico.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.girrajmedico.girrajmedico.model.dao.Medicine;
import com.girrajmedico.girrajmedico.repository.MedicineRepository;

@Service
public class MedicineService {

	@Autowired
	MedicineRepository medicineRepository;

	/**
	 * UPDATED: Fetches medicines in paginated chunks instead of all at once.
	 * * @param page The current page number (starts at 0)
	 * @param size The number of records per page (e.g., 50)
	 * @return A Page object containing the chunk of medicines and pagination metadata
	 */
	public ResponseEntity<?> getAllMedicine(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Medicine> medicinePage = medicineRepository.findAll(pageable);
		
		return ResponseEntity.ok(medicinePage);
	}

	public ResponseEntity<?> saveMedicine(Medicine medicine) {
		double percentage=medicine.getDiscountPercentage();
		double realAmount=medicine.getPrice();
		double percentageAmount = calculatePercentage(realAmount, percentage);
		medicine.setDescountPrice(realAmount-percentageAmount);
		medicineRepository.save(medicine);
		return ResponseEntity.ok("Medicine saved successfully");
	}

	public ResponseEntity<?> editMedicine(Long id,Medicine medicineDetails){
		Optional<Medicine> optionalMedicine = medicineRepository.findById(id);
		if (!optionalMedicine.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Medicine not found");
		}

		Medicine medicine = optionalMedicine.get();

		// Update fields
		double percentage=medicineDetails.getDiscountPercentage();
		double realAmount=medicineDetails.getPrice();
		double percentageAmount = calculatePercentage(realAmount, percentage);
		medicine.setDiscountPercentage(medicineDetails.getDiscountPercentage()); 
		double amt=realAmount - percentageAmount;
		medicine.setDescountPrice(amt);
		medicine.setMedicineName(medicineDetails.getMedicineName());
		medicine.setDescription(medicineDetails.getDescription());
		medicine.setPrice(medicineDetails.getPrice());
		medicine.setMedineType(medicineDetails.getMedineType());  
		medicine.setExpiryDate(medicineDetails.getExpiryDate());
		medicine.setTotalPiece(medicineDetails.getTotalPiece());
		medicineRepository.save(medicine);
		return ResponseEntity.ok("Medicine updated successfully");
	}

	public ResponseEntity<?> deletMedicine(Long id){
		if (!medicineRepository.existsById(id)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Medicine not found");
		}

		medicineRepository.deleteById(id);
		return ResponseEntity.ok("Medicine deleted successfully");
	}

	public void updateMedicineQuantity(Long id, long orderPiece) {
		Optional<Medicine> optionalMedicine = medicineRepository.findById(id);
		if (optionalMedicine.isPresent()) {
			Medicine medicine = optionalMedicine.get();
			double available=  medicine.getTotalPiece();

			medicine.setTotalPiece(available - orderPiece);
			medicineRepository.save(medicine);

		}
		// Or throw an exception if medicine not found
	}
	public ResponseEntity<?> filterMedicines(
	        String name,
	        String composition,
	        Double minPrice,
	        Double maxPrice,
	        int page,
	        int size
	) {

	    Pageable pageable = PageRequest.of(page, size);

	    Page<Medicine> medicinePage =
	            medicineRepository.filterMedicines(
	                    name,
	                    composition,
	                    minPrice,
	                    maxPrice,
	                    pageable
	            );

	    // Filter medicines starting with searched alphabet
	    List<Medicine> filteredMedicines = medicinePage.getContent()
	            .stream()
	            .filter(medicine -> {
	                if (name == null || name.trim().isEmpty()) {
	                    return true;
	                }

	                return medicine.getMedicineName() != null &&
	                        medicine.getMedicineName()
	                                .toLowerCase()
	                                .startsWith(name.toLowerCase());
	            })
	            .toList();

	    if (filteredMedicines.isEmpty()) {
	        return ResponseEntity
	                .status(HttpStatus.NOT_FOUND)
	                .body("No medicines found matching the criteria.");
	    }

	    return ResponseEntity.ok(filteredMedicines);
	}
	/**
	 * Calculates the percentage of a given real amount.
	 *
	 * @param realAmount The original amount.
	 * @param percentage The percentage to calculate (e.g., 25.0 for 25%).
	 * @return The calculated percentage amount.
	 */
	public static double calculatePercentage(double realAmount, double percentage) {
		return (percentage / 100.0) * realAmount;
	}
}
