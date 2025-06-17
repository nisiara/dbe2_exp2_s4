package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Supplier;
import com.letrasypapeles.backend.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierService {

	private SupplierRepository supplierRepository;

	@Autowired
	public SupplierService(SupplierRepository supplierRepository) {
		this.supplierRepository = supplierRepository;
	}

	public List<Supplier> obtenerTodos() {
		return supplierRepository.findAll();
	}

	public Optional<Supplier> obtenerPorId(Long id) {
		return supplierRepository.findById(id);
	}

	public Supplier guardar(Supplier proveedor) {
		return supplierRepository.save(proveedor);
	}

	public void eliminar(Long id) {
		supplierRepository.deleteById(id);
	}

	public Supplier crearProveedor(Supplier proveedor) {

		if (proveedor == null) {
			throw new IllegalArgumentException("El proveedor no puede ser nulo.");
		}
		if (!StringUtils.hasText(proveedor.getName())) { // Verifica que no sea nulo, vacío o solo espacios
			throw new IllegalArgumentException("El nombre del proveedor es obligatorio.");
		}
		if (!StringUtils.hasText(proveedor.getContact())) {
			throw new IllegalArgumentException("El contacto del proveedor es obligatorio.");
		}
		return supplierRepository.save(proveedor);
	}
}
