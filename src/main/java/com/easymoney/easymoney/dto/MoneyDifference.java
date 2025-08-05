package com.easymoney.easymoney.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;

public class MoneyDifference {

    private LocalDate fromDate;
    private LocalDate toDate;
    private BigDecimal fromAmount;
    private BigDecimal toAmount;
    private BigDecimal difference;
    private BigDecimal percentageChange;

    public MoneyDifference(LocalDate fromDate, LocalDate toDate,
            BigDecimal fromAmount, BigDecimal toAmount,
            BigDecimal difference, BigDecimal percentageChange) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.fromAmount = fromAmount;
        this.toAmount = toAmount;
        this.difference = difference;
        this.percentageChange = percentageChange;
    }

    // Getters and Setters

    public BigDecimal getPercentageChange() {
        return percentageChange;
    }

    public void setPercentageChange(BigDecimal percentageChange) {
        this.percentageChange = percentageChange;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public BigDecimal getFromAmount() {
        return fromAmount;
    }

    public void setFromAmount(BigDecimal fromAmount) {
        this.fromAmount = fromAmount;
    }

    public BigDecimal getToAmount() {
        return toAmount;
    }

    public void setToAmount(BigDecimal toAmount) {
        this.toAmount = toAmount;
    }

    public BigDecimal getDifference() {
        return difference;
    }

    public void setDifference(BigDecimal difference) {
        this.difference = difference;
    }

    // 🔍 Comparadores estáticos

    public static Comparator<MoneyDifference> byFromDateAsc() {
        return Comparator.comparing(MoneyDifference::getFromDate);
    }

    public static Comparator<MoneyDifference> byDifferenceDesc() {
        return Comparator.comparing(MoneyDifference::getDifference).reversed();
    }

    public static Comparator<MoneyDifference> byToAmountAsc() {
        return Comparator.comparing(MoneyDifference::getToAmount);
    }

    public static Comparator<MoneyDifference> byPercentageChangeDesc() {
        return Comparator.comparing(MoneyDifference::getPercentageChange).reversed();
    }
}
