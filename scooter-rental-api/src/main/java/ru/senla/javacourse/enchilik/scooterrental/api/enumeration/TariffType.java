package ru.senla.javacourse.enchilik.scooterrental.api.enumeration;

public enum TariffType {
    BASIC("Basic tariff"),
    HOURLY("Hourly tariff"),
    FIFTY_MINUTES_A_MONTH("50 minutes a month");

    private String name;

    TariffType(String name) {
        this.name = name;
    }

    //    public String getName() {
    //        return name;
    //    }
}
