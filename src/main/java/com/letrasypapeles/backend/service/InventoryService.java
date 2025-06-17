package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Inventory;
import com.letrasypapeles.backend.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {
	private InventoryRepository inventoryRepository;

	@Autowired
	public InventoryService(InventoryRepository inventoryRepository) {
		this.inventoryRepository = inventoryRepository;
	}

	public List<Inventory> obtenerTodos() {
		return inventoryRepository.findAll();
	}

	public Optional<Inventory> obtenerPorId(Long id) {
		return inventoryRepository.findById(id);
	}

	public Inventory guardar(Inventory inventario) {
		return inventoryRepository.save(inventario);
	}

	public void eliminar(Long id) {
		inventoryRepository.deleteById(id);
	}

	public List<Inventory> obtenerPorProductoId(Long productoId) {
		return inventoryRepository.findByProductId(productoId);
	}

	public List<Inventory> obtenerPorSucursalId(Long sucursalId) {
		return inventoryRepository.findByBranchId(sucursalId);
	}

	public List<Inventory> obtenerInventarioBajoUmbral(Integer umbral) {
		return inventoryRepository.findByStockLessThan(umbral);
	}
}
