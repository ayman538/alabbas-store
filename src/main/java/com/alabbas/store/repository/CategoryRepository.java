package com.alabbas.store.repository;

import com.alabbas.store.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByNameEnIgnoreCase(String nameEn);
    boolean existsByNameAr(String nameAr);

}