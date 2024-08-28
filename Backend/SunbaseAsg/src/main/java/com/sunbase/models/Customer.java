package com.sunbase.models;


import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Customer {
	@Id
    @Column(name = "uuid", updatable = false, nullable = false)
	private String uuid;

	@JsonProperty("first_name")
    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must be less than 50 characters")
    private String firstName;

    @JsonProperty("last_name")
    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must be less than 50 characters")
    private String lastName;

    @NotBlank(message = "Street is required")
    @Size(max = 100, message = "Street must be less than 100 characters")
    private String street;

    @NotBlank(message = "Address is required")
    @Size(max = 150, message = "Address must be less than 150 characters")
    private String address;

    @NotBlank(message = "City is required")
    @Size(max = 50, message = "City must be less than 50 characters")
    private String city;

    @NotBlank(message = "State is required")
    @Size(max = 50, message = "State must be less than 50 characters")
    private String state;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be between 10 digits")
    private String phone;
}
