package com.easymoney.easymoney.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Comparator;

public class MoneyDifference {

    private LocalDate fromDate;
    private LocalDate toDate;
    private BigDecimal fromAmount;
    private BigDecimal toAmount;
    private BigDecimal difference;
    private BigDecimal percentageChange;

    // Constructor completo
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

    // Método de fábrica para calcular automáticamente
    public static MoneyDifference of(LocalDate fromDate, LocalDate toDate,
            BigDecimal fromAmount, BigDecimal toAmount) {
        BigDecimal difference = toAmount.subtract(fromAmount);
        BigDecimal percentageChange = fromAmount.compareTo(BigDecimal.ZERO) == 0
                ? BigDecimal.ZERO
                : difference.multiply(BigDecimal.valueOf(100))
                        .divide(fromAmount, 2, RoundingMode.HALF_UP);

        return new MoneyDifference(fromDate, toDate, fromAmount, toAmount, difference, percentageChange);
    }

    // Getters y Setters
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

    public BigDecimal getPercentageChange() {
        return percentageChange;
    }

    public void setPercentageChange(BigDecimal percentageChange) {
        this.percentageChange = percentageChange;
    }

    // Comparadores estáticos para ordenamiento
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

    // toString para depuración
    @Override
    public String toString() {
        return String.format("From %s to %s: %s → %s | Δ %s | %s%%",
                fromDate, toDate, fromAmount, toAmount, difference, percentageChange);
    }
}