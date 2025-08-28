package com.easymoney.easymoney.controller;

import com.easymoney.easymoney.model.Category;
import com.easymoney.easymoney.model.EasyMoney;
import com.easymoney.easymoney.model.Tag;
import com.easymoney.easymoney.model.User;
import com.easymoney.easymoney.service.CategoryService;
import com.easymoney.easymoney.service.EasyMoneyService;
import com.easymoney.easymoney.service.TagService;
import com.easymoney.easymoney.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/web/easy-money")
public class WebController {

    @Autowired
    private EasyMoneyService easyMoneyService;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;

    @GetMapping("/")
    public String list(Model model) {
        model.addAttribute("entries", easyMoneyService.findAll());
        return "index";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("easyMoney", new EasyMoney());
        model.addAttribute("users", userService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("tags", tagService.findAll());
        return "easy-money/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute EasyMoney easyMoney, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("users", userService.findAll());
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("tags", tagService.findAll());
            return "easy-money/form";
        }

        // Reconstrucción explícita
        User user = userService.findById(easyMoney.getUser().getId());
        Category category = categoryService.findById(easyMoney.getCategory().getId());
        List<Tag> tags = tagService.findAllById(
                easyMoney.getTags().stream().map(Tag::getId).toList());

        easyMoney.setUser(user);
        easyMoney.setCategory(category);
        easyMoney.setTags(tags);

        easyMoneyService.save(easyMoney);
        return "redirect:/web/easy-money/";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("easyMoney", easyMoneyService.findById(id));
        model.addAttribute("users", userService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("tags", tagService.findAll());
        return "easy-money/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        easyMoneyService.delete(id);
        return "redirect:/web/easy-money/";
    }

    @GetMapping("/differences")
    public String differences(@RequestParam String start, @RequestParam String end, Model model) {
        try {
            LocalDate startDate = LocalDate.parse(start);
            LocalDate endDate = LocalDate.parse(end);
            model.addAttribute("differences", easyMoneyService.calculateConsecutiveDifferences(startDate, endDate));
        } catch (Exception e) {
            model.addAttribute("error", "Fechas inválidas. Usa el formato YYYY-MM-DD.");
        }
        return "easy-money/differences";
    }

    @GetMapping("/total")
    public String total(@RequestParam String start, @RequestParam String end, Model model) {
        try {
            LocalDate startDate = LocalDate.parse(start);
            LocalDate endDate = LocalDate.parse(end);
            model.addAttribute("total", easyMoneyService.calculateTotalMoneyDifference(startDate, endDate));
        } catch (Exception e) {
            model.addAttribute("error", "Fechas inválidas. Usa el formato YYYY-MM-DD.");
        }
        return "easy-money/total";
    }
}