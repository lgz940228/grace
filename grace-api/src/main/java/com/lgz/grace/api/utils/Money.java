package com.lgz.grace.api.utils;

import java.math.BigDecimal;

public class Money {
    private BigDecimal decimal;

    public Money() {
        this.decimal = new BigDecimal("0.00");
    }

    public Money(BigDecimal decimal) {
        if (decimal == null) {
            new BigDecimal("0.00");
        } else {
            this.decimal = decimal;
        }

    }

    public Money(Double d) {
        if (d == null) {
            this.decimal = new BigDecimal("0.00");
        } else {
            this.decimal = new BigDecimal(d);
        }

    }

    public static Money init(Double d) {
        return new Money(d);
    }

    public static Money init(BigDecimal decimal) {
        return new Money(decimal);
    }

    public Money add(Double value) {
        if (value != null) {
            this.decimal = this.decimal.add(new BigDecimal(value));
        }

        return this;
    }

    public Money subtract(Double value) {
        if (value != null) {
            this.decimal = this.decimal.subtract(new BigDecimal(value));
        }

        return this;
    }

    public Money multiply(Double value) {
        if (value != null) {
            this.decimal = this.decimal.multiply(new BigDecimal(value));
        }

        return this;
    }

    public Money divide(Double value) {
        if (value != null) {
            this.decimal = this.decimal.multiply(new BigDecimal(value));
        }

        return this;
    }

    public Double doubleValue() {
        return this.decimal == null ? null : this.decimal.setScale(2, 1).doubleValue();
    }

    public BigDecimal decimalValue() {
        return this.decimal.setScale(2, 1);
    }
}
