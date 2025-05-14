package ru.senla.javacourse.enchilik.scooterrental.api.enumeration;

public enum TariffType {
    BASIC("Basic tariff"),
    HOURLY("Hourly tariff, single rent"),
    PREPAID_MINUTES("Prepaid minutes, multiple rents, limited to a period");

    private String name;

    TariffType(String name) {
        this.name = name;
    }

    //    public String getName() {
    //        return name;
    //    }
}
