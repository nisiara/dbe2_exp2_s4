package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Category;
import com.letrasypapeles.backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
	private CategoryRepository categoryRepository;

	@Autowired
	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	public List<Category> obtenerTodas() {
		return categoryRepository.findAll();
	}

	public Optional<Category> obtenerPorId(Long id) {
		return categoryRepository.findById(id);
	}

	public Category guardar(Category categoria) {
		return categoryRepository.save(categoria);
	}

	public void eliminar(Long id) {
		categoryRepository.deleteById(id);
	}
}
