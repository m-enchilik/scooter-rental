package ru.senla.javacourse.enchilik.scooterrental.api.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class SubscriptionDto {
    private Long id;

    @NotNull(message = "ID пользователя не может быть null")
    private Long userId;

    @NotNull(message = "ID тарифа не может быть null")
    private Long tariffId;
    // TODO: Наименование и описание тарифа
    private LocalDateTime expirationTime;

    private Long restUnits;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTariffId() {
        return tariffId;
    }

    public void setTariffId(Long tariffId) {
        this.tariffId = tariffId;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }

    public Long getRestUnits() {
        return restUnits;
    }

    public void setRestUnits(Long restUnits) {
        this.restUnits = restUnits;
    }
}
