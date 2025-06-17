package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Inventory;
import com.letrasypapeles.backend.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class InventoryController {

	private InventoryService inventoryService;

	@Autowired
	public InventoryController(InventoryService inventoryService) {
		this.inventoryService = inventoryService;
	}

	@GetMapping
	public ResponseEntity<List<Inventory>> obtenerTodos() {
		List<Inventory> inventarios = inventoryService.obtenerTodos();
		return ResponseEntity.ok(inventarios);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Inventory> obtenerPorId(@PathVariable Long id) {
		return inventoryService.obtenerPorId(id)
			.map(ResponseEntity::ok)
			.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/producto/{productoId}")
	public ResponseEntity<List<Inventory>> obtenerPorProductoId(@PathVariable Long productoId) {
		List<Inventory> inventarios = inventoryService.obtenerPorProductoId(productoId);
		return ResponseEntity.ok(inventarios);
	}

	@GetMapping("/sucursal/{sucursalId}")
	public ResponseEntity<List<Inventory>> obtenerPorSucursalId(@PathVariable Long sucursalId) {
		List<Inventory> inventarios = inventoryService.obtenerPorSucursalId(sucursalId);
		return ResponseEntity.ok(inventarios);
	}

	@PostMapping("/create")
	public ResponseEntity<Inventory> crearInventario(@RequestBody Inventory inventario) {
		Inventory newInventory = inventoryService.guardar(inventario);
		return ResponseEntity.ok(newInventory);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<Inventory> actualizarInventario(@PathVariable Long id, @RequestBody Inventory inventario) {
		return inventoryService.obtenerPorId(id)
			.map(inventory -> {
				inventario.setId(id);
				Inventory updatedInventory = inventoryService.guardar(inventario);
				return ResponseEntity.ok(updatedInventory);
			})
			.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> eliminarInventario(@PathVariable Long id) {
		return inventoryService.obtenerPorId(id)
			.map(i -> {
				inventoryService.eliminar(id);
				return ResponseEntity.ok().<Void>build();
			})
			.orElse(ResponseEntity.notFound().build());
	}
}
