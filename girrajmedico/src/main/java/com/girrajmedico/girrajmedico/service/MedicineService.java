package com.girrajmedico.girrajmedico.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;



import com.girrajmedico.girrajmedico.model.dao.Medicine;
import com.girrajmedico.girrajmedico.repository.MedicineRepository;

@Service
public class MedicineService {

@Autowired
MedicineRepository medicineRepository;

public ResponseEntity<?> getAllMedicine(){
	
	List<Medicine> medicine=medicineRepository.findAll();
	
	return ResponseEntity.ok(medicine);
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
