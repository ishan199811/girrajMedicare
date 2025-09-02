package com.girrajmedico.girrajmedico.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.girrajmedico.girrajmedico.model.dao.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor , Long>{

}
