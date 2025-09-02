package com.girrajmedico.girrajmedico.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.girrajmedico.girrajmedico.model.dao.Medicine;

public interface MedicineRepository extends JpaRepository<Medicine , Long>{

}
