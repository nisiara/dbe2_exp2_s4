package com.letrasypapeles.backend.repository;

import com.letrasypapeles.backend.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
	List<Inventory> findByProductId(Long productId);
	List<Inventory> findByBranchId(Long branchId);
	List<Inventory> findByStockLessThan(int threshold);
}
