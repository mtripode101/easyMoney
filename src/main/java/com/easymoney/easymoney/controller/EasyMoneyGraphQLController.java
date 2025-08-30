package com.easymoney.easymoney.controller;

import com.easymoney.easymoney.dto.MoneyDifference;
import com.easymoney.easymoney.model.EasyMoney;
import com.easymoney.easymoney.service.EasyMoneyService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Controller
public class EasyMoneyGraphQLController {

    private final EasyMoneyService service;

    public EasyMoneyGraphQLController(EasyMoneyService service) {
        this.service = service;
    }

    // ✏️ Mutations

    @MutationMapping
    public EasyMoney createEasyMoney(@Argument EasyMoney input) {
        return service.save(input);
    }

    @MutationMapping
    public EasyMoney updateEasyMoney(@Argument Long id, @Argument EasyMoney input) {
        return service.update(id, input);
    }

    @MutationMapping
    public Boolean deleteEasyMoney(@Argument Long id) {
        service.delete(id);
        return true;
    }

    // 🔍 Queries

    @QueryMapping
    public EasyMoney easyMoneyById(@Argument Long id) {
        return service.findById(id);
    }

    @QueryMapping
    public List<EasyMoney> allEasyMoney() {
        return service.findAll();
    }

    @QueryMapping
    public List<EasyMoney> transactionsByDateRange(@Argument String start, @Argument String end) {
        return service.findByDateRange(LocalDate.parse(start), LocalDate.parse(end));
    }

    @QueryMapping
    public List<EasyMoney> searchTransactions(@Argument String keyword) {
        return service.searchByDescription(keyword);
    }

    @QueryMapping
    public List<EasyMoney> pagedTransactions(@Argument int page, @Argument int size, @Argument String sortBy) {
        return service.getPaged(page, size, sortBy).getContent();
    }

    @QueryMapping
    public List<MoneyDifference> consecutiveDifferences(
            @Argument String start,
            @Argument String end,
            @Argument String sortBy) {
        List<MoneyDifference> differences = service.calculateConsecutiveDifferences(
                LocalDate.parse(start), LocalDate.parse(end));

        Comparator<MoneyDifference> comparator = switch (sortBy) {
            case "difference" -> MoneyDifference.byDifferenceDesc();
            case "toAmount" -> MoneyDifference.byToAmountAsc();
            default -> MoneyDifference.byFromDateAsc();
        };

        return differences.stream().sorted(comparator).toList();
    }

    @QueryMapping
    public MoneyDifference totalMoneyDifference(@Argument String start, @Argument String end) {
        return service.calculateTotalMoneyDifference(LocalDate.parse(start), LocalDate.parse(end));
    }

    @QueryMapping
    public BigDecimal difference(@Argument String start, @Argument String end) {
        return service.calculateDifference(LocalDate.parse(start), LocalDate.parse(end));
    }
}