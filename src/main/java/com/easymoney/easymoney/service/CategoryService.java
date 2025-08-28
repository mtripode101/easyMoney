package com.easymoney.easymoney.service;

import com.easymoney.easymoney.model.Category;
import com.easymoney.easymoney.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    public Category save(Category category) {
        return repository.save(category);
    }

    public Category findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + id));
    }

    public List<Category> findAll() {
        return repository.findAll();
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Category findByName(String name) {
        return repository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con nombre: " + name));
    }
}