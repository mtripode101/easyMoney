package com.easymoney.easymoney.service;

import com.easymoney.easymoney.dto.MoneyDifference;
import com.easymoney.easymoney.model.EasyMoney;
import com.easymoney.easymoney.repository.EasyMoneyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class EasyMoneyService {

    @Autowired
    private EasyMoneyRepository repository;

    public EasyMoney save(EasyMoney em) {
        return repository.save(em);
    }

    public EasyMoney update(Long id, EasyMoney updated) {
        EasyMoney existing = repository.findById(id).orElseThrow();
        existing.setDate(updated.getDate());
        existing.setMoney(updated.getMoney());
        existing.setDescription(updated.getDescription());
        return repository.save(existing);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<EasyMoney> findAll() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "date"));
        //return repository.findAll();
    }

    public BigDecimal calculateDifference(LocalDate start, LocalDate end) {
        List<EasyMoney> entries = repository.findByDateBetween(start, end);
        return entries.stream()
                .map(EasyMoney::getMoney)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
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

    public List<MoneyDifference> calculateConsecutiveDifferences(LocalDate start, LocalDate end) {
        List<EasyMoney> entries = repository.findByDateBetween(start, end)
                .stream()
                .sorted(Comparator.comparing(EasyMoney::getDate))
                .toList();

        List<MoneyDifference> differences = new ArrayList<>();

        for (int i = 1; i < entries.size(); i++) {
            EasyMoney previous = entries.get(i - 1);
            EasyMoney current = entries.get(i);

            BigDecimal fromAmount = previous.getMoney();
            BigDecimal toAmount = current.getMoney();
            BigDecimal diff = toAmount.subtract(fromAmount);

            BigDecimal percentageChange = BigDecimal.ZERO;
            if (fromAmount.compareTo(BigDecimal.ZERO) != 0) {
                percentageChange = diff.multiply(BigDecimal.valueOf(100))
                        .divide(fromAmount, 2, RoundingMode.HALF_UP);
            }

            differences.add(new MoneyDifference(
                    previous.getDate(),
                    current.getDate(),
                    fromAmount,
                    toAmount,
                    diff,
                    percentageChange));
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

        BigDecimal fromAmount = first.getMoney();
        BigDecimal toAmount = last.getMoney();
        BigDecimal difference = toAmount.subtract(fromAmount);

        BigDecimal percentageChange = BigDecimal.ZERO;
        if (fromAmount.compareTo(BigDecimal.ZERO) != 0) {
            percentageChange = difference.multiply(BigDecimal.valueOf(100))
                    .divide(fromAmount, 2, RoundingMode.HALF_UP);
        }

        return new MoneyDifference(
                first.getDate(),
                last.getDate(),
                fromAmount,
                toAmount,
                difference,
                percentageChange);
    }

    public EasyMoney findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado con id: " + id));
    }
}
