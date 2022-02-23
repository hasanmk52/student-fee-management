package com.rakbank.commons.constants;

public enum School {
    SKIPLY_SCHOOL("Skiply School of Excellence - Integrate Model");

    private String name;

    School(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}