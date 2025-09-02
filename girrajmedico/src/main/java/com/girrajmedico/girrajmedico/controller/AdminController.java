package com.girrajmedico.girrajmedico.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.girrajmedico.girrajmedico.model.dao.Doctor;
import com.girrajmedico.girrajmedico.model.dao.Medicine;
import com.girrajmedico.girrajmedico.model.dao.User;
import com.girrajmedico.girrajmedico.service.DoctorService;
import com.girrajmedico.girrajmedico.service.MedicineService;
import com.girrajmedico.girrajmedico.service.OrderService;
import com.girrajmedico.girrajmedico.service.UserService;

@RestController
@RequestMapping("/admin")
public class AdminController {
	
	 @Autowired
	    private UserService userService;
	 
	 @Autowired
	 MedicineService medicineService;
	
	 @Autowired
		DoctorService doctorService;

		@Autowired
		OrderService orderService;
	 
	 @GetMapping("/getAllUser")
	 public ResponseEntity<List<User>> getAllUser() {
	 	 return ResponseEntity.ok(userService.getAllUser());
	     }
	 
	 @PostMapping("/saveMedicine")
	    public ResponseEntity<?> saveMedicine(@RequestBody Medicine medicine) {
	    	 return ResponseEntity.ok(medicineService.saveMedicine(medicine));
	        }
	 @PutMapping("/updateMedicine/{id}")
	 public ResponseEntity<?> updateMedicine(@PathVariable Long id, @RequestBody Medicine medicineDetails) {
	    
	     return ResponseEntity.ok(medicineService.editMedicine(id,medicineDetails));
	 }
	 
	 @DeleteMapping("deleteMedicine/{id}")
	 public ResponseEntity<?> deleteMedicine(@PathVariable Long id) {
	   
	    return ResponseEntity.ok(medicineService.deletMedicine(id));
	 }
	// ✅ Save doctor detail
		@PostMapping("/saveDoctor")
		public ResponseEntity<?> saveDoctor(@RequestBody Doctor doctor){
			
			return ResponseEntity.ok(doctorService.saveDoctor(doctor));
		}
		
		
		// ✅ Get all doctor detail
	@GetMapping("/getAllDoctor")
	public ResponseEntity<?> getAllDoctor(){
		
		return ResponseEntity.ok(doctorService.getAllDoctor());
	}
	// ✅ Update Doctor
	@PutMapping("/updateDoctor/{id}")
	public ResponseEntity<?> updateDoctor(@PathVariable Long id, @RequestBody Doctor updatedDoctor) {
	   return ResponseEntity.ok(doctorService.editDoctor(id, updatedDoctor));
	}

	// ✅ Delete Doctor
	@DeleteMapping("/deleteDoctor/{id}")
	public ResponseEntity<?> deleteDoctor(@PathVariable Long id) {
	    boolean deleted = doctorService.deleteDoctorById(id);
	    if (deleted) {
	        return ResponseEntity.ok("Doctor deleted successfully");
	    } else {
	        return ResponseEntity.status(404).body("Doctor not found");
	    }
	}
	@GetMapping("/getAllOrder")
	public ResponseEntity<?> getAllUserOrder(){
		
		return ResponseEntity.ok(orderService.getAllOrder());
	}
	
}
