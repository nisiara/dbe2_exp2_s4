package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Role;
import com.letrasypapeles.backend.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/role")
@PreAuthorize("hasRole('GERENTE')")
public class RoleController {

	private RoleService roleService;

	@Autowired
	public RoleController(RoleService roleService) {
		this.roleService = roleService;
	}
	@GetMapping
	public ResponseEntity<List<Role>> obtenerTodos() {
		List<Role> roles = roleService.obtenerTodos();
		return ResponseEntity.ok(roles);
	}

	@GetMapping("/{name}")
	public ResponseEntity<Role> obtenerPorNombre(@PathVariable String name) {
		return roleService.obtenerPorNombre(name)
			.map(ResponseEntity::ok)
			.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping("/create")
	public ResponseEntity<Role> crearRole(@RequestBody Role role) {
		Role newRole = roleService.guardar(role);
		return ResponseEntity.ok(newRole);
	}

	@DeleteMapping("/delete/{name}")
	public ResponseEntity<Void> eliminarRole(@PathVariable String name) {
		return roleService.obtenerPorNombre(name)
			.map(r -> {
				roleService.eliminar(name);
				return ResponseEntity.ok().<Void>build();
			})
			.orElse(ResponseEntity.notFound().build());
	}
}
