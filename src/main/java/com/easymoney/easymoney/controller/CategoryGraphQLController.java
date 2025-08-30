package com.easymoney.easymoney.controller;

import com.easymoney.easymoney.model.Category;
import com.easymoney.easymoney.model.EasyMoney;
import com.easymoney.easymoney.service.CategoryService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class CategoryGraphQLController {

    private final CategoryService categoryService;

    public CategoryGraphQLController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // 🔍 Queries

    @QueryMapping
    public Category categoryById(@Argument Long id) {
        return categoryService.findById(id);
    }

    @QueryMapping
    public Category categoryByName(@Argument String name) {
        return categoryService.findByName(name);
    }

    @QueryMapping
    public List<Category> searchCategories(@Argument String keyword) {
        return categoryService.findByNameContainingIgnoreCase(keyword);
    }

    @QueryMapping
    public List<Category> allCategories() {
        return categoryService.findAll();
    }

    // 🧩 Resolver de relación: Category → EasyMoney

    @SchemaMapping(typeName = "Category", field = "transactions")
    public List<EasyMoney> resolveTransactions(Category category) {
        return category.getTransactions();
    }

    // ✏️ Mutations

    @MutationMapping
    public Category createCategory(@Argument String name) {
        Category category = new Category();
        category.setName(name);
        return categoryService.save(category);
    }

    @MutationMapping
    public Boolean deleteCategory(@Argument Long id) {
        categoryService.delete(id);
        return true;
    }
}