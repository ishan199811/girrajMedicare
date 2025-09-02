package com.girrajmedico.girrajmedico.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.girrajmedico.girrajmedico.model.dao.Location;

public interface LocationRepository extends JpaRepository<Location , Long> {

}
