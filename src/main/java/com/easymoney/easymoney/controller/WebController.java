package com.easymoney.easymoney.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.easymoney.easymoney.model.EasyMoney;
import com.easymoney.easymoney.service.EasyMoneyService;

@Controller
@RequestMapping("/web/easy-money")
public class WebController {

    @Autowired
    private EasyMoneyService service;

    @GetMapping("/")
    public String list(Model model) {
        model.addAttribute("entries", service.findAll());
        return "index";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("easyMoney", new EasyMoney());
        return "form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute EasyMoney easyMoney) {
        service.save(easyMoney);
        return "redirect:/web/easy-money/";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("easyMoney", service.findById(id));
        return "form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/web/easy-money/";
    }

    @GetMapping("/differences")
    public String differences(@RequestParam String start, @RequestParam String end, Model model) {
        try {
            LocalDate startDate = LocalDate.parse(start);
            LocalDate endDate = LocalDate.parse(end);
            model.addAttribute("differences", service.calculateConsecutiveDifferences(startDate, endDate));
        } catch (Exception e) {
            model.addAttribute("error", "Fechas inválidas. Usa el formato YYYY-MM-DD.");
        }
        return "differences";
    }

    @GetMapping("/total")
    public String total(@RequestParam String start, @RequestParam String end, Model model) {
        model.addAttribute("total",
                service.calculateTotalMoneyDifference(LocalDate.parse(start), LocalDate.parse(end)));
        return "total";
    }
}