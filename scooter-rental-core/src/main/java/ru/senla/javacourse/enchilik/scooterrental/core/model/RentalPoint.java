package ru.senla.javacourse.enchilik.scooterrental.core.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "rental_points")
public class RentalPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    //    TODO: добавить scooters в таблицу rental_point?????
    @OneToMany(
        mappedBy = "rentalPoint",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY)
    private List<Scooter> scooters;

    //    TODO: удалить, если не потребуется
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_point_id")
    private RentalPoint parentPoint;

    @OneToMany(mappedBy = "parentPoint", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RentalPoint> childPoints;


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

    public List<Scooter> getScooters() {
        return scooters;
    }

    public void setScooters(List<Scooter> scooters) {
        this.scooters = scooters;
    }

    public RentalPoint getParentPoint() {
        return parentPoint;
    }

    public void setParentPoint(RentalPoint parentPoint) {
        this.parentPoint = parentPoint;
    }

    public List<RentalPoint> getChildPoints() {
        return childPoints;
    }

    public void setChildPoints(List<RentalPoint> childPoints) {
        this.childPoints = childPoints;
    }
}
