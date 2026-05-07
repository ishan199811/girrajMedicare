package com.girrajmedico.girrajmedico.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.girrajmedico.girrajmedico.model.dao.Medicine;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {

    @Query("SELECT m FROM Medicine m WHERE " +
           "(:name IS NULL OR LOWER(m.medicineName) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:composition IS NULL OR LOWER(m.shortComposition1) LIKE LOWER(CONCAT('%', :composition, '%')) OR LOWER(m.shortComposition2) LIKE LOWER(CONCAT('%', :composition, '%'))) AND " +
           "(:minPrice IS NULL OR m.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR m.price <= :maxPrice)")
    Page<Medicine> filterMedicines(
            @Param("name") String name, 
            @Param("composition") String composition, 
            @Param("minPrice") Double minPrice, 
            @Param("maxPrice") Double maxPrice, 
            Pageable pageable
    );
}