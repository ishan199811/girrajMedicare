package com.girrajmedico.girrajmedico.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.girrajmedico.girrajmedico.model.dao.OrderMaster;
import com.girrajmedico.girrajmedico.model.dao.User;

public interface OrderMasterRepository extends JpaRepository<OrderMaster , Long> {

	List<OrderMaster> findByUser(User user);

	
}
