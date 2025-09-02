package com.girrajmedico.girrajmedico.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.girrajmedico.girrajmedico.model.dao.Medicine;
import com.girrajmedico.girrajmedico.model.dao.OrderItem;
import com.girrajmedico.girrajmedico.model.dao.OrderMaster;

public interface OrderItemRepository extends JpaRepository<OrderItem , Long>{

	 OrderItem findByOrderAndMedicine(OrderMaster order, Medicine medicine);

}
