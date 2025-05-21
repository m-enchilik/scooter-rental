package ru.senla.javacourse.enchilik.scooterrental.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public class RentalDto {
    private Long id;

    //    @NotNull(message = "ID пользователя не может быть null")
    private Long userId;

    private String username;

    //    @NotNull(message = "ID самоката не может быть null")
    private Long scooterId;

    private String scooterModel;

    //    @NotNull(message = "Время начала аренды не может быть null")
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Double startMileage;
    private Double endMileage;

    private BigDecimal totalCost;

    private Long subscriptionId;

    private String tariffName;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getScooterId() {
        return scooterId;
    }

    public void setScooterId(Long scooterId) {
        this.scooterId = scooterId;
    }

    public String getScooterModel() {
        return scooterModel;
    }

    public void setScooterModel(String scooterModel) {
        this.scooterModel = scooterModel;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Double getStartMileage() {
        return startMileage;
    }

    public void setStartMileage(Double startMileage) {
        this.startMileage = startMileage;
    }

    public Double getEndMileage() {
        return endMileage;
    }

    public void setEndMileage(Double endMileage) {
        this.endMileage = endMileage;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getTariffName() {
        return tariffName;
    }

    public void setTariffName(String tariffName) {
        this.tariffName = tariffName;
    }
}
