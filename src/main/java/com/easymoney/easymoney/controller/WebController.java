package com.easymoney.easymoney.controller;

import com.easymoney.easymoney.model.Category;
import com.easymoney.easymoney.model.EasyMoney;
import com.easymoney.easymoney.model.Tag;
import com.easymoney.easymoney.model.User;
import com.easymoney.easymoney.service.CategoryService;
import com.easymoney.easymoney.service.EasyMoneyService;
import com.easymoney.easymoney.service.TagService;
import com.easymoney.easymoney.service.UserService;
import com.easymoney.easymoney.scylla.log.service.ScyllaLogService;
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

    @Autowired
    private ScyllaLogService scyllaLogService;

    @GetMapping("/")
    public String list(Model model) {
        model.addAttribute("entries", easyMoneyService.findAll());
        scyllaLogService.log("system", "List entries", "INFO", "Se accedió al listado de registros",
                "WebController.list");
        return "index";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("easyMoney", new EasyMoney());
        model.addAttribute("users", userService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("tags", tagService.findAll());
        scyllaLogService.log("system", "Create form", "INFO", "Se accedió al formulario de creación",
                "WebController.createForm");
        return "easy-money/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute EasyMoney easyMoney, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("users", userService.findAll());
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("tags", tagService.findAll());
            scyllaLogService.log("system", "Validation error", "WARN", "Formulario con errores de validación",
                    "WebController.save");
            return "easy-money/form";
        }

        User user = userService.findById(easyMoney.getUser().getId());
        Category category = categoryService.findById(easyMoney.getCategory().getId());
        List<Tag> tags = tagService.findAllById(easyMoney.getTags().stream().map(Tag::getId).toList());

        easyMoney.setUser(user);
        easyMoney.setCategory(category);
        easyMoney.setTags(tags);

        easyMoneyService.save(easyMoney);
        scyllaLogService.log(user.getName(), "Save entry", "INFO", "Registro guardado correctamente",
                "WebController.save");

        return "redirect:/web/easy-money/";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("easyMoney", easyMoneyService.findById(id));
        model.addAttribute("users", userService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("tags", tagService.findAll());
        scyllaLogService.log("system", "Edit form", "INFO", "Se accedió al formulario de edición para ID: " + id,
                "WebController.editForm");
        return "easy-money/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        easyMoneyService.delete(id);
        scyllaLogService.log("system", "Delete entry", "INFO", "Registro eliminado con ID: " + id,
                "WebController.delete");
        return "redirect:/web/easy-money/";
    }

    @GetMapping("/differences")
    public String differences(@RequestParam String start, @RequestParam String end, Model model) {
        try {
            LocalDate startDate = LocalDate.parse(start);
            LocalDate endDate = LocalDate.parse(end);
            model.addAttribute("differences", easyMoneyService.calculateConsecutiveDifferences(startDate, endDate));
            scyllaLogService.log("system", "Calculate differences", "INFO", "Consulta de diferencias entre fechas",
                    "WebController.differences");
        } catch (Exception e) {
            model.addAttribute("error", "Fechas inválidas. Usa el formato YYYY-MM-DD.");
            scyllaLogService.log("system", "Date parsing error", "ERROR", "Fechas inválidas: " + start + " - " + end,
                    "WebController.differences");
        }
        return "easy-money/differences";
    }

    @GetMapping("/total")
    public String total(@RequestParam String start, @RequestParam String end, Model model) {
        try {
            LocalDate startDate = LocalDate.parse(start);
            LocalDate endDate = LocalDate.parse(end);
            model.addAttribute("total", easyMoneyService.calculateTotalMoneyDifference(startDate, endDate));
            scyllaLogService.log("system", "Calculate total", "INFO", "Consulta de total entre fechas",
                    "WebController.total");
        } catch (Exception e) {
            model.addAttribute("error", "Fechas inválidas. Usa el formato YYYY-MM-DD.");
            scyllaLogService.log("system", "Date parsing error", "ERROR", "Fechas inválidas: " + start + " - " + end,
                    "WebController.total");
        }
        return "easy-money/total";
    }
}