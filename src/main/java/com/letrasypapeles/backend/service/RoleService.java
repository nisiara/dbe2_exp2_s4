package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Role;
import com.letrasypapeles.backend.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

	private RoleRepository roleRepository;

	@Autowired
	public RoleService(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	public List<Role> obtenerTodos() {
		return roleRepository.findAll();
	}

	public Optional<Role> obtenerPorNombre(String nombre) {
		return roleRepository.findByName(nombre);
	}

	public Role guardar(Role role) {
		return roleRepository.save(role);
	}

	public void eliminar(String name) {
		roleRepository.deleteByName(name);
	}
}
