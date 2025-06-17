package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Product;
import com.letrasypapeles.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
	private ProductRepository productRepository;

	@Autowired
	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public List<Product> obtenerTodos() {
		return productRepository.findAll();
	}

	public Optional<Product> obtenerPorId(Long id) {
		return productRepository.findById(id);
	}

	public Product guardar(Product producto) {
		return productRepository.save(producto);
	}

	public void eliminar(Long id) {
		productRepository.deleteById(id);
	}

	// Otros métodos según tus necesidades
}
