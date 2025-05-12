package ru.senla.javacourse.enchilik.scooterrental.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import ru.senla.javacourse.enchilik.scooterrental.api.enumeration.ScooterStatus;

public class ScooterDto {

    private Long id;

    @NotBlank(message = "Модель не может быть пустой")
    @Size(max = 100, message = "Модель не может превышать 100 символов")
    private String model;

    @NotBlank(message = "Серийный номер не может быть пустым")
    @Size(max = 50, message = "Серийный номер не может превышать 50 символов")
    private String serialNumber;

    @NotNull(message = "Статус не может быть null")
    private ScooterStatus status;

    @NotNull(message = "Уровень заряда не может быть null")
    @Min(value = 0, message = "Уровень заряда не может быть отрицательным")
    @Max(value = 100, message = "Уровень заряда не может превышать 100")
    private Integer chargeLevel;

    @PositiveOrZero(message = "Пробег не может быть отрицательным")
    private Double mileage;

    private Long rentalPointId;

    private String rentalPointName;

    private Long tariffId;

    private String tariffName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public ScooterStatus getStatus() {
        return status;
    }

    public void setStatus(ScooterStatus status) {
        this.status = status;
    }

    public Integer getChargeLevel() {
        return chargeLevel;
    }

    public void setChargeLevel(Integer chargeLevel) {
        this.chargeLevel = chargeLevel;
    }

    public Double getMileage() {
        return mileage;
    }

    public void setMileage(Double mileage) {
        this.mileage = mileage;
    }

    public Long getRentalPointId() {
        return rentalPointId;
    }

    public void setRentalPointId(Long rentalPointId) {
        this.rentalPointId = rentalPointId;
    }

    public String getRentalPointName() {
        return rentalPointName;
    }

    public void setRentalPointName(String rentalPointName) {
        this.rentalPointName = rentalPointName;
    }

    public Long getTariffId() {
        return tariffId;
    }

    public void setTariffId(Long tariffId) {
        this.tariffId = tariffId;
    }

    public String getTariffName() {
        return tariffName;
    }

    public void setTariffName(String tariffName) {
        this.tariffName = tariffName;
    }
}
