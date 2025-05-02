package ru.senla.javacourse.enchilik.scooter_rental.api.dto;

import java.util.Set;

public class UserProfileDto {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Set<String> roles;
}
