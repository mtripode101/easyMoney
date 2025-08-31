package com.easymoney.easymoney.controller;

import com.easymoney.easymoney.model.Category;
import com.easymoney.easymoney.service.CategoryService;
import com.easymoney.easymoney.scylla.log.service.ScyllaLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web/categories")
public class CategoryWebController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ScyllaLogService scyllaLogService;

    @GetMapping("/")
    public String list(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        scyllaLogService.log("system", "List categories", "INFO", "Se accedió al listado de categorías", "CategoryWebController.list");
        return "categories/index";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("category", new Category());
        scyllaLogService.log("system", "Create category form", "INFO", "Se accedió al formulario de creación de categoría", "CategoryWebController.createForm");
        return "categories/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Category category) {
        categoryService.save(category);
        scyllaLogService.log("system", "Save category", "INFO", "Categoría guardada correctamente: " + category.getName(), "CategoryWebController.save");
        return "redirect:/web/categories/";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Category category = categoryService.findById(id);
        model.addAttribute("category", category);
        scyllaLogService.log("system", "Edit category form", "INFO", "Se accedió al formulario de edición para categoría ID: " + id, "CategoryWebController.editForm");
        return "categories/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        categoryService.delete(id);
        scyllaLogService.log("system", "Delete category", "INFO", "Categoría eliminada con ID: " + id, "CategoryWebController.delete");
        return "redirect:/web/categories/";
    }
}