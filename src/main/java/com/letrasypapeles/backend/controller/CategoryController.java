package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Category;
import com.letrasypapeles.backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/category")
public class CategoryController {

	private CategoryService categoryService;

	@Autowired
	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@GetMapping
	public ResponseEntity<List<Category>> getAllCategories(){
		List<Category> categories = categoryService.obtenerTodas();
		return new ResponseEntity<>(categories, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Category> getCategoryById(@PathVariable Long id){
		return categoryService.obtenerPorId(id)
			.map(ResponseEntity::ok)
			.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping("/create")
	public ResponseEntity<Category> createCategory(@RequestBody Category category){
		Category newCategory = categoryService.guardar(category);
		return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteCategory(@PathVariable Long id){
		return categoryService.obtenerPorId(id)
			.map(category -> {
				categoryService.eliminar(id);
				return ResponseEntity.ok().<Void>build();
			})
			.orElse(ResponseEntity.notFound().build());
	}
}
