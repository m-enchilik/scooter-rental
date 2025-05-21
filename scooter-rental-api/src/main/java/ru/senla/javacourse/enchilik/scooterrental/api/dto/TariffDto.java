package ru.senla.javacourse.enchilik.scooterrental.api.dto;

import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import ru.senla.javacourse.enchilik.scooterrental.api.enumeration.TariffType;

public class TariffDto {

    private Long id;

    private TariffType type;

    //    @NotBlank(message = "Название тарифа не может быть пустым")
    @Size(max = 50, message = "Название тарифа не может превышать 50 символов")
    private String name;

    @Size(max = 255, message = "Описание тарифа не может превышать 255 символов")
    private String description;

    @PositiveOrZero(message = "Цена абонемента не может быть отрицательной")
    private BigDecimal subscriptionPrice;

    private Long unitsIncluded;

    private Integer validityPeriodHours;

    //    @NotNull(message = "Поле 'isSubscription' не может быть null")
    private Boolean isSubscription;

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

    public BigDecimal getSubscriptionPrice() {
        return subscriptionPrice;
    }

    public void setSubscriptionPrice(BigDecimal subscriptionPrice) {
        this.subscriptionPrice = subscriptionPrice;
    }

    public Long getUnitsIncluded() {
        return unitsIncluded;
    }

    public void setUnitsIncluded(Long unitsIncluded) {
        this.unitsIncluded = unitsIncluded;
    }

    public Integer getValidityPeriodHours() {
        return validityPeriodHours;
    }

    public void setValidityPeriodHours(Integer validityPeriodHours) {
        this.validityPeriodHours = validityPeriodHours;
    }

    public Boolean getIsSubscription() {
        return isSubscription;
    }

    public void setIsSubscription(Boolean subscription) {
        isSubscription = subscription;
    }
}
