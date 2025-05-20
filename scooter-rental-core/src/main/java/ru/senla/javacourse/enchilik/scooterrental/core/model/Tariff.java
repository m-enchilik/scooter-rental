package ru.senla.javacourse.enchilik.scooterrental.core.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import ru.senla.javacourse.enchilik.scooterrental.api.enumeration.TariffType;

@Entity
@Table(name = "tariffs")
public class Tariff {

    //    TODO: создать конструктор тарифов
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //    TODO: добавить отношения и аннотации
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TariffType type;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "units_included")
    private Integer unitsIncluded;

    @Column(name = "validity_period_hours")
    private Integer validityPeriodHours;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TariffType getType() {
        return type;
    }

    public void setType(TariffType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getUnitsIncluded() {
        return unitsIncluded;
    }

    public void setUnitsIncluded(Integer unitsIncluded) {
        this.unitsIncluded = unitsIncluded;
    }

    public Integer getValidityPeriodHours() {
        return validityPeriodHours;
    }

    public void setValidityPeriodHours(Integer validityPeriodHours) {
        this.validityPeriodHours = validityPeriodHours;
    }
}
