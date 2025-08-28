package com.easymoney.easymoney.controller;

import com.easymoney.easymoney.model.Tag;
import com.easymoney.easymoney.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web/tags")
public class TagWebController {

    @Autowired
    private TagService tagService;

    @GetMapping("/")
    public String list(Model model) {
        model.addAttribute("tags", tagService.findAll());
        return "tags/index";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("tag", new Tag());
        return "tags/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Tag tag) {
        tagService.save(tag);
        return "redirect:/web/tags/";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("tag", tagService.findById(id));
        return "tags/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        tagService.delete(id);
        return "redirect:/web/tags/";
    }
}