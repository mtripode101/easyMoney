package com.easymoney.easymoney.controller;

import com.easymoney.easymoney.dto.MoneyDifference;
import com.easymoney.easymoney.model.EasyMoney;
import com.easymoney.easymoney.service.EasyMoneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/easy-money")
public class EasyMoneyController {

    @Autowired
    private EasyMoneyService service;

    @PostMapping
    public ResponseEntity<EasyMoney> create(@RequestBody EasyMoney em) {
        return ResponseEntity.ok(service.save(em));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EasyMoney> update(@PathVariable Long id, @RequestBody EasyMoney em) {
        return ResponseEntity.ok(service.update(id, em));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<EasyMoney>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/difference")
    public ResponseEntity<BigDecimal> getDifference(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(service.calculateDifference(start, end));
    }

    @GetMapping("/by-date")
    public ResponseEntity<List<EasyMoney>> getByDateRange(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(service.findByDateRange(start, end));
    }

    @GetMapping("/search")
    public ResponseEntity<List<EasyMoney>> searchByDescription(@RequestParam("q") String keyword) {
        return ResponseEntity.ok(service.searchByDescription(keyword));
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<EasyMoney>> getPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "date") String sortBy) {
        return ResponseEntity.ok(service.getPaged(page, size, sortBy));
    }

    @GetMapping("/differences")
    public ResponseEntity<List<MoneyDifference>> getConsecutiveDifferences(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            @RequestParam(name = "sortBy", defaultValue = "fromDate") String sortBy) {
        List<MoneyDifference> differences = service.calculateConsecutiveDifferences(start, end);

        Comparator<MoneyDifference> comparator = switch (sortBy) {
            case "difference" -> MoneyDifference.byDifferenceDesc();
            case "toAmount" -> MoneyDifference.byToAmountAsc();
            default -> MoneyDifference.byFromDateAsc(); // fallback
        };

        List<MoneyDifference> sorted = differences.stream()
                .sorted(comparator)
                .toList();

        return ResponseEntity.ok(sorted);
    }

    @GetMapping("/total-money-difference")
    public ResponseEntity<MoneyDifference> getTotalMoneyDifference(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(service.calculateTotalMoneyDifference(start, end));
    }
}