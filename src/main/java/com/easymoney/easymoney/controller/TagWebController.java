package com.easymoney.easymoney.controller;

import com.easymoney.easymoney.model.Tag;
import com.easymoney.easymoney.service.TagService;
import com.easymoney.easymoney.scylla.log.service.ScyllaLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web/tags")
public class TagWebController {

    @Autowired
    private TagService tagService;

    @Autowired
    private ScyllaLogService scyllaLogService;

    @GetMapping("/")
    public String list(Model model) {
        model.addAttribute("tags", tagService.findAll());
        scyllaLogService.log("system", "List tags", "INFO", "Se accedió al listado de etiquetas",
                "TagWebController.list");
        return "tags/index";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("tag", new Tag());
        scyllaLogService.log("system", "Create tag form", "INFO", "Se accedió al formulario de creación de etiqueta",
                "TagWebController.createForm");
        return "tags/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Tag tag) {
        tagService.save(tag);
        scyllaLogService.log("system", "Save tag", "INFO", "Etiqueta guardada correctamente: " + tag.getLabel(),
                "TagWebController.save");
        return "redirect:/web/tags/";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Tag tag = tagService.findById(id);
        model.addAttribute("tag", tag);
        scyllaLogService.log("system", "Edit tag form", "INFO",
                "Se accedió al formulario de edición para etiqueta ID: " + id, "TagWebController.editForm");
        return "tags/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        tagService.delete(id);
        scyllaLogService.log("system", "Delete tag", "INFO", "Etiqueta eliminada con ID: " + id,
                "TagWebController.delete");
        return "redirect:/web/tags/";
    }
}