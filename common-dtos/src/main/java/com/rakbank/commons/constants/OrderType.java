package com.rakbank.commons.constants;

public enum OrderType {
    TUTION_FEES("Tution Fees");

    private String name;

    OrderType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
