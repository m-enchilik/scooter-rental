package ru.senla.javacourse.enchilik.scooterrental.api.dto;

import jakarta.validation.constraints.Size;
import java.util.List;

public class RentalPointDto {

    private Long id;

    //    @NotBlank(message = "Название точки проката не может быть пустым")
    @Size(max = 100, message = "Название точки проката не может превышать 100 символов")
    private String name;

    //    @NotBlank(message = "Адрес не может быть пустым")
    @Size(max = 255, message = "Адрес не может превышать 255 символов")
    private String address;

    //    @NotNull(message = "Широта не может быть null")
    private Double latitude;

    //    @NotNull(message = "Долгота не может быть null")
    private Double longitude;

    private Long parentPointId;

    private List<ScooterDto> scooters;

    private List<RentalPointDto> childPoints;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Long getParentPointId() {
        return parentPointId;
    }

    public void setParentPointId(Long parentPointId) {
        this.parentPointId = parentPointId;
    }

    public List<ScooterDto> getScooters() {
        return scooters;
    }

    public void setScooters(List<ScooterDto> scooters) {
        this.scooters = scooters;
    }

    public List<RentalPointDto> getChildPoints() {
        return childPoints;
    }

    public void setChildPoints(List<RentalPointDto> childPoints) {
        this.childPoints = childPoints;
    }
}
