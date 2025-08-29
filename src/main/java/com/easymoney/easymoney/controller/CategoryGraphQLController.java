package com.easymoney.easymoney.controller;

import com.easymoney.easymoney.model.Category;
import com.easymoney.easymoney.model.EasyMoney;
import com.easymoney.easymoney.service.CategoryService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class CategoryGraphQLController {

    private final CategoryService categoryService;

    public CategoryGraphQLController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @SchemaMapping(typeName = "Query", field = "categoryById")
    public Category getCategoryById(@Argument Long id) {
        return categoryService.findById(id);
    }

    @SchemaMapping(typeName = "Query", field = "categoryByName")
    public Category getCategoryByName(@Argument String name) {
        return categoryService.findByName(name);
    }

    @SchemaMapping(typeName = "Query", field = "searchCategories")
    public List<Category> searchCategories(@Argument String keyword) {
        return categoryService.findByNameContainingIgnoreCase(keyword);
    }

    @SchemaMapping(typeName = "Query", field = "allCategories")
    public List<Category> getAllCategories() {
        return categoryService.findAll();
    }

    @SchemaMapping
    public List<EasyMoney> easyMoneyList(Category category) {
        return category.getTransactions(); // Asumiendo que tenés un getter en Category
    }
}