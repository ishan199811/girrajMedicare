package com.girrajmedico.girrajmedico.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.girrajmedico.girrajmedico.model.dao.CartMaster;
import com.girrajmedico.girrajmedico.model.dao.Medicine;
import com.girrajmedico.girrajmedico.model.dao.User;

public interface CartMasterRepository extends JpaRepository<CartMaster , Long>{

	

	List<CartMaster> findAllByLoginId(User user);

	CartMaster findByLoginIdAndMedicine(User user, Medicine medicine);

	List<CartMaster> findByLoginId(User user);

}
