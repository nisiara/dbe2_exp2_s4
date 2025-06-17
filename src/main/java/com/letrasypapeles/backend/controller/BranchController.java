package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Branch;
import com.letrasypapeles.backend.service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/branch")
public class BranchController {

	private BranchService branchService;

	@Autowired
	public BranchController(BranchService branchService) {
		this.branchService = branchService;
	}


	@GetMapping
	public ResponseEntity<List<Branch>> obtenerTodas() {
		List<Branch> sucursales = branchService.obtenerTodas();
		return new ResponseEntity<>(sucursales, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Branch> obtenerPorId(@PathVariable Long id) {
		return branchService.obtenerPorId(id)
			.map(ResponseEntity::ok)
			.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping("/create")
	public ResponseEntity<Branch> crearSucursal(@RequestBody Branch sucursal) {
		Branch nuevaSucursal = branchService.guardar(sucursal);
		return ResponseEntity.ok(nuevaSucursal);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<Branch> actualizarSucursal(@PathVariable Long id, @RequestBody Branch sucursal) {
		return branchService.obtenerPorId(id)
			.map(s -> {
				sucursal.setId(id);
				Branch sucursalActualizada = branchService.guardar(sucursal);
				return ResponseEntity.ok(sucursalActualizada);
			})
			.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> eliminarSucursal(@PathVariable Long id) {
		return branchService.obtenerPorId(id)
			.map(s -> {
				branchService.eliminar(id);
				return ResponseEntity.ok().<Void>build();
			})
			.orElse(ResponseEntity.notFound().build());
	}

}
