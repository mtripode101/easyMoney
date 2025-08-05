package com.easymoney.easymoney.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.easymoney.easymoney.dto.MoneyDifference;
import com.easymoney.easymoney.model.EasyMoney;
import com.easymoney.easymoney.service.EasyMoneyService;


@RestController
@RequestMapping("/api/react/easy-money")
@CrossOrigin(origins = "http://localhost:3000") // Permite peticiones desde React
public class EasyMoneyRestController {

    @Autowired
    private EasyMoneyService service;

    // 🔍 Obtener todos los registros
    @GetMapping
    public List<EasyMoney> list() {
        return service.findAll();
    }

    // ➕ Crear nuevo registro
    @PostMapping
    public EasyMoney save(@RequestBody EasyMoney easyMoney) {
        return service.save(easyMoney);
    }

    // 🔄 Obtener un registro por ID
    @GetMapping("/{id}")
    public EasyMoney getById(@PathVariable Long id) {
        return service.findById(id);
    }

    // ✏️ Editar un registro existente
    @PutMapping("/{id}")
    public EasyMoney update(@PathVariable Long id, @RequestBody EasyMoney updated) {
        EasyMoney existing = service.findById(id);
        if (existing != null) {
            existing.setDescription(updated.getDescription());
            // existing.setAmount(updated.getAmount());
            existing.setDate(updated.getDate());
            return service.save(existing);
        }
        return null;
    }

    // 🗑️ Eliminar un registro
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    // 📊 Calcular diferencias entre fechas
    @GetMapping("/differences")
    public List<MoneyDifference> differences(@RequestParam String start, @RequestParam String end) {
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        return service.calculateConsecutiveDifferences(startDate, endDate);
    }

    // 💰 Calcular total de diferencias entre fechas
    @GetMapping("/total")
    public MoneyDifference total(@RequestParam String start, @RequestParam String end) {
        MoneyDifference result = service.calculateTotalMoneyDifference(LocalDate.parse(start), LocalDate.parse(end));
        return result; // Asegúrate de que MoneyDifference tenga este método
    }
}
