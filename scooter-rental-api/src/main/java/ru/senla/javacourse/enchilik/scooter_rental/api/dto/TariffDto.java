package ru.senla.javacourse.enchilik.scooter_rental.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import ru.senla.javacourse.enchilik.scooter_rental.core.enumeration.TariffType;

public class TariffDto {

    private Long id;

    private TariffType type;

    @NotBlank(message = "Название тарифа не может быть пустым")
    @Size(max = 50, message = "Название тарифа не может превышать 50 символов")
    private String name;

    @Size(max = 255, message = "Описание тарифа не может превышать 255 символов")
    private String description;

    @PositiveOrZero(message = "Цена абонемента не может быть отрицательной")
    private BigDecimal subscriptionPrice;

    private Integer unitsIncluded;

    private Integer validityPeriodHours;

    @NotNull(message = "Поле 'isSubscription' не может быть null")
    private Boolean isSubscription;
}
