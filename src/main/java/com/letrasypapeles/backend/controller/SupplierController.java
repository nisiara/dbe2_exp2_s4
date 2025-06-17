package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Supplier;
import com.letrasypapeles.backend.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/supplier")
public class SupplierController {

	private SupplierService supplierService;

	@Autowired
	public SupplierController(SupplierService supplierService) {
		this.supplierService = supplierService;
	}

	@GetMapping
	public ResponseEntity<List<Supplier>> getAll() {
		List<Supplier> suppliers = supplierService.obtenerTodos();
		return ResponseEntity.ok(suppliers);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Supplier> getById(@PathVariable Long id) {
		return supplierService.obtenerPorId(id)
			.map(ResponseEntity::ok)
			.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping("/create")
	public ResponseEntity<Supplier> createSupplier(@RequestBody Supplier supplier) {
		Supplier newSupplier = supplierService.guardar(supplier);
		return ResponseEntity.ok(newSupplier);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<Supplier> updateSupplier(@PathVariable Long id, @RequestBody Supplier supplier) {
		return supplierService.obtenerPorId(id)
			.map(s -> {
				supplier.setId(id);
				Supplier proveedorActualizado = supplierService.guardar(supplier);
				return ResponseEntity.ok(proveedorActualizado);
			})
			.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
		return supplierService.obtenerPorId(id)
			.map(s -> {
				supplierService.eliminar(id);
				return ResponseEntity.ok().<Void>build();
			})
			.orElse(ResponseEntity.notFound().build());
	}
}
