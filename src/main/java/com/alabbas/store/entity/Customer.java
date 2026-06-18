package com.alabbas.store.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Customer name is required")
    @Size(max = 150, message = "Customer name must not exceed 150 characters")
    private String name;

    @NotBlank(message = "Phone number is required")
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    private String phone;

    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Area is required")
    private String area;

    @NotBlank(message = "Building number is required")
    private String buildingNumber;

    private String floor;

    private String apartment;

    @NotBlank(message = "Address is required")
    @Column(length = 500)
    private String address;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
