package com.openclassrooms.mediscreen.report.enumeration;

public enum Gender {
    MALE("M"),
    FEMALE("F");

    private final String symbol;

    Gender(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public static Gender fromSymbol(String symbol) {
        for (Gender gender : Gender.values()) {
            if (gender.symbol.equals(symbol)) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Invalid gender symbol: " + symbol);
    }
}
