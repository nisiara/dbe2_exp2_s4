package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Product;
import com.letrasypapeles.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/product")
public class ProductController {

	private ProductService productService;

	@Autowired
	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping
	public ResponseEntity<List<Product>> obtenerTodos() {
		List<Product> products = productService.obtenerTodos();
		return ResponseEntity.ok(products);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Product> obtenerPorId(@PathVariable Long id) {
		return productService.obtenerPorId(id)
			.map(ResponseEntity::ok)
			.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Product> crearProducto(@RequestBody Product product) {
		Product newProduct = productService.guardar(product);
		return ResponseEntity.ok(newProduct);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Product> actualizarProducto(@PathVariable Long id, @RequestBody Product product) {
		return productService.obtenerPorId(id)
			.map(p -> {
				product.setId(id);
				Product updatedProduct = productService.guardar(product);
				return ResponseEntity.ok(updatedProduct);
			})
			.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
		return productService.obtenerPorId(id)
			.map(p -> {
				productService.eliminar(id);
				return ResponseEntity.ok().<Void>build();
			})
			.orElse(ResponseEntity.notFound().build());
	}
}
