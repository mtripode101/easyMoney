package com.easymoney.easymoney.service;

import com.easymoney.easymoney.dto.MoneyDifference;
import com.easymoney.easymoney.model.EasyMoney;
import com.easymoney.easymoney.repository.EasyMoneyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class EasyMoneyService {

    private static final Logger log = LoggerFactory.getLogger(EasyMoneyService.class);

    @Autowired
    private EasyMoneyRepository repository;

    @Value("${easymoney.threshold:0.00}")
    private BigDecimal threshold;

    public EasyMoney save(EasyMoney em) {
        log.info("Guardando nuevo registro: {}", em);
        return repository.save(em);
    }

    public EasyMoney update(Long id, EasyMoney updated) {
        EasyMoney existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado con id: " + id));

        existing.setDate(updated.getDate());
        existing.setMoney(updated.getMoney());
        existing.setDescription(updated.getDescription());

        log.info("Actualizando registro con id {}: {}", id, updated);
        return repository.save(existing);
    }

    public void delete(Long id) {
        log.warn("Eliminando registro con id: {}", id);
        repository.deleteById(id);
    }

    public List<EasyMoney> findAll() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "date"));
    }

    public EasyMoney findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado con id: " + id));
    }

    public List<EasyMoney> findByDateRange(LocalDate start, LocalDate end) {
        return repository.findByDateBetween(start, end);
    }

    public List<EasyMoney> searchByDescription(String keyword) {
        return repository.findByDescriptionContainingIgnoreCase(keyword);
    }

    public Page<EasyMoney> getPaged(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return repository.findAll(pageable);
    }

    public BigDecimal calculateDifference(LocalDate start, LocalDate end) {
        List<EasyMoney> entries = repository.findByDateBetween(start, end);
        return entries.stream()
                .map(EasyMoney::getMoney)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<MoneyDifference> calculateConsecutiveDifferences(LocalDate start, LocalDate end) {
        List<EasyMoney> entries = repository.findByDateBetween(start, end)
                .stream()
                .sorted(Comparator.comparing(EasyMoney::getDate))
                .toList();

        List<MoneyDifference> differences = new ArrayList<>();

        for (int i = 1; i < entries.size(); i++) {
            EasyMoney previous = entries.get(i - 1);
            EasyMoney current = entries.get(i);

            differences.add(MoneyDifference.of(
                    previous.getDate(),
                    current.getDate(),
                    previous.getMoney(),
                    current.getMoney()));
        }

        return differences;
    }

    public MoneyDifference calculateTotalMoneyDifference(LocalDate start, LocalDate end) {
        List<EasyMoney> entries = repository.findByDateBetween(start, end)
                .stream()
                .sorted(Comparator.comparing(EasyMoney::getDate))
                .toList();

        if (entries.size() < 2) {
            throw new IllegalArgumentException(
                    "Se necesitan al menos dos registros para calcular la diferencia total.");
        }

        EasyMoney first = entries.get(0);
        EasyMoney last = entries.get(entries.size() - 1);

        return MoneyDifference.of(
                first.getDate(),
                last.getDate(),
                first.getMoney(),
                last.getMoney());
    }
}