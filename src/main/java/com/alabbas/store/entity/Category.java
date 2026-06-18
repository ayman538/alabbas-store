package com.alabbas.store.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotBlank(message = "English name is required")
        @Size(max = 100, message = "English name must not exceed 100 characters")
        @Column(name = "name_en", length = 100, nullable = false)
        private String nameEn;

        @NotBlank(message = "Arabic name is required")
        @Size(max = 100, message = "Arabic name must not exceed 100 characters")
        @Column(name = "name_ar", length = 100, nullable = false)
        private String nameAr;

        private Boolean active = true;

        @Column(updatable = false)
        private LocalDateTime createdAt;

        @PrePersist
        public void prePersist() {
                this.createdAt = LocalDateTime.now();
        }

}
