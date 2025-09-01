package com.easymoney.easymoney.controller;

import com.easymoney.easymoney.dto.MoneyDifference;
import com.easymoney.easymoney.model.EasyMoney;
import com.easymoney.easymoney.service.EasyMoneyService;
import com.easymoney.easymoney.scylla.log.service.ScyllaLogService;
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
    private final ScyllaLogService scyllaLogService;

    public EasyMoneyGraphQLController(EasyMoneyService service, ScyllaLogService scyllaLogService) {
        this.service = service;
        this.scyllaLogService = scyllaLogService;
    }

    // ✏️ Mutations

    @MutationMapping
    public EasyMoney createEasyMoney(@Argument EasyMoney input) {
        EasyMoney saved = service.save(input);
        scyllaLogService.log("system", "Create EasyMoney", "INFO", "Registro creado vía GraphQL",
                "EasyMoneyGraphQLController.createEasyMoney");
        return saved;
    }

    @MutationMapping
    public EasyMoney updateEasyMoney(@Argument Long id, @Argument EasyMoney input) {
        EasyMoney updated = service.update(id, input);
        scyllaLogService.log("system", "Update EasyMoney", "INFO", "Registro actualizado vía GraphQL con ID: " + id,
                "EasyMoneyGraphQLController.updateEasyMoney");
        return updated;
    }

    @MutationMapping
    public Boolean deleteEasyMoney(@Argument Long id) {
        service.delete(id);
        scyllaLogService.log("system", "Delete EasyMoney", "INFO", "Registro eliminado vía GraphQL con ID: " + id,
                "EasyMoneyGraphQLController.deleteEasyMoney");
        return true;
    }

    // 🔍 Queries

    @QueryMapping
    public EasyMoney easyMoneyById(@Argument Long id) {
        EasyMoney result = service.findById(id);
        scyllaLogService.log("system", "Query EasyMoney by ID", "INFO", "Consulta por ID vía GraphQL: " + id,
                "EasyMoneyGraphQLController.easyMoneyById");
        return result;
    }

    @QueryMapping
    public List<EasyMoney> allEasyMoney() {
        List<EasyMoney> result = service.findAll();
        scyllaLogService.log("system", "Query all EasyMoney", "INFO", "Consulta de todos los registros vía GraphQL",
                "EasyMoneyGraphQLController.allEasyMoney");
        return result;
    }

    @QueryMapping
    public List<EasyMoney> transactionsByDateRange(@Argument String start, @Argument String end) {
        List<EasyMoney> result = service.findByDateRange(LocalDate.parse(start), LocalDate.parse(end));
        scyllaLogService.log("system", "Query by date range", "INFO",
                "Consulta por rango de fechas: " + start + " a " + end,
                "EasyMoneyGraphQLController.transactionsByDateRange");
        return result;
    }

    @QueryMapping
    public List<EasyMoney> searchTransactions(@Argument String keyword) {
        List<EasyMoney> result = service.searchByDescription(keyword);
        scyllaLogService.log("system", "Search transactions", "INFO", "Búsqueda por descripción: " + keyword,
                "EasyMoneyGraphQLController.searchTransactions");
        return result;
    }

    @QueryMapping
    public List<EasyMoney> pagedTransactions(@Argument int page, @Argument int size, @Argument String sortBy) {
        List<EasyMoney> result = service.getPaged(page, size, sortBy).getContent();
        scyllaLogService.log("system", "Paged transactions", "INFO",
                "Consulta paginada: page=" + page + ", size=" + size + ", sortBy=" + sortBy,
                "EasyMoneyGraphQLController.pagedTransactions");
        return result;
    }

    @QueryMapping
    public List<MoneyDifference> consecutiveDifferences(@Argument String start, @Argument String end,
            @Argument String sortBy) {
        List<MoneyDifference> differences = service.calculateConsecutiveDifferences(LocalDate.parse(start),
                LocalDate.parse(end));
        Comparator<MoneyDifference> comparator = switch (sortBy) {
            case "difference" -> MoneyDifference.byDifferenceDesc();
            case "toAmount" -> MoneyDifference.byToAmountAsc();
            default -> MoneyDifference.byFromDateAsc();
        };
        scyllaLogService.log("system", "Consecutive differences", "INFO",
                "Consulta de diferencias consecutivas: " + start + " a " + end + ", sortBy=" + sortBy,
                "EasyMoneyGraphQLController.consecutiveDifferences");
        return differences.stream().sorted(comparator).toList();
    }

    @QueryMapping
    public MoneyDifference totalMoneyDifference(@Argument String start, @Argument String end) {
        MoneyDifference result = service.calculateTotalMoneyDifference(LocalDate.parse(start), LocalDate.parse(end));
        scyllaLogService.log("system", "Total money difference", "INFO",
                "Consulta de diferencia total: " + start + " a " + end,
                "EasyMoneyGraphQLController.totalMoneyDifference");
        return result;
    }

    @QueryMapping
    public BigDecimal difference(@Argument String start, @Argument String end) {
        BigDecimal result = service.calculateDifference(LocalDate.parse(start), LocalDate.parse(end));
        scyllaLogService.log("system", "Raw difference", "INFO",
                "Consulta de diferencia monetaria: " + start + " a " + end, "EasyMoneyGraphQLController.difference");
        return result;
    }
}