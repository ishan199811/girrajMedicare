package com.girrajmedico.girrajmedico.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.girrajmedico.girrajmedico.model.dao.Doctor;
import com.girrajmedico.girrajmedico.model.dao.DoctorSpecialization;
import com.girrajmedico.girrajmedico.model.dao.Role;
import com.girrajmedico.girrajmedico.model.dao.User;
import com.girrajmedico.girrajmedico.model.request.DoctorDetailRequest;
import com.girrajmedico.girrajmedico.repository.DoctorRepository;
import com.girrajmedico.girrajmedico.repository.DoctorSpecializationRepository;
import com.girrajmedico.girrajmedico.repository.UserRepository;

@Service
public class DoctorService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	DoctorRepository doctorRepository;
	
	@Autowired
	DoctorSpecializationRepository dsr;
	
	public ResponseEntity<?> saveDoctor(Doctor doctor){
		doctorRepository.save(doctor);
		return ResponseEntity.ok("Doctor Detail nsaved ");
	}
	
public ResponseEntity<?> getAllDoctor(){
	
	List<User> dl=	userRepository.findAll().stream()
			.filter(e->e.getRoles().stream().anyMatch(role->role.getName().equals("DOCTOR"))).collect(Collectors.toList());
	
	
	
		return ResponseEntity.ok(dl);
	}

public boolean deleteDoctorById(Long id) {
	// TODO Auto-generated method stub
	doctorRepository.deleteById(id);
	return false;
}


public ResponseEntity<?> editDoctor(Long id, Doctor updatedDoctor) {
    Optional<Doctor> existingDoctorOpt = doctorRepository.findById(id);
    
    if (existingDoctorOpt.isEmpty()) {
        return ResponseEntity.notFound().build();
    }

    Doctor existingDoctor = existingDoctorOpt.get();
    existingDoctor.setDoctorName(updatedDoctor.getDoctorName());
  //  existingDoctor.setSpecialization(updatedDoctor.setSpecialization());
    existingDoctor.setClinicAddress(updatedDoctor.getClinicAddress());
    existingDoctor.setClinicNumber(updatedDoctor.getClinicNumber());

    doctorRepository.save(existingDoctor);
    return ResponseEntity.ok(existingDoctor);
}
public ResponseEntity<?> saveDoctorDetail(DoctorDetailRequest doctor) {
	// TODO Auto-generated method stub
	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
Doctor dr=new Doctor();
    if (authentication != null && authentication.isAuthenticated()) {
    	 User user = userRepository.getUserByUsername(authentication.getName());
          for(Role role : user.getRoles()) {
        	  if(role.getName().equals("ADMIN")) {
        		  return ResponseEntity.ok("Sorry Admin is not able to Add doctor field ");  
        	  }
    if(role.getName().equals("DOCTOR")) {
    	System.out.println(user.getEmail());
    	dr.setClinicAddress(doctor.getClinicAddress());
    	dr.setClinicNumber(doctor.getClinicNumber());
    	dr.setDoctorName(doctor.getDoctorName());
    	dr.setEmail(user.getEmail());
    	dr.setMobileNumber(user.getMobileNumber());
       dr.setSpecialization(doctor.getSpecification());
    	doctorRepository.save(dr);
        
      
        return ResponseEntity.ok("Doctor detail saved ");
    }
          }
    return ResponseEntity.ok("unauthorize user role ");
}
	return ResponseEntity.ok("unauthorize user");

}
}
